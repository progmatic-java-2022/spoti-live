package hu.progmatic.spotilive.felhasznalo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class UjVendegController {

    @Autowired
    FelhasznaloService felhasznaloService;

    @GetMapping("/felhasznalo/vendeg")
    public String vendeg() {
        return "ujvendegletrehozasa";
    }

    @PostMapping("/felhasznalo/vendeg")
    public String add(@ModelAttribute UjVendegCommand command, Model model) {
        model.addAttribute("ujVendegError", null);
        try {
            felhasznaloService.addGuest(command);
        } catch (FelhasznaloLetrehozasException e) {
            model.addAttribute("ujVendegError", e.getMessage());
            return "ujvendegletrehozasa";
        }
        return "ujvendegletrehozasa";
    }

    @ModelAttribute("ujFelhasznaloCommand")
    public UjVendegCommand ujVendegCommand() {
        return new UjVendegCommand();
    }


    @ModelAttribute("ujVendegError")
    public String ujVendegError() {
        return null;
    }




}
