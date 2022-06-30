package hu.progmatic.spotilive.zene;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagEditCommand {

    private String tagNev;
    private Integer tagId;
    private Integer zeneId;
}
