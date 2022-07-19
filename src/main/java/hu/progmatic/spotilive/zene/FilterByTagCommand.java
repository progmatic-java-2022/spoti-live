package hu.progmatic.spotilive.zene;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilterByTagCommand {
    Integer zenekarId;
    Integer tagId;
}
