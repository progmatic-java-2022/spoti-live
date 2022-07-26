package hu.progmatic.spotilive.email;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@Builder
public class EmailCommand {

    @Email
    private String emailcim;
    @Builder.Default
    private String subject = "Meghívó";
    private String meghivoUuid;
}
