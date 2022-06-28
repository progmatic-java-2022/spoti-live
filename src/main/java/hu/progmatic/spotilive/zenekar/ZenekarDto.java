package hu.progmatic.spotilive.zenekar;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ZenekarDto {
    private Integer id;
    private String nev;
    private String email;
    private String telefonszam;
    private String leiras;

    public static ZenekarDto factory(Zenekar zenekar){
        return ZenekarDto.builder()
                .nev(zenekar.getNev())
                .telefonszam(zenekar.getTelefonszam())
                .leiras(zenekar.getLeiras())
                .email(zenekar.getEmail())
                .id(zenekar.getId())
                .build();
    }
}
