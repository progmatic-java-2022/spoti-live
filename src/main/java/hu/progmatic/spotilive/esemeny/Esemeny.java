package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.zenekar.Zenekar;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
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
    @NotNull
    private LocalDateTime idopont;
    @ManyToOne
    @NotNull
    private Zenekar zenekar;
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "esemeny", orphanRemoval = true)
    private List<Szavazat> zenek = new ArrayList<>();


}
