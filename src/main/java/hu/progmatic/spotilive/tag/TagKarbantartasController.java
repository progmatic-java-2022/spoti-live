package hu.progmatic.spotilive.tag;

import hu.progmatic.spotilive.esemeny.EsemenyDto;
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
    ) {
        if (!bindingResult.hasErrors()) {
            try {
                TagDto ujTag = tagService.createTag(tagDto);
                model.addAttribute("tagPeldany", ujTag);
            } catch (createTagkarExeption e){
                model.addAttribute("createTagError", e.getMessage());
                return "/tagkarbantartas";
            }
            return "redirect:/tag";
        }
        return "/tagkarbantartas";
    }

    @PostMapping("/tag/delete/{id}")
    public String deleteTag(
            Model model,
            @PathVariable Integer id
    ) {
        try {
            tagService.deleteTagById(id);
        } catch (TagTorlesException e){
            model.addAttribute("deletetagerror", e.getMessage());
            return "/tagkarbantartas";
        }
            return "redirect:/tag";

    }

    @GetMapping ("/tag/{id}")
    public String tagModositas(Model model, @PathVariable("id") Integer id){
        model.addAttribute("tagPeldany", tagService.getTagById(id));
        return "/tagkarbantartas";
    }

    @PostMapping("tag/{id}")
    public String modositasMentese(
            @PathVariable ("id") Integer id,
            @ModelAttribute("tageditcommand") @Valid TagEditCommand tagEditCommand,
            BindingResult bindingResult
    ) {
        if (!bindingResult.hasErrors()) {
            tagService.editTagById(tagEditCommand);
            return "redirect:/tag";
        }
        return "/tagkarbantartas";
    }

    @ModelAttribute("tagek")
    public List<TagDto> getTagek() {
        return tagService.getAllTag();
    }

    @ModelAttribute("tagPeldany")
    public TagDto tagPeldany() {
        return TagDto.builder().build();
    }

    @ModelAttribute("tageditcommand")
    public TagEditCommand commandPeldany(TagDto tagDto){
        return TagEditCommand.builder().tagId(tagDto.getId()).tagNev(tagDto.getTagNev()).tagKategoria(tagDto.getTagKategoria()).build();
    }
    @ModelAttribute("tagkategoriak")
    public List<TagKategoria> getKategoriak() {
        return List.of(TagKategoria.values());
    }

    @ModelAttribute("createTagError")
    public String ujTagHiba() {
        return null;
    }

    @ModelAttribute("deletetagerror")
    public String torlesHiba(){
        return null;
    }


}
