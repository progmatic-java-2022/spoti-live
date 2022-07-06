package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.zene.Zene;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;

@Builder
@Data
public class ZeneToEsemenyDto {
    private Integer id;
    private Esemeny esemeny;
    private Zene zene;
    @Builder.Default
    private Integer szavazat = 0;
}
