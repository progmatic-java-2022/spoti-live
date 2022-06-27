package hu.progmatic.spotilive.felhasznalo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class MyUserDetails implements UserDetails {
  private static final String ROLE_PREFIX = "ROLE_";

  private final Long felhasznaloId;
  private final String jelszo;
  private final String nev;
  private final UserType role;

  public MyUserDetails(Felhasznalo felhasznalo) {
    jelszo = felhasznalo.getJelszo();
    nev = felhasznalo.getNev();
    role = felhasznalo.getRole();
    felhasznaloId = felhasznalo.getId();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.stream(role.getRoles()).map(s -> new SimpleGrantedAuthority(ROLE_PREFIX + s)).toList();
  }

  @Override
  public String getPassword() {
    return jelszo;
  }

  @Override
  public String getUsername() {
    return nev;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public UserType getRole() {
    return role;
  }

  public Long getFelhasznaloId() {
    return felhasznaloId;
  }
}
