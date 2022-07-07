package hu.progmatic.spotilive.tag;

import hu.progmatic.spotilive.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Integer> {
}
