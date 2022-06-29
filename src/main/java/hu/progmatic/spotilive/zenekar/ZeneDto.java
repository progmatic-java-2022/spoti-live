package hu.progmatic.spotilive.zenekar;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class ZeneDto {
    Integer id;
    String cim;
    String eloado;
    Integer hosszMp;

    public static ZeneDto factory(ZeneEntity zene) {
        return ZeneDto.builder().cim(zene.getCim()).id(zene.getId()).build();
    }
    //hangnem enum (mol, dur)
    // tempo term enum (adagio, andante, lento stb.)
    //eraenum
    //mufajenum
    //taglist
}
