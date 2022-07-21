package hu.progmatic.spotilive.esemeny;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class EsemenyDto {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Integer id;
    @NotEmpty(message = "Nem lehet üres")
    private String nev;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "Meg kell adni időpontot!")
    private LocalDateTime idoPont;

    private String idoPunt;

    private String zenekarNev;
    @Builder.Default
    private List<SzavazatDto> szavazatDtos = new ArrayList<>();


    public static EsemenyDto factory(Esemeny esemeny) {
        return EsemenyDto
                .builder()
                .id(esemeny.getId())
                .nev(esemeny.getNev())
                .idoPont(esemeny.getIdopont())
                .idoPunt(formaz(esemeny.getIdopont()))
                .zenekarNev(esemeny.getZenekar().getNev())
                .szavazatDtos(esemeny.getZenek().stream()
                        .map(SzavazatDto::factory)
                        .toList())
                .build();
    }

    private static String formaz(LocalDateTime idopont) {
        return idopont.format(formatter);
    }

}
