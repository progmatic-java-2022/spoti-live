package hu.progmatic.spotilive.demo;

import hu.progmatic.spotilive.esemeny.AddZeneToEsemenyCommand;
import hu.progmatic.spotilive.esemeny.CreateEsemenyCommand;
import hu.progmatic.spotilive.esemeny.EsemenyService;
import hu.progmatic.spotilive.felhasznalo.UserType;
import hu.progmatic.spotilive.tag.TagKategoria;
import hu.progmatic.spotilive.tag.TagService;
import hu.progmatic.spotilive.tag.TagDto;
import hu.progmatic.spotilive.zene.ZeneDto;
import hu.progmatic.spotilive.zene.ZeneService;
import hu.progmatic.spotilive.zenekar.AddZeneToZenekarCommand;
import hu.progmatic.spotilive.zenekar.ZenekarDto;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static hu.progmatic.spotilive.felhasznalo.MyUserDetails.ROLE_PREFIX;

@Transactional
@Service
public class DemoService {

    public static final String DEMO_ZENEKAR = "Demo zenekar";
    public static final String DEMO_ESEMENY = "Demo esemény";

    public static final String DEMO_TAG = "Demo tag";
    public static final String DEMO_ZENE = "Demo zene cím";
    @Autowired
    ZenekarService zenekarService;
    @Autowired
    private EsemenyService esemenyService;
    @Autowired
    private ZeneService zeneService;

    @Autowired
    private TagService tagService;

    private void clearAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private void configureAuthentication(String... roles) {
        List<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .toList();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "user",
                "credentials",
                authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        if (zenekarService.count() == 0) {
            configureAuthentication(
                    UserType.Roles.ZENEKAR_KEZELES_ROLE,
                    UserType.Roles.ESEMENY_KEZELES_ROLE
            );
            var demoZenekar = zenekarService.createZenekar(ZenekarDto.builder()
                    .nev(DEMO_ZENEKAR)
                    .email("teszt@gmail.com")
                    .telefonszam("0630-111-2222")
                    .varos("Budapest")
                    .leiras("Demo leírás")
                    .build());

            var demoEsemeny = esemenyService.createEsemeny(CreateEsemenyCommand.builder()
                    .nev(DEMO_ESEMENY)
                    .idoPont(LocalDateTime.parse("2000-02-02T10:10"))
                    .zenekarId(demoZenekar.getId())
                    .build());

            var demotag = tagService.createTag(TagDto.builder()
                    .tagNev(DEMO_TAG)
                    .tagKategoria(TagKategoria.MUFAJ)
                    .build());

            var demotag2 = tagService.createTag(TagDto.builder()
                    .tagNev("Demo tag 2")
                    .tagKategoria(TagKategoria.MUFAJ)
                    .build());

            var hangulatTag = tagService.createTag(TagDto.builder()
                    .tagNev("Hangulat tag")
                    .tagKategoria(TagKategoria.HANGULAT)
                    .build());

            var demoZene = zeneService.createZene(ZeneDto.builder()
                    .cim(DEMO_ZENE)
                    .eloado("Demo Zene előadó")
                    .hosszMp(123)
                    .build());

            zeneService.addTag(demoZene.getId(), demotag.getId());
            zeneService.addTag(demoZene.getId(), hangulatTag.getId());

            esemenyService.addZenetoEsemenyByZeneId(AddZeneToEsemenyCommand
                    .builder()
                    .esemenyId(demoEsemeny.getId())
                    .zeneId(demoZene.getId())
                    .build());

            zenekarService.addZeneToZenekar(AddZeneToZenekarCommand.builder()
                    .zeneId(demoZene.getId())
                    .zenekarId(demoZenekar.getId())
                    .build());

            clearAuthentication();
        }
    }
}
