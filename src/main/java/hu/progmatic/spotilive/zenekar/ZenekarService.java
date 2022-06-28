package hu.progmatic.spotilive.zenekar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ZenekarService {

    public static final String TEST_ZENEKAR = "Teszt zenekar 1";
    @Autowired
   private ZenekarRepository zenekarRepository;



    public ZenekarDto createZenekar(ZenekarDto zenekarDto) {
        if(zenekarRepository.findByNev(zenekarDto.getNev()).isPresent()){
            throw new AddZenekarExeption("Zenekar már létezik ilyen névvel");
        }
        if(zenekarRepository.findByEmail(zenekarDto.getEmail()).isPresent()){
            throw new AddZenekarExeption("Zenekar már létezik ilyen email címmel");
        }
        Zenekar zenekar = Zenekar.builder()
                .nev(zenekarDto.getNev())
                .email(zenekarDto.getEmail())
                .telefonszam(zenekarDto.getTelefonszam())
                .leiras(zenekarDto.getLeiras())
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
        return ZenekarDto.factory(zenekarRepository.getZenekarByNevContainingIgnoreCase(nev));
    }

    public ZenekarDto getById(Integer id) {
        return ZenekarDto.factory(zenekarRepository.getReferenceById(id));
    }

    public ZenekarDto editZenekar(ZenekarDto dto) {
        Zenekar zenekar = zenekarRepository.getReferenceById(dto.getId());
        zenekar.setNev(dto.getNev());
        zenekar.setEmail(dto.getEmail());
        zenekar.setLeiras(dto.getLeiras());
        return ZenekarDto.factory(zenekar);
    }

    public int count() {
        return (int) zenekarRepository.count();
    }
}
