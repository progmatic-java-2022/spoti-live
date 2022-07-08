package hu.progmatic.spotilive.tag;

import hu.progmatic.spotilive.tag.TagDto;
import hu.progmatic.spotilive.tag.TagService;
import hu.progmatic.spotilive.zene.ZeneService;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class TagekController {



    @Autowired
    private ZeneService zeneService;
    @Autowired
    private ZenekarService zenekarService;
    @Autowired
    private TagService tagService;

    @GetMapping("/tagek")
    public String oldalBetoltes() {
        return "tagek";
    }

    @ModelAttribute("eredetiTagLista")
    public List<TagDto> eredetiTagLista() {
        return tagService.getAllTag();
    }
    @ModelAttribute("zeneTagLista")
    public List<TagDto> zeneTagLista(Integer zeneId) {
        return zeneService.listAllTagDtoByZeneId(zeneId);
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
