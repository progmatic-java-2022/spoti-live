package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.zenekar.ZenekarDto;
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
public class ZeneKarbantartasController {

    @Autowired
    ZeneKarbantartasService zeneKarbantartasService;

    @GetMapping("/zene")
    public String oldaBetoltes() {
        return "/zenekarbantartas";
    }

    @PostMapping("/zenekarbantartas/zene")
    public String addZene(
            Model model,
            @ModelAttribute("zenePeldany") ZeneDto dto,
            BindingResult bindingresult
    ) {
        if (!bindingresult.hasErrors()) {
            zeneKarbantartasService.createZene(dto);
            return "redirect:/zene";
        }
        return "/zenekarbantartas";
    }

    @PostMapping("/zenekarbantartas/zene/{id}")
    public String editZenekar(
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
    public String deleteZenekar(@PathVariable("id") Integer id) {
        zeneKarbantartasService.deleteById(id);
        return "redirect:/zene";
    }


    @GetMapping("/zenekarbantartas/zene/edit/{id}")
    public String editForm(
            Model model,
            @PathVariable Integer id
    ) {
        model.addAttribute("zenePeldany", zeneKarbantartasService.getById(id));
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
}