package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.felhasznalo.UserType;
import hu.progmatic.spotilive.zenekar.Zenekar;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Service
@Transactional
@Slf4j
public class EsemenyService {

    @Autowired
    private EsemenyRepository esemenyRepository;

    @Autowired
    private ZenekarService zenekarService;

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

    public EsemenyDto getById(Integer id) {
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


}
