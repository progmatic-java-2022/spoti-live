package hu.progmatic.spotilive.felhasznalo;

import hu.progmatic.spotilive.demo.DemoService;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MeghivoServiceTest {


    @Autowired
    private MeghivoService meghivoService;
    @Autowired
    private FelhasznaloService felhasznaloService;


    @Test
    void MeghivoHozzaadasTest() {
        var meghivo = meghivoService.meghivoLetrehozasa(5);
        var meghivo2 = meghivoService.meghivoLetrehozasa(5);
        assertNull(meghivo.getFelhasznalo());
        assertNotNull(meghivo.getUuid());
        assertThat(meghivo.getUuid()).isNotEqualTo(meghivo2.getUuid());
        assertNotNull(meghivo.getKredit().getId());

        meghivoService.deleteById(meghivo.getId());
        meghivoService.deleteById(meghivo2.getId());
    }


    @Test
    @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
    void FelhasznalasaTest() {
        var meghivo = meghivoService.meghivoLetrehozasa(5);
        assertNull(meghivo.getFelhasznalo());
        assertNotNull(meghivo.getUuid());
        meghivoService.meghivoFelhasznalasa(MeghivoFelhasznalasaCommand.builder()
                .uuid(meghivo.getUuid())
                .jelszo1("jelszo")
                .jelszo2("jelszo")
                .felhasznaloNev("guest12")
                .build()
        );
        var modositottMeghivo = meghivoService.findMeghivoByUUId(meghivo.getUuid());

        assertEquals("guest12",modositottMeghivo.getFelhasznalo().getNev());
        assertEquals(5, modositottMeghivo.getKredit().getKreditMennyiseg());
        assertEquals(5,modositottMeghivo.getFelhasznalo().getKredit().getKreditMennyiseg());


        var hibaUzenet = "Hiba";
        try {
            meghivoService.meghivoFelhasznalasa(MeghivoFelhasznalasaCommand.builder()
                    .uuid(meghivo.getUuid())
                    .jelszo1("jelszo")
                    .jelszo2("jelszo")
                    .felhasznaloNev("guest12")
                    .build()
            );
        } catch (Exception e) {
            hibaUzenet = e.getMessage();
        }
        assertEquals("Ezt már elhasználták!", hibaUzenet);

        var kiolvasottFelhasznalo = meghivoService.findMeghivoByUUId(meghivo.getUuid());
        felhasznaloService.delete(kiolvasottFelhasznalo.getFelhasznalo().getId());
    }
}
