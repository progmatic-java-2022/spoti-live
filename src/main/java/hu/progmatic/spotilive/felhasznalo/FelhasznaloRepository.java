package hu.progmatic.spotilive.felhasznalo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FelhasznaloRepository extends JpaRepository<Felhasznalo, Long> {
  Felhasznalo findFelhasznaloByUuidEquals(String uuid);

  Optional<Felhasznalo> findByNev(String nev);
  Felhasznalo getFelhasznaloById(Long id);
}
