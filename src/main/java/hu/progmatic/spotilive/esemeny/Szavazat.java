package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.felhasznalo.Felhasznalo;
import hu.progmatic.spotilive.zene.Zene;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Szavazat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private Esemeny esemeny;
    @ManyToOne
    private Zene zene;
    @Builder.Default
    private Integer szavazat = 1;



}
