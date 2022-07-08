package hu.progmatic.spotilive.zenekar;


import hu.progmatic.spotilive.zene.Zene;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZeneToZenekar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    @ManyToOne
    Zenekar zenekar;
    @ManyToOne
    Zene zene;
}
