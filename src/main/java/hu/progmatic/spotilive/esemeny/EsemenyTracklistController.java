package hu.progmatic.spotilive.esemeny;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EsemenyTracklistController {

    @Autowired
    EsemenyService esemenyService;

    @GetMapping("/zenelista/esemeny/{esemenyid}")
    public String esemenyTracklistBeltoltese(
            Model model, @PathVariable("esemenyid") Integer esemenyid) {
       model.addAttribute("esemenytracklist", esemenyService.getEsemenyZenei(esemenyid));
        return "/esemenytracklist";
    }

    @ModelAttribute("esemenytracklist")
    public List<ZeneToEsemenyDto> esemenyZenei(){
        return new ArrayList<>();
    }


}
