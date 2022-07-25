package hu.progmatic.spotilive.zenekar;

import hu.progmatic.spotilive.felhasznalo.UserType;
import hu.progmatic.spotilive.zene.ZeneDto;
import hu.progmatic.spotilive.zene.ZeneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ZenekarService {

    @Autowired
    private ZenekarRepository zenekarRepository;

    @RolesAllowed(UserType.Roles.ZENEKAR_KEZELES_ROLE)
    public ZenekarDto createZenekar(ZenekarDto zenekarDto) {
        if (zenekarRepository.findByNev(zenekarDto.getNev()).isPresent()) {
            throw new AddZenekarExeption("Zenekar már létezik ilyen névvel!");
        }
        if (zenekarRepository.findByEmail(zenekarDto.getEmail()).isPresent()) {
            throw new AddZenekarExeption("Zenekar már létezik ilyen email címmel!");
        }
        if (zenekarDto.getTelefonszam() != null && zenekarRepository.findByTelefonszam(zenekarDto.getTelefonszam()).isPresent()) {
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
        if (zenekarRepository.findByNev(dto.getNev()).isPresent() &&
        !zenekarRepository.getZenekarByNev(dto.getNev()).getId().equals(dto.getId())) {
            throw new AddZenekarExeption("Zenekar már létezik ilyen névvel!");
        }
        if (zenekarRepository.findByEmail(dto.getEmail()).isPresent() &&
        !zenekarRepository.getZenekarByEmail(dto.getEmail()).getId().equals(dto.getId())) {
            throw new AddZenekarExeption("Zenekar már létezik ilyen email címmel!");
        }
        if (dto.getTelefonszam() != null && zenekarRepository.findByTelefonszam(dto.getTelefonszam()).isPresent() &&
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




