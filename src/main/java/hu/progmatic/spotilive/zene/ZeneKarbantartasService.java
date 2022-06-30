package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.zenekar.Zenekar;
import hu.progmatic.spotilive.zenekar.ZenekarDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ZeneKarbantartasService {
    @Autowired
    ZeneRepository zeneRepository;

    @Autowired
    TagRepository tagRepository;

    public ZeneDto createZene(ZeneDto zeneDto) {
        ZeneEntity zene = ZeneEntity.builder().cim(zeneDto.getCim()).build();
        return ZeneDto.factory( zeneRepository.save(zene));
    }

    public void deleteById(Integer id) {
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

    public ZeneDto getById(Integer id) {
        return ZeneDto.factory(zeneRepository.getReferenceById(id));
    }

    public ZeneDto editZene(ZeneDto dto) {
        ZeneEntity zene = zeneRepository.getReferenceById(dto.getId());
        zene.setCim(dto.getCim());
        zene.setEloado(dto.getEloado());
        zene.setHosszMp(dto.getHosszMp());

        return ZeneDto.factory(zene);
    }

    public int count() {
        return (int) zeneRepository.count();
    }

    public void addTag(TagDto dto) {
        ZeneEntity zene = zeneRepository.getReferenceById(dto.getZeneId());
        var tag = tagRepository.save(
                TagEntity.builder()
                .tagNev(dto.getTagNev())
                .zeneSzam(zene)
                .build());
        zene.getTagek().add(tag);
    }

    public void deleteTagById(Integer tagId) {
        TagEntity tagEntity = tagRepository.getReferenceById(tagId);
        ZeneEntity zeneEntity = tagEntity.getZeneSzam();
        zeneEntity.getTagek().remove(tagEntity);
        tagEntity.setZeneSzam(null);
    }

    public void editTagById(TagEditCommand command) {
        TagEntity tagEntity = tagRepository.getReferenceById(command.getTagId());
        tagEntity.setTagNev(command.getTagNev());
    }

    public List<TagDto> listAllTagByZeneId(Integer id) {
        ZeneEntity zene = zeneRepository.getReferenceById(id);

        return zene.getTagek().stream().map(TagDto::factory).toList();
    }
}
