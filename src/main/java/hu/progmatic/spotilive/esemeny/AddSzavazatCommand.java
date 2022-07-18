package hu.progmatic.spotilive.esemeny;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddSzavazatCommand {
    Integer esemenyId;
    Integer zeneId;
}
