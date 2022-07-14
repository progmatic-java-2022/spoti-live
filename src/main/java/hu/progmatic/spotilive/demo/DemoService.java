package hu.progmatic.spotilive.demo;

import hu.progmatic.spotilive.esemeny.AddZeneToEsemenyCommand;
import hu.progmatic.spotilive.esemeny.CreateEsemenyCommand;
import hu.progmatic.spotilive.esemeny.EsemenyService;
import hu.progmatic.spotilive.felhasznalo.FelhasznaloService;
import hu.progmatic.spotilive.felhasznalo.UjFelhasznaloCommand;
import hu.progmatic.spotilive.felhasznalo.UserType;
import hu.progmatic.spotilive.tag.TagKategoria;
import hu.progmatic.spotilive.tag.TagService;
import hu.progmatic.spotilive.tag.TagDto;
import hu.progmatic.spotilive.zene.CreateZeneCommand;
import hu.progmatic.spotilive.zene.ZeneService;
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

  public static final String ADMIN_FELHASZNALO = "admin";
  public static final String ZENEKAR_1_FELHASZNALO = "zenekar1";
  public static final String ZENEKAR_2_FELHASZNALO = "zenekar2";

  public static final String DEMO_ZENEKAR = "Demo zenekar";
  public static final String DEMO_ESEMENY = "Demo esemény";

  public static final String DEMO_TAG = "Demo tag";
  public static final String DEMO_HANGULAT_TAG = "Hangulat tag";
  public static final String DEMO_ZENE = "Demo zene cím";
  public static final String DEMO_ZENE2 = "Demo zene cím 2";
  public static final String DEMO_ZENE3 = "Demo zene cím 3";
  public static final String PREFIX1 = "1_zenekar_";
  public static final String PREFIX2 = "2_zenekar_";
  @Autowired
  ZenekarService zenekarService;
  @Autowired
  private EsemenyService esemenyService;
  @Autowired
  private ZeneService zeneService;

  @Autowired
  private TagService tagService;
  @Autowired
  private FelhasznaloService felhasznaloService;


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
          UserType.Roles.ESEMENY_KEZELES_ROLE,
          UserType.Roles.USER_WRITE_ROLE
      );

      createAlapFelhasznalok();
      createTagek();
      createDemoZenekar(PREFIX1, "2222", ZENEKAR_1_FELHASZNALO);
      createDemoZenekar(PREFIX2, "3333", ZENEKAR_2_FELHASZNALO);

      clearAuthentication();
    }
  }

  private void createAlapFelhasznalok() {
    felhasznaloService.add(new UjFelhasznaloCommand(ADMIN_FELHASZNALO, "adminpass", UserType.ADMIN, null));
    felhasznaloService.add(new UjFelhasznaloCommand("user", "user", UserType.USER, null));
    felhasznaloService.add(new UjFelhasznaloCommand("guest", "guest", UserType.GUEST, null));
  }

  private void createTagek() {
    tagService.createTag(TagDto.builder()
        .tagNev(DEMO_TAG)
        .tagKategoria(TagKategoria.MUFAJ)
        .build());

    tagService.createTag(TagDto.builder()
        .tagNev("Demo tag 2")
        .tagKategoria(TagKategoria.MUFAJ)
        .build());

    tagService.createTag(TagDto.builder()
        .tagNev(DEMO_HANGULAT_TAG)
        .tagKategoria(TagKategoria.HANGULAT)
        .build());
  }


  private void createDemoZenekar(
      String prefix,
      String telefonszamUtolsoNegyKaraktere,
      String felhasznalonev
  ) {
    var demoZenekar = zenekarService.createZenekar(ZenekarDto.builder()
        .nev(prefix + DEMO_ZENEKAR)
        .email(prefix + "teszt@gmail.com")
        .telefonszam("0630-111-" + telefonszamUtolsoNegyKaraktere)
        .varos("Budapest")
        .leiras("Demo leírás")
        .build());
    felhasznaloService.add(
        new UjFelhasznaloCommand(
            felhasznalonev,
            "zenekar",
            UserType.ZENEKAR,
            demoZenekar.getId()
        )
    );


    var demoEsemeny = esemenyService.createEsemeny(CreateEsemenyCommand.builder()
        .nev(prefix + DEMO_ESEMENY)
        .idoPont(LocalDateTime.parse("2000-02-02T10:10"))
        .zenekarId(demoZenekar.getId())
        .build());


    var demoZene = zeneService.createZene(CreateZeneCommand.builder()
        .cim(prefix + DEMO_ZENE)
        .eloado("Demo Zene előadó")
        .hosszMp(123)
        .zenekarId(demoZenekar.getId())
        .build());

    var demoZene2 = zeneService.createZene(CreateZeneCommand.builder()
        .cim(prefix + DEMO_ZENE2)
        .eloado("Demo Zene előadó 2")
        .hosszMp(456)
        .zenekarId(demoZenekar.getId())
        .build());

    var demoZene3 = zeneService.createZene(CreateZeneCommand.builder()
        .cim(prefix + DEMO_ZENE3)
        .eloado("Demo Zene előadó 3")
        .hosszMp(789)
        .zenekarId(demoZenekar.getId())
        .build());

    zeneService.addTag(demoZene.getId(), tagService.getTagDtoByNev(DEMO_TAG).getId());
    zeneService.addTag(demoZene.getId(), tagService.getTagDtoByNev(DEMO_HANGULAT_TAG).getId());

    esemenyService.addZenetoEsemenyByZeneId(AddZeneToEsemenyCommand
        .builder()
        .esemenyId(demoEsemeny.getId())
        .zeneId(demoZene.getId())
        .build());

    esemenyService.addZenetoEsemenyByZeneId(AddZeneToEsemenyCommand
        .builder()
        .esemenyId(demoEsemeny.getId())
        .zeneId(demoZene2.getId())
        .build());

    esemenyService.addZenetoEsemenyByZeneId(AddZeneToEsemenyCommand
        .builder()
        .esemenyId(demoEsemeny.getId())
        .zeneId(demoZene3.getId())
        .build());
  }
}
