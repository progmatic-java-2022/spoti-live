package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.esemeny.EsemenyDto;
import hu.progmatic.spotilive.zenekar.Zenekar;
import hu.progmatic.spotilive.zenekar.ZenekarDto;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TagekController {



    @Autowired
    private ZeneService zeneService;
    @Autowired
    private ZenekarService zenekarService;

    @GetMapping("/tagek")
    public String oldalBetoltes() {
        return "tagek";
    }

    @ModelAttribute("eredetiTagLista")
    public List<TagDto> eredetiTagLista() {
        return zeneService.getAllTag();
    }
    @ModelAttribute("zeneTagLista")
    public List<TagDto> zeneTagLista(Integer zeneId) {
        return zeneService.findAllTagDto();
        //return zeneService.listAllTagDtoByZeneId(zeneId);
    }
    @ModelAttribute("zeneTag")
    public TagDto zeneTag() {
        return TagDto.builder().build();
    }
//    @ModelAttribute("zenekar")
//    public ZenekarDto zenekar(Integer zenekarId) {
//        return zenekarService.getById(zenekarId);
//    }
}
