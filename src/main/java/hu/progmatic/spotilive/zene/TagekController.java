package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.zenekar.Zenekar;
import hu.progmatic.spotilive.zenekar.ZenekarDto;
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
        return zeneService.listAllTagDtoByZeneId(zeneId);
    }
    @ModelAttribute("zenekar")
    public ZenekarDto zenkar(Integer zenekarId) {
        return zenekarService.getById(zenekarId);
    }
}
