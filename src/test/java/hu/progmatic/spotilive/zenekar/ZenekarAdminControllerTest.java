package hu.progmatic.spotilive.zenekar;

import hu.progmatic.spotilive.MockMvcTestHelper;
import hu.progmatic.spotilive.demo.DemoService;
import hu.progmatic.spotilive.esemeny.EsemenyDto;
import hu.progmatic.spotilive.felhasznalo.UserType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ZenekarAdminControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ZenekarService zenekarService;


    @Test
    @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
    void oldalbetolteseTest() throws Exception {
        oldalBetolteseSzovegLatszik("Demo leírás");
    }

    private void oldalBetolteseSzovegLatszik(String szoveg) throws Exception {
        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/zenekarKarbantartas")
                .expectStatusIsOk()
                .expectContentContainsString(szoveg);
    }

    @Test
    @WithMockUser(roles = UserType.Roles.ZENEKAR_KEZELES_ROLE)
    void zenekarTorleseTest() throws Exception {
        var zenekar = zenekarService.createZenekar(ZenekarDto.builder()
                .nev("Teszt Zenekar törlésre")
                .email("email@email.hu")
                .build()
        );
        oldalBetolteseSzovegLatszik("Teszt Zenekar törlésre");
        String deleteResource = "/zenekar/delete/" + zenekar.getId();
        MockMvcTestHelper
                .testRequest(mockMvc)
                .postRequest(deleteResource)
                .expectRedirectedToUrlPattern("/zenekar?**")
                .printRequest();
        oldalBetolteseSzovegNemLatszik("Teszt Zenekar törlésre");
    }

    private void oldalBetolteseSzovegNemLatszik(String szoveg) throws Exception {
        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/zenekarKarbantartas")
                .expectStatusIsOk()
                .expectContentNotContainsString(szoveg);
    }


}