package hu.progmatic.spotilive.tag;

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

@Controller
public class TagKarbantartasController {

    @Autowired
    TagService tagService;

    @GetMapping("/tag")
    public String oldalbetoltes(Model model) {
        model.addAttribute("tagek", tagService.getAllTag());
        return "tagkarbantartas";
    }

    @PostMapping("/tag/add")
    public String addTag(
            Model model,
            @ModelAttribute("tagPeldany") @Valid TagDto tagDto,
            BindingResult bindingResult
    ){
        if (!bindingResult.hasErrors()){
            TagDto ujTag = tagService.createTag(tagDto);
            model.addAttribute("tagPeldany", ujTag);
            return "redirect:/tag";
        }
        return "/tagkarbantartas";
    }

    @PostMapping("/tag/delete/{id}")
    public String deleteTag(
            Model model,
            @PathVariable Integer id
    ){
        tagService.deleteTagById(id);
        return "redirect:/tag";
    }

    @ModelAttribute("tagek")
    public List<TagDto> getTagek() {
        return tagService.getAllTag();
    }

    @ModelAttribute("tagPeldany")
    public TagDto tagPeldany() {
        return TagDto.builder().build();
    }

    @ModelAttribute("tagkategoriak")
    public List<TagKategoria> getKategoriak(){
        return List.of(TagKategoria.values());
    }

}
