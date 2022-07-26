package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.DemoServiceTestHelper;
import hu.progmatic.spotilive.MockMvcTestHelper;
import hu.progmatic.spotilive.demo.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ZeneControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DemoServiceTestHelper demoServiceTestHelper;

    @Test
    @WithUserDetails(DemoService.ZENEKAR_1_FELHASZNALO)
    void csakSajatZenekJelennekMegZenekar1() throws Exception {
        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/zene")
                .expectStatusIsOk()
                .expectContentContainsString(demoServiceTestHelper.getDemoZenekar1ZeneDto().getCim())
                .expectContentNotContainsString(demoServiceTestHelper.getDemoZenekar2ZeneDto().getCim());
    }

    @Test
    @WithUserDetails(DemoService.ZENEKAR_2_FELHASZNALO)
    void csakSajatZenekJelennekMegZenekar2() throws Exception {
        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/zene")
                .expectStatusIsOk()
                .expectContentNotContainsString(demoServiceTestHelper.getDemoZenekar1ZeneDto().getCim())
                .expectContentContainsString(demoServiceTestHelper.getDemoZenekar2ZeneDto().getCim());
    }

    @Test
    @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
    void csakSajatZenekJelennekMegAdmin() throws Exception {
        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/zene")
                .expectStatusIsOk()
                .expectContentContainsString(demoServiceTestHelper.getDemoZenekar1ZeneDto().getCim())
                .expectContentContainsString(demoServiceTestHelper.getDemoZenekar2ZeneDto().getCim());
    }

}