package hu.progmatic.spotilive.zenekaradminfelulet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ZenekarService {
    @Autowired
    ZenekarRepository zenekarRepository;

    public ZenekarDto createZenekar(ZenekarDto zenekarDto) {
        Zenekar zenekar = Zenekar.builder().nev(zenekarDto.getNev()).build();
        return ZenekarDto.factory(zenekarRepository.save(zenekar));
    }

    public void deleteAll() {
        zenekarRepository.deleteAll();
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
        return null;
    }
}
