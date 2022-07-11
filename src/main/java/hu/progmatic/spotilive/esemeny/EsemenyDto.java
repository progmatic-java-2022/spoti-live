package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.zene.Zene;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class EsemenyDto {
    private Integer id;
    @NotEmpty(message = "Nem lehet üres")
    private String nev;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "Meg kell adni időpontot!")
    private LocalDateTime idoPont;

    private String zenekarNev;
    @Builder.Default
    private List<ZeneToEsemenyDto> zenek = new ArrayList<>();


    public static EsemenyDto factory(Esemeny esemeny) {
        return EsemenyDto
                .builder()
                .id(esemeny.getId())
                .nev(esemeny.getNev())
                .idoPont(esemeny.getIdopont())
                .zenekarNev(esemeny.getZenekar().getNev())
                .zenek(esemeny.getZenek().stream()
                        .map(ZeneToEsemenyDto::factory)
                        .toList())
                .build();
    }

}
