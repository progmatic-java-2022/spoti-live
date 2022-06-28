package hu.progmatic.spotilive.demo;

import hu.progmatic.spotilive.esemeny.EsemenyDto;
import hu.progmatic.spotilive.esemeny.EsemenyService;
import hu.progmatic.spotilive.zenekaradminfelulet.ZenekarDto;
import hu.progmatic.spotilive.zenekaradminfelulet.ZenekarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class
DemoService {
    public static final String DEMO_ZENEKAR = "Demo zenekar";
//    public static final String DEMO_ESEMENY = "Demo esemény";
    @Autowired
    ZenekarService zenekarService;

    @Autowired
    EsemenyService esemenyService;

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
    if(zenekarService.count() == 0){
        zenekarService.createZenekar(ZenekarDto.builder().nev(DEMO_ZENEKAR).build());
    }
//    if(esemenyService.countAllEsemeny() == 0){
//        esemenyService.createEsemeny(EsemenyDto.builder().nev(DEMO_ESEMENY).build());
//    }
    }
}
