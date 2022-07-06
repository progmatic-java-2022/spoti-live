package hu.progmatic.spotilive.zene;

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

}
