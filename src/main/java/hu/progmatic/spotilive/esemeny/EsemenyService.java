package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.felhasznalo.UserType;
import hu.progmatic.spotilive.zene.ZeneService;
import hu.progmatic.spotilive.zenekar.Zenekar;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class
EsemenyService {

    @Autowired
    private EsemenyRepository esemenyRepository;

    @Autowired
    private ZenekarService zenekarService;
    @Autowired
    private ZeneService zeneService;


    @RolesAllowed(UserType.Roles.ESEMENY_KEZELES_ROLE)
    public EsemenyDto createEsemeny(CreateEsemenyCommand esemeny) {
        Zenekar zenekar = zenekarService.getZenekarEntityById(esemeny.getZenekarId());
        Esemeny ujEsemeny = Esemeny
                .builder()
                .nev(esemeny.getNev())
                .idopont(esemeny.getIdoPont())
                .zenekar(zenekar)
                .build();
        zenekar.getEsemenyek().add(ujEsemeny);
        return EsemenyDto.factory(esemenyRepository.save(ujEsemeny));
    }

    public EsemenyDto getEsemenyDtoById(Integer id) {
        Esemeny referenceById = esemenyRepository.getReferenceById(id);
        return EsemenyDto.factory(referenceById);
    }

    public void deleteEsemeny(Integer id) {
        esemenyRepository.deleteById(id);
    }

    public int countAllEsemeny() {
        return (int) esemenyRepository.count();
    }

    public List<EsemenyDto> findAllEsemeny() {
        return esemenyRepository.findAll().stream().map(EsemenyDto::factory).toList();
    }


    public void udpate(EsemenyDto modositott, Integer id) {
        var modositando = esemenyRepository.getReferenceById(id);
        modositando.setNev(modositott.getNev());
        modositando.setIdopont(modositott.getIdoPont());

    }


    public EsemenyDto getByName(String esemenyNev) {
        return EsemenyDto.factory(esemenyRepository.findEsemenyByNevContainingIgnoreCase(esemenyNev));
    }

    public Esemeny getEsemenyById(Integer id) {
        return esemenyRepository.getReferenceById(id);
    }

    public void addZenetoEsemenyByZeneId(AddZeneToEsemenyCommand command) {
        var esemeny = esemenyRepository.getReferenceById(command.getEsemenyId());
        var zene = zeneService.getZeneById(command.getZeneId());
        ZeneToEsemeny zeneEsemeny = ZeneToEsemeny.builder()
                .zene(zene)
                .esemeny(esemeny)
                .build();
        esemeny.getZenek().add(zeneEsemeny);
        zene.getEsemenyek().add(zeneEsemeny);

    }

    public void addSzavazat(AddSzavazatCommand command) {
    var esemeny = esemenyRepository.getReferenceById(command.getEsemenyId());
    var modositando = esemeny.getZenek()
            .stream()
            .filter(zene -> zene.getZene().getId().equals(command.getZeneId()))
            .findFirst()
            .orElseThrow();
    modositando.setSzavazat(modositando.getSzavazat() + 1);
    }


    public List<ZeneToEsemenyDto> getEsemenyZenei(Integer esemenyid) {
        var esemeny = esemenyRepository.getReferenceById(esemenyid);
        return esemeny.getZenek().stream().map(ZeneToEsemenyDto::factory).toList();
    }

    public List<ZeneToEsemenyDto> getEsemenyZeneiByLikesAndAbc(Integer esemenyid) {
        var esemeny = esemenyRepository.getReferenceById(esemenyid);
        return esemenyRepository.getZenekByLikesAndAbc(esemenyid)
                .stream()
                .map(ZeneToEsemenyDto::factory)
                .toList();
    }
}