package hu.progmatic.spotilive.zenekar;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddZeneToZenekarCommand {

    private Integer zenekarId;
    private Integer zeneId;

}
