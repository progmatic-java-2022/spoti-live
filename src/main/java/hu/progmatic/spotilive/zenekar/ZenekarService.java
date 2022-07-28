package hu.progmatic.spotilive.zenekar;

import hu.progmatic.spotilive.felhasznalo.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ZenekarService {

    @Autowired
    private ZenekarRepository zenekarRepository;

    @RolesAllowed(UserType.Roles.ZENEKAR_KEZELES_ROLE)
    public ZenekarDto createZenekar(ZenekarDto zenekarDto) {
        if (zenekarDto.getTelefonszam() != null && zenekarDto.getTelefonszam().equals("")) {
            zenekarDto.setTelefonszam(null);
        }
        if (isNevHasznalt(zenekarDto)) {
            throw new AddZenekarExeption("Zenekar már létezik ilyen névvel!");
        }
        if (isEmailHasznalt(zenekarDto)) {
            throw new AddZenekarExeption("Zenekar már létezik ilyen email címmel!");
        }
        if (isTelefonszamHasznalt(zenekarDto)) {
            throw new AddZenekarExeption("Zenekar már létezik ilyen telefonszámmal'");
        }
        Zenekar zenekar = Zenekar.builder()
                .nev(zenekarDto.getNev())
                .email(zenekarDto.getEmail())
                .telefonszam(zenekarDto.getTelefonszam())
                .leiras(zenekarDto.getLeiras())
                .varos(zenekarDto.getVaros())
                .build();
        return ZenekarDto.factory(zenekarRepository.save(zenekar));
    }

    private boolean isNevHasznalt(ZenekarDto zenekarDto) {
        return nevHasznalt(zenekarRepository.findByNev(zenekarDto.getNev()));
    }

    private boolean nevHasznalt(Optional<Zenekar> zenekarRepository) {
        return zenekarRepository.isPresent();
    }

    private boolean isEmailHasznalt(ZenekarDto zenekarDto) {
        return zenekarRepository.findByEmail(zenekarDto.getEmail()).isPresent();
    }

    private boolean isTelefonszamHasznalt(ZenekarDto zenekarDto) {
        return zenekarDto.getTelefonszam() != null
                && !zenekarDto.getTelefonszam().equals("")
                && zenekarRepository.findByTelefonszam(zenekarDto.getTelefonszam()).isPresent();
    }

    public void deleteById(Integer id) {
        zenekarRepository.deleteById(id);
    }

    public List<ZenekarDto> findAllDto() {
        return zenekarRepository.findAll()
                .stream()
                .map(ZenekarDto::factory)
                .toList();
    }

    public ZenekarDto getByName(String nev) {
        return ZenekarDto.factory(zenekarRepository.getZenekarByNev(nev));
    }

    public ZenekarDto getById(Integer id) {
        return ZenekarDto.factory(zenekarRepository.getReferenceById(id));
    }

    public ZenekarDto editZenekar(ZenekarDto dto) {
        if (dto.getTelefonszam() != null && dto.getTelefonszam().equals("")) {
            dto.setTelefonszam(null);
        }
        if (isNevHasznalt(dto) &&
                !zenekarRepository.getZenekarByNev(dto.getNev()).getId().equals(dto.getId())) {
            throw new AddZenekarExeption("Zenekar már létezik ilyen névvel!");
        }
        if (isEmailHasznalt(dto) &&
                !zenekarRepository.getZenekarByEmail(dto.getEmail()).getId().equals(dto.getId())) {
            throw new AddZenekarExeption("Zenekar már létezik ilyen email címmel!");
        }
        if (isTelefonszamHasznalt(dto) &&
                !zenekarRepository.getZenekarByTelefonszam(dto.getTelefonszam()).getId().equals(dto.getId())) {
            throw new AddZenekarExeption("Zenekar már létezik ilyen telefonszámmal'");
        }
        Zenekar zenekar = zenekarRepository.getReferenceById(dto.getId());
        zenekar.setNev(dto.getNev());
        zenekar.setEmail(dto.getEmail());
        zenekar.setLeiras(dto.getLeiras());
        zenekar.setVaros(dto.getVaros());
        zenekar.setTelefonszam(dto.getTelefonszam());
        return ZenekarDto.factory(zenekar);
    }

    public int count() {
        return (int) zenekarRepository.count();
    }

    public Zenekar getZenekarEntityById(Integer zenekarId) {
        return zenekarRepository.getReferenceById(zenekarId);
    }


}




