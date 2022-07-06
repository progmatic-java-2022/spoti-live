package hu.progmatic.spotilive.zene;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
public class ZeneDto {
    private Integer id;
    @NotEmpty
    private String cim;
    @NotEmpty
    private String eloado;
    @NotNull
    private Integer hosszMp;
    private List<String> tagStringList;


    public static ZeneDto factory(Zene zene) {
        return ZeneDto.builder()
                .cim(zene.getCim())
                .id(zene.getId())
                .eloado(zene.getEloado())
                .hosszMp(zene.getHosszMp())
                .tagStringList(getTagStringList(zene.getTagToZeneEntityList()))
                .build();
    }

    private static List<String> getTagStringList(List<TagToZene> tagToZeneEntityList) {
        return tagToZeneEntityList
                .stream()
                .map(TagToZene::getTag)
                .map(Tag::getTagNev)
                .toList();
    }


}
