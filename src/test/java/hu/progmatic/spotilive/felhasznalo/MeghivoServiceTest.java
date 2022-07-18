package hu.progmatic.spotilive.felhasznalo;

import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MeghivoServiceTest {


    @Autowired
    private MeghivoService meghivoService;
    @Autowired
    private FelhasznaloService felhasznaloService;


    @Test
    void MeghivoHozzaadasTest() {
        var meghivo = meghivoService.meghivoLetrehozasa();
        assertNull(meghivo.getFelhasznalo());
        assertEquals("testUUId", meghivo.getUuid());
    }

    @AfterEach
    void tearDown() {
        meghivoService.deleteAll();
    }

    @Test
    void FelhasznalasaTest() {
        var meghivo = meghivoService.meghivoLetrehozasa();
        assertNull(meghivo.getFelhasznalo());
        assertEquals("testUUId", meghivo.getUuid());
        meghivoService.meghivoMentese(MeghivoFelhasznalasaCommand.builder()
                        .uuid(meghivo.getUuid())
                        .jelszo1("jelszo")
                        .jelszo2("jelszo")
                        .felhasznaloNev("guest12")
                .build()
        );
        var modositottMeghivo = meghivoService.findMeghivoByUUId(meghivo.getUuid());
        assertEquals("guest12",modositottMeghivo.getFelhasznalo().getNev());
        var hibaUzenet = "Hiba";
        try {
            meghivoService.meghivoMentese(MeghivoFelhasznalasaCommand.builder()
                    .uuid(meghivo.getUuid())
                    .jelszo1("jelszo")
                    .jelszo2("jelszo")
                    .felhasznaloNev("guest12")
                    .build()
            );
        } catch (Exception e) {
            hibaUzenet = e.getMessage();
        }
        assertEquals("Ezt már elhasználták!",hibaUzenet);
    }

}
