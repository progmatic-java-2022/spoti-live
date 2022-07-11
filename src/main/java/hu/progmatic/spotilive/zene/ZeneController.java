package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.tag.TagService;
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
public class ZeneController {

    @Autowired
    ZeneService zeneKarbantartasService;

    @Autowired
    TagService tagService;

    @GetMapping("/zene")
    public String oldaBetoltes() {
        return "/zenekarbantartas";
    }

    @GetMapping("/tagek/zene/{zeneid}")
    public String modalBetoltes(Model model, @PathVariable Integer zeneid){
        model.addAttribute("showModal",true);
        model.addAttribute("zeneTagSzerkeztes", tagService.getZeneTagSzerkesztesListaDto(zeneid));
        return "/zenekarbantartas";
    }

    @PostMapping("/zenekarbantartas/zene")
    public String addZene(
            @ModelAttribute("zenePeldany") @Valid CreateZeneCommand command,
            BindingResult bindingresult,
            Model model
    ) {
        model.addAttribute("zeneError", null);
        if (!bindingresult.hasErrors()) {
            try {
                zeneKarbantartasService.createZene(command);
            }catch (CreateZeneExeption e){
                model.addAttribute("zeneError", e.getMessage());
                return "/zenekarbantartas";
            }
            return "redirect:/zene";
        }
        return "/zenekarbantartas";
    }

    @PostMapping("/zenekarbantartas/zene/{id}")
    public String editZene(
            Model model,
            @PathVariable("id") Integer id,
            @ModelAttribute("zenePeldany") @Valid ZeneDto dto,
            BindingResult bindingResult) {
        if(!bindingResult.hasErrors()){
            zeneKarbantartasService.editZene(dto);
            return "redirect:/zene";
        }

        return "/zenekarbantartas";
    }

    @PostMapping("/zenekarbantartas/zene/delete/{id}")
    public String deleteZene(@PathVariable("id") Integer id) {
        zeneKarbantartasService.deleteZeneById(id);
        return "redirect:/zene";
    }


    @GetMapping("/zenekarbantartas/zene/edit/{id}")
    public String editForm(
            Model model,
            @PathVariable Integer id
    ) {
        model.addAttribute("zenePeldany", zeneKarbantartasService.getZeneDtoById(id));
        return "/zenekarbantartas";
    }


    @ModelAttribute("zenePeldany")
    public ZeneDto getZenePeldany() {
        return ZeneDto.builder().build();
    }

    @ModelAttribute("zeneLista")
    public List<ZeneDto> getZenek() {
        return zeneKarbantartasService.findAllDto();
    }

    @ModelAttribute("ujZeneError")
    public String ujZeneError() {
        return null;
    }

    @ModelAttribute("getCim")
    public String getCim() {
        return "Számok";
    }
    @ModelAttribute("zeneError")
    public String getZeneError(){
        return null;
    }


    @ModelAttribute("zeneTagSzerkeztes")
    public ZeneTagSzerkesztesListaDto getZeneTagSzerkeztes(){
        return ZeneTagSzerkesztesListaDto.builder().build();
    }
    @ModelAttribute("showModal")
    public boolean showModal(){
        return false;
    }


}