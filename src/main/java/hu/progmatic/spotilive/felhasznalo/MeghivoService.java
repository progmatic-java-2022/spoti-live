package hu.progmatic.spotilive.felhasznalo;

import org.springframework.stereotype.Service;

@Service
public class MeghivoService {
    public MeghivoDto meghivoLetrehozasa() {
        return MeghivoDto.builder().uuid("testUUId").build();
    }

    public void meghivoFelhasznalasa(MeghivoFelhasznalasaCommand command) {

    }
    public boolean meghivoFelVanHasznalva(String uuid) {
        return false;
    }
}
