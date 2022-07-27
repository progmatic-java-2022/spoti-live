package hu.progmatic.spotilive.felhasznalo;

import hu.progmatic.spotilive.demo.DemoService;
import hu.progmatic.spotilive.demo.FakeAuthenticationHandler;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MeghivoServiceTest {


    @Autowired
    private MeghivoService meghivoService;
    @Autowired
    private FelhasznaloService felhasznaloService;
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Test
    void meghivoHozzaadasTest() {
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
    void felhasznalasaTest() throws Exception {
        var fakeAuth =new FakeAuthenticationHandler(authenticationConfiguration);
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

        fakeAuth.loginAsUser(DemoService.ADMIN_FELHASZNALO,"adminpass");
        felhasznaloService.delete(kiolvasottFelhasznalo.getFelhasznalo().getId());
    }
}
