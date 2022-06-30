package hu.progmatic.spotilive.zene;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagToZeneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private ZeneEntity zene;
    @ManyToOne
    private TagEntity tag;

}
