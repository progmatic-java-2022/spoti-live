package hu.progmatic.spotilive.zenekar;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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
    @NotBlank
    @Column(unique = true)
    private String nev;
    @Column(unique = true)
    @NotBlank
    private String email;
    @Column(unique = true)
    private String telefonszam;
    private String leiras;

}
