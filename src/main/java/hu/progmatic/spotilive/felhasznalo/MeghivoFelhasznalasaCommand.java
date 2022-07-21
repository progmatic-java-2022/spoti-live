package hu.progmatic.spotilive.felhasznalo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MeghivoFelhasznalasaCommand {
    private String uuid;
    private String felhasznaloNev;
    private String jelszo1;
    private String jelszo2;
    private Integer kreditMennyiseg;

}
