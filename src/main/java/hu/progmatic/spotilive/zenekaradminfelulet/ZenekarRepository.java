package hu.progmatic.spotilive.zenekaradminfelulet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ZenekarRepository extends JpaRepository<Zenekar, Integer> {
    Zenekar getZenekarByNevContainingIgnoreCase(String nev);
}
