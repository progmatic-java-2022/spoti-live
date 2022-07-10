package hu.progmatic.spotilive.zene;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ZeneRepository extends JpaRepository<Zene, Integer> {
   Optional<Zene>  getZeneByCim(String cim);
}
