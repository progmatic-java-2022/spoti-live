package hu.progmatic.spotilive.zenekar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class ZenekarAdminController {


    @Autowired
    private ZenekarService zenekarService;

    @GetMapping("/admin")
    public String oldalbetoltes(){
        return "zenekar_admin";
    }

    @ModelAttribute("zenekarLista")
    public List<ZenekarDto> getZenekarList(){
       return zenekarService.findAllDto();
    }



}
