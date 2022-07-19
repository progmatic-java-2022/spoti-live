package hu.progmatic.spotilive.felhasznalo;
import hu.progmatic.spotilive.esemeny.*;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class GuestController {
    @Autowired
    EsemenyService esemenyService;
    @Autowired
    private ZenekarService zenekarService;


    @GetMapping("/guestind")
    public String esemenyekGuestOldalBetoltes(Model model) {
        return "/guestindex";
    }


    @ModelAttribute("esemenyekLista")
    public List<EsemenyDto> getAllEsemenyLista() {
        return esemenyService.findAllEsemeny();
    }
}
