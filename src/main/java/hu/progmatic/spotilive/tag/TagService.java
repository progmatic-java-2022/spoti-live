package hu.progmatic.spotilive.tag;

import hu.progmatic.spotilive.zene.*;
import hu.progmatic.spotilive.zenekar.AddZenekarExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Transactional
@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    @Autowired
    ZeneRepository zeneRepository;

    public TagDto createTag(TagDto dto) {
        if (tagRepository.getTagByTagNev(dto.getTagNev()).isPresent()) {
            throw new createTagkarExeption("Tag már létezik ilyen névvel!");
        }
        Tag tagEntity = Tag.builder()
                .tagNev(dto.getTagNev())
                .tagKategoria(dto.getTagKategoria())
                .build();
        return TagDto.factory(tagRepository.save(tagEntity));
    }
    public TagDto getTagById(Integer id){
        return TagDto.factory(tagRepository.getReferenceById(id));
    }

    public void editTagById(TagEditCommand command) {
        Tag tagEntity = tagRepository.getReferenceById(command.getTagId());
        tagEntity.setTagNev(command.getTagNev());
        tagEntity.setTagKategoria(command.getTagKategoria());
    }

    public List<TagDto> getAllTag() {
        return tagRepository.findAll().stream().map(TagDto::factory).toList();
    }

    public void deleteTagById(Integer id) {
        tagRepository.deleteById(id);
    }

    public void deleteAlltag() {
        tagRepository.deleteAll();
    }


}
