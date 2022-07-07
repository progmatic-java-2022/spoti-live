package hu.progmatic.spotilive.esemeny;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddSzavazatCommand {
    private Integer esemenyId;
    private Integer zeneId;
}
