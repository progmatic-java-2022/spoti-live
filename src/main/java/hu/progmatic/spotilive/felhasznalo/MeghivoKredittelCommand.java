package hu.progmatic.spotilive.felhasznalo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
@Builder
public class MeghivoKredittelCommand {

    @Positive(message = "Nem lehet negatív")
    @Builder.Default
    private Integer kreditekSzama = 0;
    private String emailCim;
}
