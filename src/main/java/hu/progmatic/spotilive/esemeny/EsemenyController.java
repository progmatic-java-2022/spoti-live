package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.felhasznalo.FelhasznaloService;
import hu.progmatic.spotilive.felhasznalo.UserType;
import hu.progmatic.spotilive.zenekar.ZenekarDto;
import hu.progmatic.spotilive.zenekar.ZenekarService;
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
    @Autowired
    private FelhasznaloService felhasznaloService;

    @Autowired
    private ZenekarService zenekarService;


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
        model.addAttribute("esemenyModositas",
                esemenyService.getEsemenyDtoById(id));
        return "/esemenyek";
    }

    @PostMapping("/esemeny/{id}")
    public String esemenyMentese(
            @PathVariable Integer id,
            @ModelAttribute("esemenyModositas") @Valid EsemenyDto ujEsemeny,
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
    public String esemenyHozzaadasa(
            @ModelAttribute("createEsemenyCommand") @Valid CreateEsemenyCommand ujEsemeny,
            BindingResult bindingResult,
            Model model
    ) {
        if (!bindingResult.hasErrors()) {
            esemenyService.createEsemeny(ujEsemeny);
            return "redirect:/esemeny";
        }
        return "/esemenyek";
    }

    @ModelAttribute("esemenyekLista")
    public List<EsemenyDto> getEsemenyekLista() {
        return esemenyService.findAllEsemeny();
    }

    @ModelAttribute("createEsemenyCommand")
    public CreateEsemenyCommand createEsemenyCommand() {
        return CreateEsemenyCommand.builder().build();
    }

    @ModelAttribute("esemenyModositas")
    public EsemenyDto esemenyModositas() {
        return EsemenyDto.builder().build();
    }

    @ModelAttribute("esemenyModositasJogVan")
    public boolean esemenyModositasJogVan(){
        return felhasznaloService.hasRole(UserType.Roles.ESEMENY_KEZELES_ROLE);
    }

    @ModelAttribute("adminModositasJogVan")
    public boolean adminModositasJogVan(){return felhasznaloService.hasRole(UserType.Roles.USER_WRITE_ROLE);}


    @ModelAttribute("allZenekar")
    public List<ZenekarDto> allZenekar(){
        return zenekarService.findAllDto();
    }
}
