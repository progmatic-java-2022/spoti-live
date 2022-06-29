package hu.progmatic.spotilive.zene;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ZeneDto {
    private Integer id;
    private String cim;
    private String eloado;
    private Integer hosszMp;
    private List<TagDto> tagDtoList;

    //hangnem enum (mol, dur)
    // tempo term enum (adagio, andante, lento stb.)
    //eraenum
    //mufajenum
    //taglist

    public static ZeneDto factory(ZeneEntity zene) {
        return ZeneDto.builder()
                .cim(zene.getCim())
                .id(zene.getId())
                .eloado(zene.getEloado())
                .tagDtoList(getTagList(zene.getTagek()))
                .build();
    }

    private static List<TagDto> getTagList(List<TagEntity> tagek) {
        return tagek.stream().map(TagDto::factory).toList();
    }

}
