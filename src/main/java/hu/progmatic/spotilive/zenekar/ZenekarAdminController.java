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
    public String oldalbetoltes(){
        return "zenekar_admin";
    }

//    @PostMapping("/admin/zenekar/delete/{id}")
//    public String delete (
//            @PathVariable("id") Integer id
//    ){
//        zenekarService.deleteById(id);
//        return "zenekar_admin";
//    }

    @PostMapping("/zenekarKarbantartas/zenekar/")
    public String addZenekar (Model model, @ModelAttribute("zenekarPeldany") @Valid ZenekarDto dto,
                              BindingResult bindingResult){
        if (!dto.getTelefonszam()
                .matches("(?<elotag>\\+36[\\d]{2})?" +
                        "(?<masikelotag>06[\\d]{2})?(?<elvalaszto1>[\\-\\/])?" +
                        "([\\d]{7})?(?<utotagkotojellel>[\\d]{3}-[\\d]{4})"))
            bindingResult.addError(new FieldError("zenekarPeldany", "telefonszam","Helyes formátum pl.: 0630-164-1922"));

        if (!bindingResult.hasErrors()){
           var ujZenekar = zenekarService.createZenekar(dto);
           model.addAttribute("zenekarPeldany", ujZenekar);

            return "redirect:/zenekarKarbantartas";
        }
        return "zenekar_admin";
    }

    @ModelAttribute("zenekarLista")
    public List<ZenekarDto> getZenekarList(){
       return zenekarService.findAllDto();
    }

    @ModelAttribute("zenekarPeldany")
    public ZenekarDto zenekarDto(){
        return ZenekarDto.builder().build();
    }

    @ModelAttribute("adminCim")
    public String adminCim(){
        return "Hello Admin";
    }

}
