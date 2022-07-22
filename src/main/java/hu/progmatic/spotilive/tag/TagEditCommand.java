package hu.progmatic.spotilive.tag;

import hu.progmatic.spotilive.tag.TagKategoria;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class TagEditCommand {

    private String tagNev;
    private Integer tagId;
    private Integer zeneId;
    @NotNull(message = "Műfaj megadása kötelező!")
    private TagKategoria tagKategoria;
}
