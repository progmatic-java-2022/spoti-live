package hu.progmatic.spotilive.zenekar;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ZenekarRepository extends JpaRepository<Zenekar, Integer> {
    Zenekar getZenekarByNevContainingIgnoreCase(String nev);
    Optional<Zenekar> findByNev(String nev);
    Optional<Zenekar> findByEmail(String email);
    Optional<Zenekar> findByTelefonszam(String telefonszam);
}
