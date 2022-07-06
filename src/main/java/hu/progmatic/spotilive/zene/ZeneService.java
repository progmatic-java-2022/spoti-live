package hu.progmatic.spotilive.zene;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ZeneService {
    @Autowired
    ZeneRepository zeneRepository;

    @Autowired
    TagRepository tagRepository;

    public ZeneDto createZene(ZeneDto zeneDto) {
        Zene zene = Zene.builder()
                .eloado(zeneDto.getEloado())
                .hosszMp(zeneDto.getHosszMp())
                .cim(zeneDto.getCim()).build();
        return ZeneDto.factory( zeneRepository.save(zene));
    }

    public void deleteZeneById(Integer id) {
        zeneRepository.deleteById(id);
    }

    public List<ZeneDto> findAllDto() {
        return zeneRepository.findAll()
                .stream()
                .map(ZeneDto::factory)
                .toList();
    }

    public ZeneDto getBycim(String cim) {
        return ZeneDto.factory(zeneRepository.getZeneByCimContainingIgnoreCase(cim));
    }

    public ZeneDto getZeneDtoById(Integer id) {
        return ZeneDto.factory(zeneRepository.getReferenceById(id));
    }

    public ZeneDto editZene(ZeneDto dto) {
        Zene zene = zeneRepository.getReferenceById(dto.getId());
        zene.setCim(dto.getCim());
        zene.setEloado(dto.getEloado());
        zene.setHosszMp(dto.getHosszMp());

        return ZeneDto.factory(zene);
    }

    public int count() {
        return (int) zeneRepository.count();
    }

    public TagDto createTag(TagDto dto) {
       Tag tagEntity = Tag.builder().tagNev(dto.getTagNev()).build();
       return TagDto.factory(tagRepository.save(tagEntity));
    }
    public TagDto getTagById(Integer id){
        return TagDto.factory(tagRepository.getReferenceById(id));
    }

    public void addTag(Integer zeneId, Integer tagId) {
    Zene zene = zeneRepository.getReferenceById(zeneId);
    Tag tag = tagRepository.getReferenceById(tagId);
    TagToZene tagToZeneEntity = TagToZene
            .builder()
            .tag(tag)
            .zene(zene)
            .build();
    zene.getTagToZeneEntityList().add(tagToZeneEntity);
    tag.getTagToZeneEntityList().add(tagToZeneEntity);
    }


    public void editTagById(TagEditCommand command) {
        Tag tagEntity = tagRepository.getReferenceById(command.getTagId());
        tagEntity.setTagNev(command.getTagNev());
    }

    public List<String> listAllTagStringByZeneId(Integer id) {
        Zene zene = zeneRepository.getReferenceById(id);
        return ZeneDto.factory(zene).getTagStringList();
    }

    public List<TagDto> listAllTagDtoByZeneId(Integer zeneId) {
        Zene zene = zeneRepository.getReferenceById(zeneId);
        List<Tag> tagek = zene.getTagToZeneEntityList()
                .stream()
                .map(TagToZene::getTag)
                .toList();
        return tagek.stream().map(TagDto::factory).toList();
    }

    public void deleteTagFromZene(Integer tagId, Integer zeneId) {
        Zene zene = zeneRepository.getReferenceById(zeneId);
        Tag tag = tagRepository.getReferenceById(tagId);
        TagToZene tagToZene = zene.getTagToZeneEntityList()
                .stream()
                .filter(tagToZeneEntity -> tagToZeneEntity.getTag().getId().equals(tagId))
                .findFirst()
                .orElseThrow();
    zene.getTagToZeneEntityList().remove(tagToZene);
    tag.getTagToZeneEntityList().remove(tagToZene);

    }


    public List<TagDto> getAllTag() {
        return tagRepository.findAll().stream().map(TagDto::factory).toList();
    }

    public void deleteTagById(Integer id) {
        tagRepository.deleteById(id);
        //meg nem jo mert a kapcsolat nem torlodik a zenebol
    }
}
