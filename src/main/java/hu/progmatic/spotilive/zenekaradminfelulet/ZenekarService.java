package hu.progmatic.spotilive.zenekaradminfelulet;

import hu.progmatic.spotilive.felhasznalo.UjFelhasznaloCommand;
import hu.progmatic.spotilive.felhasznalo.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ZenekarService {

    public static final String TEST_ZENEKAR = "Teszt zenekar 1";
    @Autowired
    ZenekarRepository zenekarRepository;



    public ZenekarDto createZenekar(ZenekarDto zenekarDto) {
        Zenekar zenekar = Zenekar.builder().nev(zenekarDto.getNev()).build();
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

    public ZenekarDto editZenekarNev(ZenekarDto dto) {
        Zenekar zenekar = zenekarRepository.getReferenceById(dto.getId());
        zenekar.setNev(dto.getNev());
        return ZenekarDto.factory(zenekar);
    }

    public int count() {
        return (int) zenekarRepository.count();
    }
}
