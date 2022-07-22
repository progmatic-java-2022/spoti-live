package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.felhasznalo.FelhasznaloService;
import hu.progmatic.spotilive.felhasznalo.UserType;
import hu.progmatic.spotilive.tag.TagDto;
import hu.progmatic.spotilive.tag.TagService;
import hu.progmatic.spotilive.zenekar.ZenekarDto;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
public class ZeneController {

    @Autowired
    ZeneService zeneKarbantartasService;
    @Autowired
    ZenekarService zenekarService;

    @Autowired
    TagService tagService;
    @Autowired
    private FelhasznaloService felhasznaloService;


    @GetMapping("/zene")
    public String oldaBetoltes() {
        return "/zenekarbantartas";
    }

    @GetMapping("/tagek/zene/{zeneid}")
    public String modalBetoltes(Model model, @PathVariable Integer zeneid) {
        model.addAttribute("showModal", true);
        model.addAttribute("zeneTagSzerkeztes", tagService.getZeneTagSzerkesztesListaDto(zeneid));
        return "/zenekarbantartas";
    }

    @PostMapping("/zenekarbantartas/zene")
    public String addZene(
            @ModelAttribute("createZeneCommand") @Valid CreateZeneCommand command,
            BindingResult bindingresult,
            Model model
    ) {
        model.addAttribute("zeneError", null);
        if (!bindingresult.hasErrors()) {
            try {
                zeneKarbantartasService.createZene(command);
            } catch (CreateZeneExeption e) {
                model.addAttribute("zeneError", e.getMessage());
                return "/zenekarbantartas";
            }
            return "redirect:/zene";
        }
        return "/zenekarbantartas";
    }

    @PostMapping("/zenekarbantartas/zene/{zeneid}/tag/{tagid}/add")
    public String addTagToZene(
            Model model,
            @PathVariable Integer zeneid,
            @PathVariable Integer tagid) {
        zeneKarbantartasService.addTag(zeneid, tagid);
        return "redirect:/tagek/zene/{zeneid}";
    }

    @PostMapping("/zenekarbantartas/zene/{zeneid}/tag/{tagid}/remove")
    public String removeTagFromZene(
            Model model,
            @PathVariable Integer zeneid,
            @PathVariable Integer tagid
    ) {
        zeneKarbantartasService.deleteTagFromZene(tagid, zeneid);
        return "redirect:/tagek/zene/{zeneid}";
    }

    @PostMapping("/zenekarbantartas/zene/{id}")
    public String editZene(
            Model model,
            @PathVariable("id") Integer id,
            @ModelAttribute("zeneModositas") @Valid ZeneDto dto,
            BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            zeneKarbantartasService.editZene(dto);
            return "redirect:/zene";
        }

        return "/zenekarbantartas";
    }

    @PostMapping("/zenekarbantartas/zene/delete/{id}")
    public String deleteZene(@PathVariable("id") Integer id) {
        zeneKarbantartasService.deleteZeneById(id);
        return "redirect:/zene";
    }


    @GetMapping("/zenekarbantartas/zene/edit/{id}")
    public String editForm(
            Model model,
            @PathVariable Integer id
    ) {
        model.addAttribute("zeneModositas", zeneKarbantartasService.getZeneDtoById(id));
        return "/zenekarbantartas";
    }
    @PostMapping("/zenecontrol/filter")
    public String elkuld(Model model,
                         @ModelAttribute("filterCommand") FilterByTagCommand filter) {
        model.addAttribute("zeneLista",zeneKarbantartasService.getZenekByTagList(filter));
        return "zenekarbantartas";
    }

    @ModelAttribute("zenePeldany")
    public ZeneDto getZenePeldany() {
        return ZeneDto.builder().build();
    }

    @ModelAttribute("createZeneCommand")
    public CreateZeneCommand createZeneCommand() {
        return CreateZeneCommand.builder().build();
    }

    @ModelAttribute("zeneModositas")
    public ZeneDto zeneModositas() {
        return ZeneDto.builder().build();
    }

    @ModelAttribute("zeneLista")
    public List<ZeneDto> getZenek() {
        return zeneKarbantartasService.findAllModosithatoDto();
    }

    @ModelAttribute("ujZeneError")
    public String ujZeneError() {
        return null;
    }

    @ModelAttribute("zeneError")
    public String getZeneError() {
        return null;
    }

    @ModelAttribute("allZenekar")
    public List<ZenekarDto> allZenekar() {
        return zenekarService.findAllDto();
    }

    @ModelAttribute("zeneTagSzerkeztes")
    public ZeneTagSzerkesztesListaDto getZeneTagSzerkeztes() {
        return ZeneTagSzerkesztesListaDto.builder().build();
    }

    @ModelAttribute("showModal")
    public boolean showModal() {
        return false;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        return felhasznaloService.isAdmin();
    }

    @ModelAttribute("adminModositasJogVan")
    public boolean adminModositasJogVan() {
        return felhasznaloService.hasRole(UserType.Roles.USER_WRITE_ROLE);
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