package hu.progmatic.spotilive.zenekaradminfelulet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ZenekarDto {
    private Integer id;
    private String nev;
    private String email;
    private String leiras;

    public static ZenekarDto factory(Zenekar zenekar){
        return ZenekarDto.builder().nev(zenekar.getNev()).id(zenekar.getId()).build();
    }
}
