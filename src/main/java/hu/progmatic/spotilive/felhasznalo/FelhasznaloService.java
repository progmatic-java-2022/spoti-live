package hu.progmatic.spotilive.felhasznalo;

import hu.progmatic.spotilive.zenekar.Zenekar;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static hu.progmatic.spotilive.demo.DemoService.ADMIN_FELHASZNALO;

@Service
@Log
@Transactional
public class FelhasznaloService {

  private final FelhasznaloRepository felhasznaloRepository;
  private final PasswordEncoder encoder;
  @Autowired
  private ZenekarService zenekarService;

  public FelhasznaloService(FelhasznaloRepository felhasznaloRepository, PasswordEncoder encoder) {
    this.felhasznaloRepository = felhasznaloRepository;
    this.encoder = encoder;
  }

  @RolesAllowed(UserType.Roles.USER_READ_ROLE)
  public List<Felhasznalo> findAll() {
    return felhasznaloRepository.findAll();
  }

  @RolesAllowed(UserType.Roles.USER_WRITE_ROLE)
  public void add(UjFelhasznaloCommand command) {
    if (felhasznaloRepository.findByNev(command.getNev()).isPresent()) {
      throw new FelhasznaloLetrehozasException("Ilyen névvel már létezik felhasználó!");
    }
    Zenekar zenekar = getZenekar(command);
    Felhasznalo felhasznalo = Felhasznalo.builder()
        .nev(command.getNev())
        .jelszo(encoder.encode(command.getJelszo()))
        .role(command.getRole())
        .zenekar(zenekar)
        .build();
    felhasznaloRepository.save(felhasznalo);
  }

  public Felhasznalo addGuest(MeghivoFelhasznalasaCommand command) {
    if (felhasznaloRepository.findByNev(command.getFelhasznaloNev()).isPresent()) {
      throw new FelhasznaloLetrehozasException(
              "felhasznaloNev",
              "Ilyen névvel már létezik felhasználó!"
      );
    }
    if (command.getFelhasznaloNev().isEmpty()) {
      throw new FelhasznaloLetrehozasException(
              "felhasznaloNev",
              "Felhasználónév megadása kötelező!"
      );
    }
    Felhasznalo felhasznalo = Felhasznalo.builder()
            .uuid(command.getUuid())
            .nev(command.getFelhasznaloNev())
            .role(UserType.GUEST)
            .jelszo(encoder.encode(command.getJelszo1()))
            .build();
    if (command.getJelszo1().length() < 5){
      throw new FelhasznaloLetrehozasException(
              "jelszo1",
              "A jelszónak legalább 5 karakter hosszúnak kell lennie"
      );
    }
    if (!command.getJelszo1().equals(command.getJelszo2())){
      throw new FelhasznaloLetrehozasException(
              "jelszo2",
              "A két jelszó nem egyezik"
      );
    }
    return felhasznaloRepository.save(felhasznalo);
  }

  private Zenekar getZenekar(UjFelhasznaloCommand command) {
    Zenekar zenekar = null;
    if (command.getZenekarId() != null) {
      zenekar = zenekarService.getZenekarEntityById(command.getZenekarId());
    }
    return zenekar;
  }

  @RolesAllowed(UserType.Roles.USER_WRITE_ROLE)
  public void delete(Long id) {
    felhasznaloRepository.deleteById(id);
  }

  @RolesAllowed(UserType.Roles.USER_READ_ROLE)
  public Optional<Felhasznalo> findByName(String nev) {
    return felhasznaloRepository.findByNev(nev);
  }


  public Felhasznalo getById(Long id) {
    return felhasznaloRepository.getFelhasznaloById(id);
  }

  public boolean hasRole(String role) {
    return SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getAuthorities()
        .stream()
        .anyMatch(
            grantedAuthority ->
                grantedAuthority
                    .getAuthority()
                    .equals(MyUserDetails.ROLE_PREFIX + role)
        );
  }

  public Long getFelhasznaloId() {
    MyUserDetails userPrincipal = getMyUserDetails();
    if (userPrincipal == null) {
      return null;
    }
    return userPrincipal.getFelhasznaloId();
  }

  public boolean isAdmin() {
    MyUserDetails userPrincipal = getMyUserDetails();
    if (userPrincipal == null) {
      return hasRole(UserType.Roles.ADMIN_ROLE);
    }
    return userPrincipal.getRole().equals(UserType.ADMIN);
  }
  public boolean isZenekar() {
    MyUserDetails userPrincipal = getMyUserDetails();
    if (userPrincipal == null) {
      return hasRole(UserType.Roles.ZENE_KEZELES_ROLE);
    }
    return userPrincipal.getRole().equals(UserType.ZENEKAR);
  }
  public boolean isGuest() {
    MyUserDetails userPrincipal = getMyUserDetails();
    if (userPrincipal == null) {
      return hasRole(UserType.Roles.GUEST_READ_ROLE);
    }
    return userPrincipal.getRole().equals(UserType.GUEST);
  }
  public Integer getZenekarId() {
    MyUserDetails userPrincipal = getMyUserDetails();
    if (userPrincipal == null) {
      return null;
    }
    return userPrincipal.getZenekarId();
  }

  private MyUserDetails getMyUserDetails() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof MyUserDetails) {
      return (MyUserDetails) principal;
    }
    return null;
  }

  public void createAlapFelhasznalok() {
    if (felhasznaloRepository.count() == 0) {
      add(new UjFelhasznaloCommand(ADMIN_FELHASZNALO, "adminpass", UserType.ADMIN, null));
      add(new UjFelhasznaloCommand("user", "user", UserType.USER, null));
    }
  }
}
