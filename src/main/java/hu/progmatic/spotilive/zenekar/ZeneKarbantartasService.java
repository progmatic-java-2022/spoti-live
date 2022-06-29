package hu.progmatic.spotilive.zenekar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class ZeneKarbantartasService {
    @Autowired
    ZeneRepository zeneRepository;

    public ZeneDto createZene(ZeneDto zeneDto) {
        ZeneEntity zene = ZeneEntity.builder().cim(zeneDto.cim).build();
        return ZeneDto.factory( zeneRepository.save(zene));
    }
}
