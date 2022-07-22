package hu.progmatic.spotilive.tag;

import hu.progmatic.spotilive.zene.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    @Autowired
    ZeneRepository zeneRepository;

    public TagDto createTag(TagDto dto) {
        if (tagRepository.getTagByTagNev(dto.getTagNev()).isPresent()) {
            throw new CreateTagExeption("Tag már létezik ilyen névvel!");
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
        if(tagRepository.getTagByTagNev(command.getTagNev()).isPresent() &&
                !command.getTagId().equals(tagRepository.getTagByTagNevIgnoreCase(command.getTagNev()).getId())){
            throw new CreateTagExeption("Tag már létezik ilyen névvel!");
        }
        Tag tagEntity = tagRepository.getReferenceById(command.getTagId());
        tagEntity.setTagNev(command.getTagNev());
        tagEntity.setTagKategoria(command.getTagKategoria());
    }

    public List<TagDto> getAllTag() {
        return tagRepository.findAll().stream().map(TagDto::factory).toList();
    }

    public void deleteTagById(Integer id) {
        if(tagRepository.getReferenceById(id).getTagToZeneEntityList().size() != 0){
                    List<String> liszt = tagRepository.getReferenceById(id).getTagToZeneEntityList()
                            .stream()
                            .map(TagToZene::getZene)
                            .map(Zene::getCim)
                            .toList();
                    StringBuilder zenek = new StringBuilder();
                    boolean isElso = true;
                    for (String zene : liszt){
                        if (isElso){
                            zenek.append(zene);
                            isElso = false;
                        }else {
                        zenek.append(", ").append(zene);
                        }
                    }

            throw new TagTorlesException("A tag nem törölhető, mert a következő zenékhez hozzá van rendelve: " + zenek + "!");
        }
        tagRepository.deleteById(id);
    }




    public ZeneTagSzerkesztesListaDto getZeneTagSzerkesztesListaDto(Integer zeneId) {
        Zene zene = zeneRepository.getReferenceById(zeneId);
        List<Tag> tagek = tagRepository.findAll();
        return ZeneTagSzerkesztesListaDto.factory(zene, tagek);
    }

    public TagDto getTagDtoByNev(String nev) {
        return TagDto.factory(tagRepository.getTagByTagNev(nev).orElseThrow());
    }


    public boolean isTagExistsById(Integer tagId) {
        return tagRepository.existsById(tagId);
    }

    public Set<String> getAllTagTipus() {
        return tagRepository.findAll()
                .stream()
                .map(tag -> tag.getTagKategoria().getTagKategoriaString())
                .collect(Collectors.toSet());
    }
}
