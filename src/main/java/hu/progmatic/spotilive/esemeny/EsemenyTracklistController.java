package hu.progmatic.spotilive.esemeny;


import hu.progmatic.spotilive.felhasznalo.FelhasznaloService;
import hu.progmatic.spotilive.felhasznalo.KreditException;
import hu.progmatic.spotilive.felhasznalo.UserType;
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
    @Autowired
    private SzavazatService szavazatService;
    @Autowired
    private FelhasznaloService felhasznaloService;


    @GetMapping("/esemeny/zenelista/{esemenyId}")
    public String esemenyTracklistBeltoltese(
            Model model, @PathVariable("esemenyId") Integer esemenyId) {
        model.addAttribute("esemenytracklist", szavazatService.getEsemenyTrackList(esemenyId));
        model.addAttribute("esemenyDto", esemenyService.getEsemenyDtoById(esemenyId));
        return "/esemenytracklist";
    }

    @PostMapping("/esemeny/{esemenyId}/zenelista/{zeneId}")
    public String addSzavazat(
            @PathVariable("zeneId") Integer zeneId,
            @PathVariable("esemenyId") Integer esemenyId,
            Model model) {
        try {
            esemenyService.addSzavazat(SzavazatCommand.builder()
                    .esemenyId(esemenyId)
                    .zeneId(zeneId)
                    .build());
            return "redirect:/esemeny/zenelista/" + esemenyId;
        }
        catch (KreditException e) {
            model.addAttribute("kreditError", e.getMessage());
            model.addAttribute("esemenytracklist", szavazatService.getEsemenyTrackList(esemenyId));
            return "/esemenytracklist";
        }
    }

    @PostMapping("/esemeny/delete/{esemenyId}/zenelista/{zeneId}")
    public String deleteSzavazat(
            @PathVariable("zeneId") Integer zeneId,
            @PathVariable("esemenyId") Integer esemenyId,
            Model model) {
        esemenyService.deleteSzavazat(SzavazatCommand.builder()
                .esemenyId(esemenyId)
                .zeneId(zeneId)
                .build());
        return "redirect:/esemeny/zenelista/" + esemenyId;
    }


    @ModelAttribute("esemenytracklist")
    public List<SzavazatTracklistDto> esemenyZenei() {
        return new ArrayList<>();
    }

    @ModelAttribute("esemenyModositasJogVan")
    public boolean esemenyModositasJogVan(){
        return felhasznaloService.hasRole(UserType.Roles.ESEMENY_KEZELES_ROLE);
    }

    @ModelAttribute("esemenyDto")
    public EsemenyDto aktualisEsemeny() {
        return EsemenyDto.builder().build();
    }

    @ModelAttribute("kreditek")
    public String getKreditek() {
        return esemenyService.getKreditekSzama();}

    @ModelAttribute("kreditError")
    public String getZeneError() {
        return null;
    }
}
