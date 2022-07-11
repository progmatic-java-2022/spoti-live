package hu.progmatic.spotilive.zene;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateZeneCommand {
    @NotBlank(message = "Nem lehet üres")
    private String cim;
    @NotBlank(message = "Nem lehet üres")
    private String eloado;
    @NotBlank(message = "Nem lehet üres")
    private Integer hosszMp;
    @NotNull
    private Integer zenekarId;
}
