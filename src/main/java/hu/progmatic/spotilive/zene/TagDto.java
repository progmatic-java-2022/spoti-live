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
    private ZeneDto zeneSzam;

    public static TagDto factory(TagEntity tag) {
        return TagDto.builder()
                .id(tag.getId())
                .tagNev(tag.getTagNev())
                .zeneSzam(ZeneDto.factory(tag.getZeneSzam()))
                .build();
    }
}
