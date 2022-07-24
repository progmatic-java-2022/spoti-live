package hu.progmatic.spotilive.zenekar;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ZenekarRepository extends JpaRepository<Zenekar, Integer> {
    Optional<Zenekar> findByNev(String nev);
    Optional<Zenekar> findByEmail(String email);
    Optional<Zenekar> findByTelefonszam(String telefonszam);

    Zenekar getZenekarByNev(String nev);
    Zenekar getZenekarByEmail(String email);
    Zenekar getZenekarByTelefonszam(String telefonszam);
}
