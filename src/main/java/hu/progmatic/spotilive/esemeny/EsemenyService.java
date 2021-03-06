package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.felhasznalo.*;
import hu.progmatic.spotilive.zene.Zene;
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
    @Autowired
    private SzavazatRepository szavazatRepository;


    @RolesAllowed(UserType.Roles.ESEMENY_KEZELES_ROLE)
    public EsemenyDto createEsemeny(CreateEsemenyCommand command) {
        Integer zenekarId;
        if (felhasznaloService.isAdmin()) {
            zenekarId = command.getZenekarId();
        } else {
            zenekarId = felhasznaloService.getZenekarId();
        }
        Zenekar zenekar = zenekarService.getZenekarEntityById(zenekarId);
        Esemeny ujEsemeny = Esemeny
                .builder()
                .nev(command.getNev())
                .idopont(command.getIdoPont())
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
                    "Zenekar jogosults??ggal nem m??dos??that?? m??s esem??nye!"
            );
        }
    }


    public List<EsemenyDto> findAllEsemeny() {
        return esemenyRepository.findAll().stream().map(EsemenyDto::factory).toList();
    }


    public void update(EsemenyDto modositott, Integer id) {
        exceptionDobasHaNincsJogosultsagEsemenyhez(id);
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

    public void addSzavazat(SzavazatCommand command) {
        if (vanElegKredit()) {
            Szavazat szavazat = getOrCreateSajatSzavazat(command);
            szavazat.setSzavazat(szavazat.getSzavazat() + 1);
            if (felhasznaloService.isGuest()) {
                kreditKezeles(true);
            }
        } else {
            throw new KreditException("Nincs el??g kredit! Vonj vissza egy szavazatot, de ink??bb V??S??ROLJ!");
        }

    }

    private void kreditKezeles(boolean addSzavazat) {
        var felhasznaloId = felhasznaloService.getFelhasznaloId();
        var felhasznalo = felhasznaloService.getById(felhasznaloId);
        var kredit = felhasznalo.getKredit();
        if (addSzavazat) {
            kredit.setKreditMennyiseg(kredit.getKreditMennyiseg() - 1);
        } else {
            kredit.setKreditMennyiseg(kredit.getKreditMennyiseg() + 1);
        }
    }


    public boolean vanElegKredit() {
        if (felhasznaloService.isGuest()) {

            var felhasznaloId = felhasznaloService.getFelhasznaloId();
            var felhasznalo = felhasznaloService.getById(felhasznaloId);
            var kredit = felhasznalo.getKredit();
            return kredit.getKreditMennyiseg() > 0;
        } else {
            return true;
        }
    }

    public void deleteSzavazat(SzavazatCommand command) {
        var felhasznaloId = felhasznaloService.getFelhasznaloId();
        var felhasznalo = felhasznaloService.getById(felhasznaloId);
        var esemeny = esemenyRepository.getReferenceById(command.getEsemenyId());
        var zene = zeneService.getZeneById(command.getZeneId());
        Szavazat szavazat = felhasznalo.getSzavazatok()
                .stream()
                .filter(
                        keresettSzavazat ->
                                keresettSzavazat.getEsemeny().getId().equals(esemeny.getId())
                                        && keresettSzavazat.getZene().getId().equals(zene.getId())
                )
                .findFirst()
                .orElseThrow();
        if (szavazat.getSzavazat() == 1) {
            zene.getSzavazatok().remove(szavazat);
            felhasznalo.getSzavazatok().remove(szavazat);
            szavazatRepository.delete(szavazat);
            if (felhasznaloService.isGuest()) {
                kreditKezeles(false);
            }
        } else {
            szavazat.setSzavazat(szavazat.getSzavazat() - 1);
            if (felhasznaloService.isGuest()) {
                kreditKezeles(false);
            }
        }
    }


    private Szavazat getOrCreateSajatSzavazat(SzavazatCommand command) {
        var esemeny = esemenyRepository.getReferenceById(command.getEsemenyId());
        var zene = zeneService.getZeneById(command.getZeneId());
        var felhasznaloId = felhasznaloService.getFelhasznaloId();
        var felhasznalo = felhasznaloService.getById(felhasznaloId);
        return felhasznalo.getSzavazatok()
                .stream()
                .filter(
                        keresettSzavazat ->
                                keresettSzavazat.getEsemeny().getId().equals(esemeny.getId())
                                        && keresettSzavazat.getZene().getId().equals(zene.getId())
                )
                .findAny()
                .orElseGet(() -> ujSzavazat(esemeny, zene, felhasznalo));
    }

    private Szavazat ujSzavazat(Esemeny esemeny, Zene zene, Felhasznalo felhasznalo) {
        Szavazat ujSzavazat = Szavazat.builder()
                .esemeny(esemeny)
                .zene(zene)
                .felhasznalo(felhasznalo)
                .szavazat(0)
                .build();
        esemeny.getZenek().add(ujSzavazat);
        zene.getSzavazatok().add(ujSzavazat);
        return ujSzavazat;
    }


    public List<EsemenyDto> findAllModosithatoDto() {

        if (felhasznaloService.isAdmin()) {
            return findAllEsemeny();
        }
        var zenekarId = felhasznaloService.getZenekarId();

        return esemenyRepository.findAllByZenekarId(zenekarId)
                .stream()
                .map(EsemenyDto::factory)
                .toList();
    }

    public String getKreditekSzama() {
        if (felhasznaloService.isGuest()) {
            var felhasznaloId = felhasznaloService.getFelhasznaloId();
            var felhasznalo = felhasznaloService.getById(felhasznaloId);
            Integer kreditek = felhasznalo.getKredit().getKreditMennyiseg();
             return kreditek + "";
        }
        return "Csak guestnek kell kredit!";
    }
}