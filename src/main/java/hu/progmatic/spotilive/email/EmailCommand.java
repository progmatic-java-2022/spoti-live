package hu.progmatic.spotilive.email;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailCommand {

    private String emailcim;
    @Builder.Default
    private String subject = "Meghívó";
    private String meghivoUuid;
}
