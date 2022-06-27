package hu.progmatic.spotilive.felhasznalo;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
  private final FelhasznaloRepository felhasznaloRepository;

  public MyUserDetailsService(FelhasznaloRepository felhasznaloRepository) {
    this.felhasznaloRepository = felhasznaloRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    Optional<Felhasznalo> felhasznalo = felhasznaloRepository.findByNev(username);
    if (felhasznalo.isEmpty()) {
      throw new UsernameNotFoundException(username);
    }
    return new MyUserDetails(felhasznalo.get());
  }
}