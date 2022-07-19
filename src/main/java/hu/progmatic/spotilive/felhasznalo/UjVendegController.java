package hu.progmatic.spotilive.felhasznalo;

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
import java.util.Arrays;
import java.util.List;

@Controller
public class UjVendegController {

    @Autowired
    MeghivoService meghivoService;

    @GetMapping("/public/meghivo")
    public String vendeg() {

        return "ujvendegletrehozasa";
    }

    @PostMapping("/public/meghivo")
    public String add(
            @ModelAttribute ("meghivofelhasznalasacommand") @Valid MeghivoFelhasznalasaCommand command,
            BindingResult bindingResult,
            Model model) {
       if (!bindingResult.hasErrors()) {
           try {
               meghivoService.meghivoFelhasznalasa(command);
               return "redirect:/login";
           } catch (FelhasznaloLetrehozasException e) {
               bindingResult.addError(
                       new FieldError(
                               "meghivofelhasznalasacommand",
                               e.getMezoNev(),
                               e.getMessage()
                       ));
               return "ujvendegletrehozasa";
           }
       }
        return "ujvendegletrehozasa";
    }

    @ModelAttribute("meghivofelhasznalasacommand")
    public MeghivoFelhasznalasaCommand meghivoFelhasznalasaCommand() {
        return MeghivoFelhasznalasaCommand.builder().build();
    }
}
