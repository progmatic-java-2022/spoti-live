package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.felhasznalo.FelhasznaloService;
import hu.progmatic.spotilive.felhasznalo.NincsJogosultsagAZenekarhozException;
import hu.progmatic.spotilive.tag.Tag;
import hu.progmatic.spotilive.tag.TagDto;
import hu.progmatic.spotilive.tag.TagRepository;
import hu.progmatic.spotilive.zenekar.Zenekar;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class ZeneService {
    @Autowired
    ZeneRepository zeneRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    ZenekarService zenekarService;


    @Autowired
    private FelhasznaloService felhasznaloService;

    public ZeneDto createZene(CreateZeneCommand command) {
        if (zeneRepository.getZeneByCim(command.getCim()).isPresent()) {
            throw new CreateZeneExeption("Zene már létezik ilyen címmel");
        }
        Integer zenekarId;
        if (felhasznaloService.isAdmin()) {
            zenekarId = command.getZenekarId();
        } else {
            zenekarId = felhasznaloService.getZenekarId();
        }

        Zenekar zenekar = zenekarService.getZenekarEntityById(zenekarId);
        Zene ujZene = Zene.builder()
                .cim(command.getCim())
                .eloado(command.getEloado())
                .hosszMp(command.getHosszMp())
                .zenekar(zenekar)
                .build();
        return ZeneDto.factory(zeneRepository.save(ujZene));
    }

    public void deleteZeneById(Integer id) {
        exceptionDobasHaNincsJogosultsagAZenehez(id);
        var zeneTagek = zeneRepository.getReferenceById(id)
                .getTagToZeneEntityList()
                .stream()
                .map(tagToZene -> tagToZene.getTag().getId())
                .toList();
        for (var tag : zeneTagek) {
            deleteTagFromZene(tag, id);
        }
        zeneRepository.deleteById(id);
    }

    private void exceptionDobasHaNincsJogosultsagAZenehez(Integer id) {
        if (felhasznaloService.isAdmin()) {
            return;
        }
        var zene = zeneRepository.getReferenceById(id);
        var felhasznaloZenekarId = felhasznaloService.getZenekarId();
        if (!Objects.equals(zene.getZenekar().getId(), felhasznaloZenekarId)) {
            throw new NincsJogosultsagAZenekarhozException(
                    "Zenekar jogosultsággal nem módosítható más zenéje!"
            );
        }
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
        exceptionDobasHaNincsJogosultsagAZenehez(dto.getId());
        Zene zene = zeneRepository.getReferenceById(dto.getId());
        zene.setCim(dto.getCim());
        zene.setEloado(dto.getEloado());
        zene.setHosszMp(dto.getHosszMp());

        return ZeneDto.factory(zene);
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

    public ZeneDto getZeneByNev(String nev) {
        return ZeneDto.factory(zeneRepository.getZeneByCim(nev).orElseThrow());
    }

    public List<ZeneDto> findAllModosithatoDto() {
        if (felhasznaloService.isAdmin()) {
            return findAllDto()
                    .stream()
                    .sorted(Comparator.comparing(ZeneDto::getZenekarNev))
                    .toList();
        }
        Integer zenekarId = felhasznaloService.getZenekarId();
        List<Zene> allByZenekarId = zeneRepository
            .findAllByZenekarId(zenekarId);
        return allByZenekarId
                .stream()
                .map(ZeneDto::factory)
                .sorted(Comparator.comparing(ZeneDto::getCim))
                .toList();
    }

    public List<ZeneDto> getZenekByTagList(FilterByTagCommand command) {
        var zeneLista = zeneRepository.findAll();
        var tagLista = command.tagLista;
        return zeneLista
                .stream()
                .filter(zene -> zene.hasCheckedTags(tagLista))
                .map(ZeneDto::factory)
                .toList();


    }
}
