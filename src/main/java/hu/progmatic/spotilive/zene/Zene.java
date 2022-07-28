package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.esemeny.Szavazat;
import hu.progmatic.spotilive.zenekar.Zenekar;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedEntityGraph(
    name = "Zene.tagList",
    attributeNodes = @NamedAttributeNode(
        value = "tagToZeneEntityList",
        subgraph = "tagList.tag"
    ),
    subgraphs = {
        @NamedSubgraph(
            name = "tagList.tag",
            attributeNodes = @NamedAttributeNode("tag")
        )
    }
)
public class Zene {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotBlank(message = "Nem lehet üres")
    private String cim;

    @NotBlank(message = "Nem lehet üres")
    private String eloado;

    @NotNull(message = "Nem lehet üres")
    private Integer hosszMp;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "zene")
    private List<TagToZene> tagToZeneEntityList = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zene")
    private List<Szavazat> szavazatok = new ArrayList<>();
    @ManyToOne
    @NotNull
    private Zenekar zenekar;

    public boolean hasCheckedTags(List<String> tags) {
        if (tags.isEmpty()) {
            return true;
        }
        return new HashSet<>(
          tagToZeneEntityList.stream()
                  .map(tag -> tag.getTag().getTagNev())
                  .toList()
        ).containsAll(tags);
    }

}
