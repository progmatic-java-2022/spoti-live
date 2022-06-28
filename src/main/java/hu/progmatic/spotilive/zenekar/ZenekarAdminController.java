package hu.progmatic.spotilive.zenekar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ZenekarAdminController {


    @Autowired
    private ZenekarService zenekarService;

    @GetMapping("/zenekarKarbantartas")
    public String oldalbetoltes() {
        return "zenekar_admin";
    }

    @PostMapping("/zenekar/delete/{id}")
    public String delete(
            @PathVariable("id") Integer id
    ) {
        zenekarService.deleteById(id);
        return "redirect:/zenekarKarbantartas";
    }

    @GetMapping("/zenekar/edit/{id}")
    public String zenekarModositas(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("zenekarPeldany", zenekarService.getById(id));
        return "zenekar_admin";
    }

    @PostMapping("/zenekarKarbantartas/zenekar/")
    public String addZenekar(Model model, @ModelAttribute("zenekarPeldany") @Valid ZenekarDto dto,
                             BindingResult bindingResult) {
        model.addAttribute("ujZenekarError", null);
        if ((!dto.getTelefonszam()
                .matches("(?<elotag>\\+36-?[\\d]{2})?" +
                        "(?<masikelotag>06-?[\\d]{2})?(?<elvalaszto1>[\\-\\/])?" +
                        "([\\d]{7})?(?<utotagkotojellel>[\\d]{3}-[\\d]{4})") && (!dto.getTelefonszam().equals(""))))
            bindingResult.addError(new FieldError("zenekarPeldany", "telefonszam", "Helyes formátum pl.: 0630-164-1922"));

        if (!bindingResult.hasErrors()) {
            try {
                var ujZenekar = zenekarService.createZenekar(dto);
                model.addAttribute("zenekarPeldany", ujZenekar);
            } catch (AddZenekarExeption e) {
                model.addAttribute("ujZenekarError", e.getMessage());
                return "zenekar_admin";
            }
            return "redirect:/zenekarKarbantartas";
        }
        return "zenekar_admin";
    }

    @PostMapping("/zenekarKarbantartas/zenekar/{id}")
    public String editZenekar(Model model, @PathVariable("id") Integer id, @ModelAttribute("zenekarPeldany") @Valid ZenekarDto dto,
                              BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            zenekarService.editZenekar(dto);


            return "redirect:/zenekarKarbantartas";
        }
        return "zenekar_admin";
    }

    @ModelAttribute("zenekarLista")
    public List<ZenekarDto> getZenekarList() {
        return zenekarService.findAllDto();
    }

    @ModelAttribute("zenekarPeldany")
    public ZenekarDto zenekarDto() {
        return ZenekarDto.builder().build();
    }

    @ModelAttribute("adminCim")
    public String adminCim() {
        return "Hello Admin";
    }

    @ModelAttribute("ujZenekarError")
    public String zenekarError() {
        return null;
    }
}
