package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.esemeny.EsemenyDto;
import hu.progmatic.spotilive.esemeny.ZeneToEsemeny;
import hu.progmatic.spotilive.esemeny.ZeneToEsemenyDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zene {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotBlank(message = "Nem lehet üres")
    private String cim;
    @NotBlank(message = "Nem lehet üres")
    private String eloado;
    @NotNull(message = "Nem lehet üres")
    private Integer hosszMp;
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "zene")
    private List<TagToZene> tagToZeneEntityList = new ArrayList<>();
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zene")
    private List<ZeneToEsemeny> esemenyek = new ArrayList<>();

}
