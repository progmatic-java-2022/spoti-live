package hu.progmatic.spotilive.tag;

import hu.progmatic.spotilive.zene.TagToZene;
import hu.progmatic.spotilive.zene.Zene;
import hu.progmatic.spotilive.zene.ZeneDto;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class TagDto {

    private Integer id;
    @NotBlank(message = "Nem lehet üres!")
    private String tagNev;
    @NotNull(message = "Kategória megadása kötelező!")
    private TagKategoria tagKategoria;

    public static TagDto factory(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .tagNev(tag.getTagNev())
                .tagKategoria(tag.getTagKategoria())
                .build();
    }

}
