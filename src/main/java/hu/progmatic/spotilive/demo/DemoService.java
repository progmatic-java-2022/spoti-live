package hu.progmatic.spotilive.demo;

import hu.progmatic.spotilive.esemeny.EsemenyDto;
import hu.progmatic.spotilive.esemeny.EsemenyService;
import hu.progmatic.spotilive.zenekar.ZenekarDto;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Transactional
@Service
public class DemoService {
    public static final String DEMO_ZENEKAR = "Demo zenekar";
    public static final String DEMO_ESEMENY = "Demo esemény";
    @Autowired
    ZenekarService zenekarService;
    @Autowired
    private EsemenyService esemenyService;


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
        if (esemenyService.countAllEsemeny()==0){
            esemenyService.createEsemeny(EsemenyDto.builder()
                            .nev(DEMO_ESEMENY)
                            .idoPont(LocalDateTime.parse("2000-02-02T10:10"))
                    .build());
        }


    }
}
