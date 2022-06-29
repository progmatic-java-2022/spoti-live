package hu.progmatic.spotilive.zene;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ZeneDto {
    private Integer id;
    private String cim;
    private String eloado;
    private Integer hosszMp;

    public static ZeneDto factory(ZeneEntity zene) {
        return ZeneDto.builder().cim(zene.getCim()).id(zene.getId()).build();
    }
    //hangnem enum (mol, dur)
    // tempo term enum (adagio, andante, lento stb.)
    //eraenum
    //mufajenum
    //taglist
}
