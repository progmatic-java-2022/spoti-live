package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.felhasznalo.FelhasznaloService;
import hu.progmatic.spotilive.felhasznalo.NincsJogosultsagAZenekarhozException;
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
import java.util.Objects;

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
    @Autowired
    private FelhasznaloService felhasznaloService;


    @RolesAllowed(UserType.Roles.ESEMENY_KEZELES_ROLE)
    public EsemenyDto createEsemeny(CreateEsemenyCommand command) {
        Zenekar zenekar = zenekarService.getZenekarEntityById(command.getZenekarId());
//        if (felhasznaloService.isAdmin()) {
            Esemeny ujEsemeny = Esemeny
                    .builder()
                    .nev(command.getNev())
                    .idopont(command.getIdoPont())
                    .zenekar(zenekar)
                    .build();
            zenekar.getEsemenyek().add(ujEsemeny);
            return EsemenyDto.factory(esemenyRepository.save(ujEsemeny));
//        }
//
//        else {
//            Esemeny ujEsemeny = Esemeny
//                    .builder()
//                    .nev(command.getNev())
//                    .idopont(command.getIdoPont())
//                    .zenekar(zenekarService.getZenekarEntityById(felhasznaloService.getZenekarId()))
//                    .build();
//            zenekar.getEsemenyek().add(ujEsemeny);
//            return EsemenyDto.factory(esemenyRepository.save(ujEsemeny));
//
//        }

    }

    public EsemenyDto getEsemenyDtoById(Integer id) {
        Esemeny referenceById = esemenyRepository.getReferenceById(id);
        return EsemenyDto.factory(referenceById);
    }

    public void deleteEsemeny(Integer id) {
        exceptionDobasHaNincsJogosultsagEsemenyhez(id);
        esemenyRepository.deleteById(id);
    }

    private void exceptionDobasHaNincsJogosultsagEsemenyhez(Integer id) {
        if (felhasznaloService.isAdmin()) {
            return;
        }
        Esemeny esemeny = esemenyRepository.getReferenceById(id);
        var felhasznaloZenekarId = felhasznaloService.getZenekarId();
        if (!Objects.equals(esemeny.getZenekar().getId(), felhasznaloZenekarId)) {
            throw new NincsJogosultsagAZenekarhozException(
                    "Zenekar jogosultsággal nem módosítható más eseménye!"
            );
        }
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
        Szavazat zeneEsemeny = Szavazat.builder()
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



    public List<SzavazatTracklistDto> getEsemenyZeneiByLikesAndAbc(Integer esemenyid) {
        return esemenyRepository.getZenekByLikesAndAbc(esemenyid)
                .stream()
                .map(SzavazatTracklistDto::factory)
                .toList();
    }

    public List<EsemenyDto> findAllModosithatoDto() {

        if (felhasznaloService.isAdmin()){
            return findAllEsemeny();
        }
        var zenekarId = felhasznaloService.getZenekarId();

        return esemenyRepository.findAllByZenekarId(zenekarId)
                .stream()
                .map(EsemenyDto::factory)
                .toList();

    }
}