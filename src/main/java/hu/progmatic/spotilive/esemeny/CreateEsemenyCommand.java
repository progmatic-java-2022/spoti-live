package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.zenekar.Zenekar;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class CreateEsemenyCommand {

    @NotEmpty(message = "Nem lehet üres")
    private String nev;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "Meg kell adni időpontot!")
    private LocalDateTime idoPont;
    @NotNull
    private Integer zenekarId;
}
