package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.zenekar.Zenekar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ZeneRepository extends JpaRepository<Zene, Integer> {
   List<Zene> findAllByZenekar(Zenekar zenekar);
   Optional<Zene>  getZeneByCim(String cim);
}
