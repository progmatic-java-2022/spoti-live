package hu.progmatic.spotilive.esemeny;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SzavazatCommand {
    Integer esemenyId;
    Integer zeneId;
}
