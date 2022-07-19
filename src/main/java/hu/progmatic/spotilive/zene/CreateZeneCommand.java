package hu.progmatic.spotilive.zene;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateZeneCommand {
    @NotBlank(message = "Nem lehet üres")
    private String cim;
    @NotBlank(message = "Nem lehet üres")
    private String eloado;
    @NotNull(message = "Nem lehet üres")
    @Min(message = "A zene hossza minimum 120 másodperc!", value = 120)
    private Integer hosszMp;
    private Integer zenekarId;
}
