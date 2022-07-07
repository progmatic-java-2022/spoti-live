package hu.progmatic.spotilive.tag;

import hu.progmatic.spotilive.zene.TagToZene;
import hu.progmatic.spotilive.zene.Zene;
import hu.progmatic.spotilive.zene.ZeneDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TagDto {

    private Integer id;
    private String tagNev;
    private TagKategoria tagKategoria;
    private List<ZeneDto> zeneDtoList;

    public static TagDto factory(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .tagNev(tag.getTagNev())
                .zeneDtoList(getZeneDtoList(tag))
                .tagKategoria(tag.getTagKategoria())
                .build();
    }

    private static List<ZeneDto> getZeneDtoList(Tag tag) {
       List<Zene> zenek = tag.getTagToZeneEntityList().stream().map(TagToZene::getZene).toList();
       return zenek.stream().map(ZeneDto::factory).toList();

    }
}