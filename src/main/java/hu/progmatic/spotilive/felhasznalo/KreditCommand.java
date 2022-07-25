package hu.progmatic.spotilive.felhasznalo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class KreditCommand {

    @Builder.Default
    private Integer kreditekSzama = 0;
    private String emailCim;
}
