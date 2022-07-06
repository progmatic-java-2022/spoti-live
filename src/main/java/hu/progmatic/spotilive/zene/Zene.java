package hu.progmatic.spotilive.zene;

import lombok.*;

import javax.persistence.*;
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
    private String cim;
    private String eloado;
    private Integer hosszMp;
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "zene")
    private List<TagToZene> tagToZeneEntityList = new ArrayList<>();

}
