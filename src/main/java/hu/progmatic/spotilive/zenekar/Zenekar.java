package hu.progmatic.spotilive.zenekar;

import hu.progmatic.spotilive.esemeny.Esemeny;
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
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "zenekar")
    private List<Esemeny> esemenyek = new ArrayList<>();
    private String varos;
}
