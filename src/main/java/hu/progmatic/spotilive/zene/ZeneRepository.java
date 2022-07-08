package hu.progmatic.spotilive.zene;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ZeneRepository extends JpaRepository<Zene, Integer> {
    Zene getZeneByCim(String cim);
}
