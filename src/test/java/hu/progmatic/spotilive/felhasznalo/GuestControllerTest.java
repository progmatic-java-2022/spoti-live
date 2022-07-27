package hu.progmatic.spotilive.felhasznalo;

import hu.progmatic.spotilive.MockMvcTestHelper;
import hu.progmatic.spotilive.demo.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class GuestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(DemoService.GUEST_FELHASZNALO)
    void guestFeluletTest() throws Exception {
        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/guestindex")
                .expectContentContainsString("Tracklist")
                .expectContentContainsString("Hello guest!");
    }

}