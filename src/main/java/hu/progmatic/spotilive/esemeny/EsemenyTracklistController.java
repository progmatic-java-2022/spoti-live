package hu.progmatic.spotilive.esemeny;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    @PostMapping("/zenelista/{zeneId}/esemeny/{esemenyId}")
    public String addSzavazat(@PathVariable ("zeneId") Integer zeneId,
                              @PathVariable ("esemenyId") Integer esemenyId){
        esemenyService.addSzavazat(AddSzavazatCommand.builder()
                .esemenyId(esemenyId)
                .zeneId(zeneId)
                .build());
        return "/zenelista/esemeny/{esemenyid}";
    }


    @ModelAttribute("esemenytracklist")
    public List<ZeneToEsemenyDto> esemenyZenei(){
        return new ArrayList<>();
    }



}
