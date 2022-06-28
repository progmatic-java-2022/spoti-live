package hu.progmatic.spotilive.esemeny;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Builder
public class EsemenyDto {
    private Integer id;
    @NotEmpty(message = "Nem lehet üres")
    private String nev;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "Meg kell adni időpontot!")
    private LocalDateTime idoPont;

    public static EsemenyDto factory(Esemeny esemeny) {
        return EsemenyDto
                .builder()
                .id(esemeny.getId())
                .nev(esemeny.getNev())
                .idoPont(esemeny.getIdopont())
                .build();
    }
}
