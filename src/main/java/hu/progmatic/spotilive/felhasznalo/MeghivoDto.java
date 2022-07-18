package hu.progmatic.spotilive.felhasznalo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MeghivoDto {
    private String uuid;
    private Felhasznalo felhasznalo;

    public static MeghivoDto factory(Meghivo meghivo) {
        return MeghivoDto.builder().uuid(meghivo.getUuid()).felhasznalo(meghivo.getFelhasznalo()).build();
    }
}
