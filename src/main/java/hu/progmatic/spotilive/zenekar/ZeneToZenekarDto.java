package hu.progmatic.spotilive.zenekar;


import hu.progmatic.spotilive.zene.Zene;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Builder
@Data
public class ZeneToZenekarDto {

    private Integer id;
    private Zenekar zenekar;
    private Zene zene;

    public static ZeneToZenekarDto factory(ZeneToZenekar entity){
        return ZeneToZenekarDto.builder()
                .zenekar(entity.getZenekar())
                .zene(entity.getZene())
                .build();
    }
}
