package hu.progmatic.spotilive.felhasznalo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MeghivoDto {
    private String uuid;
    private Felhasznalo felhasznalo;
}
