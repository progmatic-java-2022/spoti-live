package hu.progmatic.spotilive.felhasznalo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MeghivoRepository extends JpaRepository <Meghivo, Integer> {


    Meghivo findMeghivoByUuidEquals(String uuid);
}
