package hu.progmatic.spotilive.zene;

import lombok.Builder;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import java.util.List;

@Data
@Builder
public class TagDto {

    private Integer id;
    private String tagNev;
    private List<ZeneDto> zeneDtoList;

    public static TagDto factory(TagEntity tag) {
        return TagDto.builder()
                .id(tag.getId())
                .tagNev(tag.getTagNev())
                .zeneDtoList(getZeneDtoList(tag))
                .build();
    }

    private static List<ZeneDto> getZeneDtoList(TagEntity tag) {
       List<ZeneEntity> zenek = tag.getTagToZeneEntityList().stream().map(TagToZeneEntity::getZene).toList();
       return zenek.stream().map(ZeneDto::factory).toList();

    }
}
