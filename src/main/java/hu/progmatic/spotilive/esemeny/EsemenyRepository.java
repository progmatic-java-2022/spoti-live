package hu.progmatic.spotilive.esemeny;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EsemenyRepository extends JpaRepository<Esemeny, Integer> {

    Esemeny findEsemenyByNevContainingIgnoreCase(String nev);
}
