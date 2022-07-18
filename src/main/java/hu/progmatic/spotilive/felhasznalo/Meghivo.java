package hu.progmatic.spotilive.felhasznalo;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meghivo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String uuid = UUID.randomUUID().toString();
    @OneToOne
    private Felhasznalo felhasznalo;
}
