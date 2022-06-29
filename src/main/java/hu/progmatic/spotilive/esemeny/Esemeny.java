package hu.progmatic.spotilive.esemeny;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Esemeny {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotEmpty
    private String nev;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH-mm")
    @NotNull
    private LocalDateTime idopont;

    //https://code-with-me.global.jetbrains.com/kmlrgWkpRYBQo8lo_VuXig#p=IU&fp=4516BCBA5B47F82A8A4F35BEF759225B2372E6077D28CC30A9EA61CC42D36205
}
