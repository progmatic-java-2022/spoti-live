package hu.progmatic.spotilive.felhasznalo;

import hu.progmatic.spotilive.esemeny.Szavazat;
import hu.progmatic.spotilive.zenekar.Zenekar;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Felhasznalo {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, unique = true)
  private String nev;

  private String jelszo;

  @Enumerated(EnumType.STRING)
  private UserType role;

  @ManyToOne
  private Zenekar zenekar;

  @OneToMany(mappedBy = "felhasznalo")
  @Builder.Default
  private List<Szavazat> szavazatok = new ArrayList<>();

  @OneToOne (cascade = CascadeType.ALL, mappedBy = "felhasznalo")
  @Builder.Default
  private Meghivo meghivo = null;
}