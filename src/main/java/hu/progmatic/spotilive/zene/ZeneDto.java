package hu.progmatic.spotilive.zene;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ZeneDto {
    private Integer id;
    private String cim;
    private String eloado;
    private Integer hosszMp;
    private List<String> tagStringList;


    public static ZeneDto factory(ZeneEntity zene) {
        return ZeneDto.builder()
                .cim(zene.getCim())
                .id(zene.getId())
                .eloado(zene.getEloado())
                .tagStringList(getTagStringList(zene.getTagToZeneEntityList()))
                .build();
    }

    private static List<String> getTagStringList(List<TagToZeneEntity> tagToZeneEntityList) {
        return tagToZeneEntityList
                .stream()
                .map(TagToZeneEntity::getTag)
                .map(TagEntity::getTagNev)
                .toList();
    }


}
