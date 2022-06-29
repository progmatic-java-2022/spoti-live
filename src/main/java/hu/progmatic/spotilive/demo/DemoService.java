package hu.progmatic.spotilive.demo;

import hu.progmatic.spotilive.esemeny.Esemeny;
import hu.progmatic.spotilive.esemeny.EsemenyDto;
import hu.progmatic.spotilive.esemeny.EsemenyRepository;
import hu.progmatic.spotilive.esemeny.EsemenyService;
import hu.progmatic.spotilive.felhasznalo.UserType;
import hu.progmatic.spotilive.zene.ZeneEntity;
import hu.progmatic.spotilive.zene.ZeneRepository;
import hu.progmatic.spotilive.zenekar.Zenekar;
import hu.progmatic.spotilive.zenekar.ZenekarDto;
import hu.progmatic.spotilive.zenekar.ZenekarRepository;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Transactional
@Service
public class DemoService implements InitializingBean {

    public static final String DEMO_ZENEKAR = "Demo zenekar";
    public static final String DEMO_ESEMENY = "Demo esemény";
    @Autowired
    ZenekarService zenekarService;
    @Autowired
    private EsemenyService esemenyService;
    @Autowired
    private EsemenyRepository esemenyRepository;
    @Autowired
    private ZenekarRepository zenekarRepository;

    @Autowired
    private ZeneRepository zeneRepository;


//    @EventListener(ContextRefreshedEvent.class)
//    public void init() {
//        if (zenekarService.count() == 0) {
//            zenekarService.createZenekar(ZenekarDto.builder()
//                    .nev(DEMO_ZENEKAR)
//                    .email("teszt@gmail.com")
//                    .telefonszam("0630-111-2222")
//                    .leiras("Demo leírás")
//                    .build());
//        }
//        if (esemenyService.countAllEsemeny()==0){
//            esemenyService.createEsemeny(EsemenyDto.builder()
//                            .nev(DEMO_ESEMENY)
//                            .idoPont(LocalDateTime.parse("2000-02-02T10:10"))
//                    .build());
//        }
//    }
    //https://code-with-me.global.jetbrains.com/kmlrgWkpRYBQo8lo_VuXig#p=IU&fp=4516BCBA5B47F82A8A4F35BEF759225B2372E6077D28CC30A9EA61CC42D36205

    @Override
    public void afterPropertiesSet() throws Exception {
        esemenyRepository.save(
                Esemeny.builder()
                        .nev(DEMO_ESEMENY)
                        .idopont(LocalDateTime.parse("2000-02-02T10:10"))
                        .build()
        );
        zenekarRepository.save(
                Zenekar.builder()
                        .nev(DEMO_ESEMENY)
                        .email("teszt@gmail.com")
                        .telefonszam("0630-111-2222")
                        .leiras("Demo leírás")
                        .build()
        );

        zeneRepository.save(ZeneEntity.builder()
                        .cim("Demo Zene Cím")
                        .eloado("Demo Zene előadó")

                .build());

    }
}
