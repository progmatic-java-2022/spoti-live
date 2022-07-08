package hu.progmatic.spotilive.zenekar;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ZenekarDto {
    private Integer id;
    @NotBlank(message = "Mező kitöltése kötelező!")
    private String nev;
    @NotBlank(message = "Mező kitöltése kötelező!")
    @Email(message = "Létező email cím megadása kötelező!")
    private String email;
    private String telefonszam;
    private String leiras;
    @NotBlank(message = "Mező kitöltése kötelező!")
    private String varos;
    @Builder.Default
    List<ZeneToZenekarDto> zenek = new ArrayList<>();

    public static ZenekarDto factory(Zenekar zenekar){
        return ZenekarDto.builder()
                .nev(zenekar.getNev())
                .telefonszam(zenekar.getTelefonszam())
                .leiras(zenekar.getLeiras())
                .email(zenekar.getEmail())
                .id(zenekar.getId())
                .varos(zenekar.getVaros())
                .zenek(zenekar.getZenek().stream().map(ZeneToZenekarDto::factory).toList())
                .build();
    }
}
