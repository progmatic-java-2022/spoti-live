package hu.progmatic.spotilive.esemeny;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EsemenyRepository extends JpaRepository<Esemeny, Integer> {

    Esemeny findEsemenyByNevContainingIgnoreCase(String nev);


    @Query("""
select zene
from ZeneToEsemeny zene
where zene.esemeny.id = :esemenyId
order by zene.szavazat desc , zene.zene.cim asc
""")
    List<ZeneToEsemeny> getZenekByLikesAndAbc(@Param("esemenyId") Integer esemenyid);

    List<Esemeny> findAllByZenekarId(Integer zenekarId);
}
