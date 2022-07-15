package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.felhasznalo.Felhasznalo;
import hu.progmatic.spotilive.zene.ZeneDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SzavazatDto {

    private Integer felhasznaloId;
    private Integer esemenyId;
    private Integer zeneId;


    public static SzavazatDto factory(Szavazat entity) {

       return SzavazatDto.builder()
               .esemenyId(entity.getEsemeny().getId())
               .zeneId(entity.getZene().getId())

               .build();


    }

}
