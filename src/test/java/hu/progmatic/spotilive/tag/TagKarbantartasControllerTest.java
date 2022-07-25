package hu.progmatic.spotilive.tag;

import hu.progmatic.spotilive.MockMvcTestHelper;
import hu.progmatic.spotilive.demo.DemoService;
import hu.progmatic.spotilive.esemeny.CreateEsemenyCommand;
import hu.progmatic.spotilive.esemeny.EsemenyDto;
import hu.progmatic.spotilive.felhasznalo.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class TagKarbantartasControllerTest {
    @Autowired
    TagService tagService;


    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(UserType.Roles.TAG_KARBANTARTAS_ROLE)
    void oldalbetolteseTest() throws Exception {
        oldalBetolteseSzovegLatszik("Új Tag létrehozása");
    }

    private void oldalBetolteseSzovegLatszik(String szoveg) throws Exception {
        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/tag")
                .expectStatusIsOk()
                .expectContentContainsString(szoveg);
    }

    @Test
    @WithMockUser(roles = UserType.Roles.TAG_KARBANTARTAS_ROLE)
    void tagTorleseTest() throws Exception {
        var tag = tagService.createTag(TagDto.builder()
                .tagNev("Proba tag")
                .tagKategoria(TagKategoria.MUFAJ)
                .build()
        );
        oldalBetolteseSzovegLatszik("Proba tag");
        String deleteResource = "/tag/delete/" + tag.getId();
        MockMvcTestHelper
                .testRequest(mockMvc)
                .postRequest(deleteResource)
                .expectRedirectedToUrlPattern("/tag?**")
                .printRequest();
        oldalBetolteseSzovegNemLatszik("Proba tag");
    }

    private void oldalBetolteseSzovegNemLatszik(String szoveg) throws Exception {
        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/tag")
                .expectStatusIsOk()
                .expectContentNotContainsString(szoveg);
    }


}