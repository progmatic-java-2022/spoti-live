package hu.progmatic.spotilive.zenekar;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ZenekarDto {
    private Integer id;
    @NotBlank(message = "Nem lehet üres")
    private String nev;
    @Email(message = "Helyes formátum kell")
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
