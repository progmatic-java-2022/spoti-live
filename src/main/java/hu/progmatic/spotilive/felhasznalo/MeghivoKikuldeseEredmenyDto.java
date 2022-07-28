package hu.progmatic.spotilive.felhasznalo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeghivoKikuldeseEredmenyDto {

    String emailCim;
    Integer kreditekSzama;
    boolean sikeresKuldes;
    @Builder.Default
    String hibaUzenet = "Sikeres küldés";
}
