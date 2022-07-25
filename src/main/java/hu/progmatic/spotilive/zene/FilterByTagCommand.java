package hu.progmatic.spotilive.zene;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FilterByTagCommand {
    Integer esemenyId;
    List<String> tagLista;
}
