package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.zene.Zene;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SzavazatTracklistDto {
    private Integer id;
    private Esemeny esemeny;
    private Zene zene;
    @Builder.Default
    private Integer szavazat = 0;

    public static SzavazatTracklistDto factory(Szavazat entity){
        return SzavazatTracklistDto.builder()
                .esemeny(entity.getEsemeny())
                .zene(entity.getZene())
                .szavazat(entity.getSzavazat())
                .build();
    }
}
