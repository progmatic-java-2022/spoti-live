package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.zenekar.Zenekar;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    //@FutureOrPresent
    @NotNull
    private LocalDateTime idopont;
    @ManyToOne
    @NotNull
    private Zenekar zenekar;
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "esemeny", orphanRemoval = true)
    private List<ZeneToEsemeny> zenek = new ArrayList<>();


    public List<ZeneToEsemenyDto> getZenekDto() {
        return zenek.stream().map(zeneToEsemeny -> ZeneToEsemenyDto.builder()
                        .esemeny(zeneToEsemeny.getEsemeny())
                        .zene(zeneToEsemeny.getZene())
                        .build())
                .toList();
    }
}
