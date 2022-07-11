package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.tag.Tag;
import hu.progmatic.spotilive.tag.TagDto;
import hu.progmatic.spotilive.tag.TagRepository;
import hu.progmatic.spotilive.zenekar.Zenekar;
import hu.progmatic.spotilive.zenekar.ZenekarService;
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

    @Autowired
    ZenekarService zenekarService;

    public ZeneDto createZene(CreateZeneCommand command) {
        if (zeneRepository.getZeneByCim(command.getCim()).isPresent()) {
            throw new CreateZeneExeption("Zene már létezik ilyen címmel");
        }
        Zenekar zenekar = zenekarService.getZenekarEntityById(command.getZenekarId());
        Zene ujZene = Zene.builder()
                .cim(command.getCim())
                .eloado(command.getEloado())
                .hosszMp(command.getHosszMp())
                .zenekar(zenekar)
                .build();
        return ZeneDto.factory(zeneRepository.save(ujZene));
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
        return ZeneDto.factory(zeneRepository.getZeneByCim(cim).orElseThrow());
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

    public Zene getZeneById(Integer zeneId) {
        return zeneRepository.getReferenceById(zeneId);
    }
}
