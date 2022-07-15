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

    @GetMapping("/esemeny/zenelista/{esemenyId}")
    public String esemenyTracklistBeltoltese(
            Model model, @PathVariable("esemenyId") Integer esemenyId) {
        model.addAttribute("esemenytracklist", esemenyService.getEsemenyZeneiByLikesAndAbc(esemenyId));
        model.addAttribute("esemenyDto", esemenyService.getEsemenyDtoById(esemenyId));
        return "/esemenytracklist";
    }

    @PostMapping("/esemeny/{esemenyId}/zenelista/{zeneId}")
    public String addSzavazat(
            @PathVariable("zeneId") Integer zeneId,
            @PathVariable("esemenyId") Integer esemenyId,
            Model model) {
        esemenyService.addSzavazat(AddSzavazatCommand.builder()
                .esemenyId(esemenyId)
                .zeneId(zeneId)
                .build());

        return "redirect:/esemeny/zenelista/" + esemenyId;
    }


    @ModelAttribute("esemenytracklist")
    public List<SzavazatTracklistDto> esemenyZenei() {
        return new ArrayList<>();
    }

    @ModelAttribute("esemenyDto")
    public EsemenyDto aktualisEsemeny() {
        return EsemenyDto.builder().build();
    }


}
