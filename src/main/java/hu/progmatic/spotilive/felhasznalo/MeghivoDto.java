package hu.progmatic.spotilive.felhasznalo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MeghivoDto {
    private Integer id;
    private String uuid;
    private Felhasznalo felhasznalo;
    private Kredit kredit;

    public static MeghivoDto factory(Meghivo meghivo) {
        return MeghivoDto.builder()
                .uuid(meghivo.getUuid())
                .kredit(meghivo.getKredit())
                .felhasznalo(meghivo.getFelhasznalo())
                .id(meghivo.getId())
                .build();
    }
}
