package hu.progmatic.spotilive.demo;

import hu.progmatic.spotilive.zenekar.ZenekarDto;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class DemoService {
    public static final String DEMO_ZENEKAR = "Demo zenekar";
    @Autowired
    ZenekarService zenekarService;

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        if (zenekarService.count() == 0) {
            zenekarService.createZenekar(ZenekarDto.builder()
                    .nev(DEMO_ZENEKAR)
                    .email("teszt@gmail.com")
                    .telefonszam("0630-111-2222")
                    .leiras("Demo leírás")
                    .build());
        }
    }
}
