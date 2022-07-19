package hu.progmatic.spotilive.felhasznalo;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kredit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Builder.Default
    private Integer kreditMennyiseg = 1;
    @OneToOne
    Meghivo meghivo;
    @OneToOne
    @Builder.Default
    Felhasznalo felhasznalo = null;
}
