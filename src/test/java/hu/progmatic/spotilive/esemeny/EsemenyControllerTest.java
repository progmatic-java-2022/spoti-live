package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.MockMvcTestHelper;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class EsemenyControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EsemenyService esemenyService;

    @Test
    @WithMockUser
    void oldalbetolteseTest() throws Exception{
        oldalBetolteseSzovegLatszik("Demo esemény");
    }

    private void oldalBetolteseSzovegLatszik(String szoveg) throws Exception{
        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/esemeny")
                .expectStatusIsOk()
                .expectContentContainsString(szoveg);
    }

    @Test
    @WithMockUser
    void esemenyTorleseTest() throws Exception {
        var esemeny = esemenyService.createEsemeny(EsemenyDto.builder()
                .nev("Esküvő")
                .idoPont(LocalDateTime.parse("2000-12-12T14:21"))
                .build()
        );
        oldalBetolteseSzovegLatszik("Esküvő");
        String deleteResource = "/esemeny/delete/" + esemeny.getId();
        MockMvcTestHelper
                .testRequest(mockMvc)
                .postRequest(deleteResource)
                .expectRedirectedToUrlPattern("/esemeny?**")
                .printRequest();
        oldalBetolteseSzovegNemLatszik("Esküvő");
    }

    private void oldalBetolteseSzovegNemLatszik(String szoveg) throws Exception{
        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/esemeny")
                .expectStatusIsOk()
                .expectContentNotContainsString(szoveg);
    }


}