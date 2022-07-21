package hu.progmatic.spotilive.tag;

import hu.progmatic.spotilive.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Optional<Tag> getTagByTagNev(String nev);
    Tag getTagByTagNevIgnoreCase(String nev);
}
