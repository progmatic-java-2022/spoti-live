package hu.progmatic.spotilive.esemeny;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class EsemenyController {

    @Autowired
    private EsemenyService esemenyService;


    @GetMapping("/esemeny")
    public String esemenyekOldalBetoltes(Model model) {
        return "/esemenyek";
    }

    @PostMapping("/esemeny/delete/{id}")
    public String esemenyTorles(Model model, @PathVariable("id") Integer id) {
        esemenyService.deleteEsemeny(id);
        return "redirect:/esemeny";
    }

    @GetMapping("/esemeny/{id}")
    public String esemenySzerkesztes(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("formEsemenyPeldany",
                esemenyService.getById(id));
        return "/esemenyek";
    }

    @PostMapping("/esemeny/{id}")
    public String esemenyMentese(
            @PathVariable Integer id,
            @ModelAttribute("formEsemenyPeldany") @Valid EsemenyDto ujEsemeny,
            BindingResult bindingResult,
            Model model
    ) {
        if (!bindingResult.hasErrors()) {
            esemenyService.udpate(ujEsemeny, id);
            return "redirect:/esemeny";
        }
        return "/esemenyek";
    }

    @PostMapping("/esemeny")
    public String termekHozzaadas(
            @ModelAttribute("formEsemenyPeldany") @Valid EsemenyDto ujEsemeny,
            BindingResult bindingResult,
            Model model
    ) {
        if (!bindingResult.hasErrors()) {
            esemenyService.createEsemeny(ujEsemeny);
            return "/esemeny";
        }
        return "/esemenyek";
    }

    @ModelAttribute("esemenyekLista")
    public List<EsemenyDto> getEsemenyekLista() {
        return esemenyService.findAllEsemeny();
    }

    @ModelAttribute("formEsemenyPeldany")
    public EsemenyDto formEsemenyPeldany() {
        return EsemenyDto.builder().build();
    }


}
