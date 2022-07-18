package hu.progmatic.spotilive.felhasznalo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UjVendegCommand {

    private String nev;
    private String jelszo;

}
