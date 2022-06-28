package hu.progmatic.spotilive.zenekar;

import hu.progmatic.spotilive.MockMvcTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class ZenekarAdminControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser
    void oldalBetoltesTeszt() throws Exception{
        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/zenekarKarbantartas")
                .expectStatusIsOk()
                .expectContentContainsString("Demo zenekar");

    }
}