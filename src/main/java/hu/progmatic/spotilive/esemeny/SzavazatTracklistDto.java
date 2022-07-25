package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.felhasznalo.Felhasznalo;
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

    private Integer szavazatByFelhasznalo;



    public static SzavazatTracklistDto factory(Zene zene, Esemeny esemeny, Felhasznalo felhasznalo){
        return SzavazatTracklistDto.builder()
                .zeneId(zene.getId())
                .esemenyId(esemeny.getId())
                .szamCim(zene.getCim())
                .osszSzavazat(getZeneOsszesSzavazatai(zene,esemeny))
                .szavazatByFelhasznalo(getZeneSzavazataiByFelhasznalo(zene,esemeny,felhasznalo))
                .build();
    }

    private static Integer getZeneOsszesSzavazatai(Zene zene, Esemeny esemeny) {
        return zene.getSzavazatok().stream()
                .filter(szavazat -> szavazat.getEsemeny().getId().equals(esemeny.getId()))
                .mapToInt(Szavazat::getSzavazat)
                .sum();
    }

    private static Integer getZeneSzavazataiByFelhasznalo(Zene zene, Esemeny esemeny, Felhasznalo felhasznalo) {
        return zene.getSzavazatok().stream()
                .filter(szavazat -> szavazat.getEsemeny().getId().equals(esemeny.getId()))
                .filter(szavazat -> szavazat.getFelhasznalo().getId().equals(felhasznalo.getId()))
                .mapToInt(Szavazat::getSzavazat)
                .sum();
    }
}
