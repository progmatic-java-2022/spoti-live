package hu.progmatic.spotilive.esemeny;


import hu.progmatic.spotilive.felhasznalo.FelhasznaloService;
import hu.progmatic.spotilive.felhasznalo.KreditException;
import hu.progmatic.spotilive.felhasznalo.UserType;
import hu.progmatic.spotilive.tag.TagDto;
import hu.progmatic.spotilive.tag.TagService;
import hu.progmatic.spotilive.zene.FilterByTagCommand;
import hu.progmatic.spotilive.zene.ZeneDto;
import hu.progmatic.spotilive.zene.ZeneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class EsemenyTracklistController {

    @Autowired
    EsemenyService esemenyService;
    @Autowired
    private SzavazatService szavazatService;
    @Autowired
    private FelhasznaloService felhasznaloService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ZeneService zeneService;


    @GetMapping("/esemeny/zenelista/{esemenyId}")
    public String esemenyTracklistBeltoltese(
            Model model, @PathVariable("esemenyId") Integer esemenyId,
            @RequestParam("tagLista") Optional<List<String >> filter) {
        esemenyTrakclistModelEpites(model, esemenyId, filter);

        return "/esemenytracklist";
    }

    private void esemenyTrakclistModelEpites(Model model, Integer esemenyId, Optional<List<String>> filter) {
        FilterByTagCommand filterByTagCommand = FilterByTagCommand.builder()
            .esemenyId(esemenyId)
            .tagLista(filter.orElse(List.of()))
            .build();

        List<SzavazatTracklistDto> esemenyTrackList = szavazatService.getEsemenyTrackList(filterByTagCommand);
        model.addAttribute("kreditek",esemenyService.getKreditekSzama());
        model.addAttribute("kreditekSzamaInt",esemenyService.getKreditSzam());
        model.addAttribute("esemenytracklist", esemenyTrackList);
        model.addAttribute("esemenyDto", esemenyService.getEsemenyDtoById(esemenyId));
        model.addAttribute("filterCommand", filterByTagCommand);
        model.addAttribute("vanElegKredit", vanElegKredit());
    }

    @PostMapping("/esemeny/{esemenyId}/zenelista/{zeneId}")
    public String addSzavazat(
            @PathVariable("zeneId") Integer zeneId,
            @PathVariable("esemenyId") Integer esemenyId,
            @RequestParam("tagLista") Optional<List<String >> filter,
            RedirectAttributes attr,
            Model model) {
        try {
            esemenyService.addSzavazat(SzavazatCommand.builder()
                    .esemenyId(esemenyId)
                    .zeneId(zeneId)
                    .build());

        }
        catch (KreditException e) {
            attr.addAttribute("kreditError",e);
        }
        attr.addAttribute("tagLista",filter.orElse(List.of()));
        return "redirect:/esemeny/zenelista/" + esemenyId;
    }

    @PostMapping("/esemeny/delete/{esemenyId}/zenelista/{zeneId}")
    public String deleteSzavazat(
            @PathVariable("zeneId") Integer zeneId,
            @PathVariable("esemenyId") Integer esemenyId,
            @RequestParam("tagLista") Optional<List<String >> filter,
            RedirectAttributes attr,
            Model model) {

            esemenyService.deleteSzavazat(SzavazatCommand.builder()
                    .esemenyId(esemenyId)
                    .zeneId(zeneId)
                    .build());

        attr.addAttribute("tagLista",filter.orElse(List.of()));
        return "redirect:/esemeny/zenelista/" + esemenyId;
    }

    @PostMapping("/tracklist/filter")
    public String elkuld(Model model,
                         @ModelAttribute("filterCommand") FilterByTagCommand filter) {
        model.addAttribute("esemenytracklist",zeneService.getZenekByTagList(filter));
        return "/esemenytracklist";
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
 @ModelAttribute("kreditekSzamaInt")
    public Integer getKreditSzam() {
        return esemenyService.getKreditSzam();}

    @ModelAttribute("kreditError")
    public String getKreditError() {
        return "Nincs elég kredit! Ha akarod visszavonhatod a szavazatod.";
    }

    @ModelAttribute("vanElegKredit")
    public boolean vanElegKredit(){
        return esemenyService.vanElegKredit();
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(){
        return felhasznaloService.isAdmin();
    }

    @ModelAttribute("isZenekar")
    public boolean isZenekar(){
        return felhasznaloService.isZenekar();
    }
    @ModelAttribute("isOnlyGuest")
    public boolean isOnlyGuest() {
        return felhasznaloService.isGuest();
    }
    @ModelAttribute("osszesTag")
    public List<TagDto> osszesTag() {
        return tagService.getAllTag();
    }

    @ModelAttribute("tagTipusok")
    public Set<String> tagTipusok() {
        return tagService.getAllTagTipus();
    }

    @ModelAttribute("filterCommand")
    FilterByTagCommand filterByTagCommand() {
        return FilterByTagCommand.builder().build();
    }
}
