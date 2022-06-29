package hu.progmatic.spotilive.zenekar;

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
public class ZeneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String cim;
    private String eloado;
    private Integer hosszMp;
    @Builder.Default
    @OneToMany(orphanRemoval = true,cascade = CascadeType.ALL,mappedBy = "zeneSzam")
    private List<TagEntity> tagek = new ArrayList<>();
}
