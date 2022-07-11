package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.esemeny.ZeneToEsemenyDto;
import hu.progmatic.spotilive.tag.Tag;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class ZeneDto {
    private Integer id;
    @NotBlank(message = "Nem lehet üres")
    private String cim;
    @NotBlank(message = "Nem lehet üres")
    private String eloado;
    @NotNull(message = "Nem lehet üres")
    private Integer hosszMp;
    private List<String> tagStringList;
    @Builder.Default
    private List<ZeneToEsemenyDto> esemenyek=new ArrayList<>();

    private String zenekarNev;


    public static ZeneDto factory(Zene zene) {
        return ZeneDto.builder()
                .cim(zene.getCim())
                .id(zene.getId())
                .eloado(zene.getEloado())
                .hosszMp(zene.getHosszMp())
                .tagStringList(getTagStringList(zene.getTagToZeneEntityList()))
                .zenekarNev(zene.getZenekar().getNev())
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
