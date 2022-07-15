package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.zene.Zene;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SzavazatTracklistDto {
    private Integer zeneId;
    private Integer esemenyId;
    private String szamCim;

    private Integer osszSzavazat;



    public static SzavazatTracklistDto factory(Zene zene, Esemeny esemeny){
        return SzavazatTracklistDto.builder()
                .zeneId(zene.getId())
                .esemenyId(esemeny.getId())
                .szamCim(zene.getCim())
                .osszSzavazat(getZeneOsszesSzavazatai(zene,esemeny))
                .build();
    }

    private static Integer getZeneOsszesSzavazatai(Zene zene, Esemeny esemeny) {
        return zene.getSzavazatok().stream()
                .filter(szavazat -> szavazat.getEsemeny().getId().equals(esemeny.getId()))
                .mapToInt(Szavazat::getSzavazat)
                .sum();


    }
}
