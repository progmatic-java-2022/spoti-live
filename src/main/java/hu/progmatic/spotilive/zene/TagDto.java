package hu.progmatic.spotilive.zene;

import lombok.Builder;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;

@Data
@Builder
public class TagDto {

    private Integer id;
    private String tagNev;
//    private ZeneDto zeneSzam;
    private Integer zeneId;

    public static TagDto factory(TagEntity tag) {
        return TagDto.builder()
                .id(tag.getId())
                .tagNev(tag.getTagNev())
                .build();
    }
}
