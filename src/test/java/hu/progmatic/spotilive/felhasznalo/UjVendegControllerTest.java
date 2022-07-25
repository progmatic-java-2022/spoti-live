package hu.progmatic.spotilive.felhasznalo;

import hu.progmatic.spotilive.MockMvcTestHelper;
import hu.progmatic.spotilive.esemeny.EsemenyDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static hu.progmatic.spotilive.demo.DemoService.ADMIN_FELHASZNALO;
import static hu.progmatic.spotilive.demo.DemoService.ZENEKAR_1_FELHASZNALO;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UjVendegControllerTest {

    @Autowired
    MeghivoService meghivoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(ADMIN_FELHASZNALO)
    void oldalbetolteseTest() throws Exception {
        oldalBetolteseSzovegLatszik("Vendég regisztráció");
    }

    private void oldalBetolteseSzovegLatszik(String szoveg) throws Exception {
        Integer kreditek = 0;
        MeghivoDto meghivo = meghivoService.meghivoLetrehozasa(kreditek);
        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/public/meghivo/" + meghivo.getUuid())
                .expectStatusIsOk()
                .expectContentContainsString(szoveg);
    }

}