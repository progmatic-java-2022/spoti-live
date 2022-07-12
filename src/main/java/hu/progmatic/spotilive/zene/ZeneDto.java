package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.esemeny.ZeneToEsemenyDto;
import hu.progmatic.spotilive.tag.Tag;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
    @Min(message = "A zene hossza minimum 120 másodperc!", value = 120)
    private Integer hosszMp;

    private List<String> tagStringList;
    @Builder.Default
    private List<ZeneToEsemenyDto> esemenyek=new ArrayList<>();

    private String zenekarNev;
    private Integer zenekarId;


    public static ZeneDto factory(Zene zene) {
        return ZeneDto.builder()
                .cim(zene.getCim())
                .id(zene.getId())
                .eloado(zene.getEloado())
                .hosszMp(zene.getHosszMp())
                .tagStringList(getTagStringList(zene.getTagToZeneEntityList()))
                .zenekarNev(zene.getZenekar().getNev())
                .zenekarId(zene.getZenekar().getId())
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
