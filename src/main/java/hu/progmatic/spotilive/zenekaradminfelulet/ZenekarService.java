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

    }

    public List<ZenekarDto> findAllDto() {
        return null;
    }
}
