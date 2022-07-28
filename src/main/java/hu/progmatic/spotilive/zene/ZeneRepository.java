package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.zenekar.Zenekar;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ZeneRepository extends JpaRepository<Zene, Integer> {
   List<Zene> findAllByZenekar(Zenekar zenekar);
   Optional<Zene>  getZeneByCim(String cim);

  @EntityGraph(value = "Zene.tagList", type = EntityGraph.EntityGraphType.LOAD)
  List<Zene> findAllByZenekarId(Integer zenekarId);
}
