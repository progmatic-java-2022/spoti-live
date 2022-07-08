package hu.progmatic.spotilive.tag;

import hu.progmatic.spotilive.tag.TagKategoria;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagEditCommand {

    private String tagNev;
    private Integer tagId;
    private Integer zeneId;
    private TagKategoria tagKategoria;
}
