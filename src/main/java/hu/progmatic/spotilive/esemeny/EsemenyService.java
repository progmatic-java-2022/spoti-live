package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.felhasznalo.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class EsemenyService {

    @Autowired
    private EsemenyRepository esemenyRepository;

    @RolesAllowed(UserType.Roles.ESEMENY_KEZELES_ROLE)
    public EsemenyDto createEsemeny(EsemenyDto esemeny) {
        Esemeny ujEsemeny = Esemeny
                .builder()
                .nev(esemeny.getNev())
                .idopont(esemeny.getIdoPont())
                .build();
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
