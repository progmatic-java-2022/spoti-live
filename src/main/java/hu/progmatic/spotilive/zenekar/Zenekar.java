package hu.progmatic.spotilive.zenekar;

import hu.progmatic.spotilive.esemeny.Esemeny;
import hu.progmatic.spotilive.felhasznalo.Felhasznalo;
import hu.progmatic.spotilive.zene.Zene;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zenekar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String nev;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String telefonszam;
    private String leiras;
    private String varos;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "zenekar")
    private List<Esemeny> esemenyek = new ArrayList<>();


    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "zenekar")
    private List<Zene> zeneLista = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "zenekar")
    private List<Felhasznalo> tagok = new ArrayList<>();
}
