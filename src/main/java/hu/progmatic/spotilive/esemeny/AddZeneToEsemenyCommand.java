package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.zene.ZeneDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddZeneToEsemenyCommand {
    Integer esemenyId;
    Integer zeneId;
    ZeneDto zene;
}
