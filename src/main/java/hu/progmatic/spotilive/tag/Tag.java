package hu.progmatic.spotilive.tag;

import hu.progmatic.spotilive.zene.TagToZene;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String tagNev;
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private TagKategoria tagKategoria;
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "tag")
    private List<TagToZene> tagToZeneEntityList = new ArrayList<>();

}
