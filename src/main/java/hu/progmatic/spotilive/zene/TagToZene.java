package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.tag.Tag;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagToZene {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private Zene zene;
    @ManyToOne
    private Tag tag;

}
