package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.DemoServiceTestHelper;
import hu.progmatic.spotilive.MockMvcTestHelper;
import hu.progmatic.spotilive.demo.DemoService;
import hu.progmatic.spotilive.tag.TagDto;
import hu.progmatic.spotilive.tag.TagKategoria;
import hu.progmatic.spotilive.tag.TagService;
import org.junit.jupiter.api.Disabled;
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
    @Autowired
    private ZeneService zeneService;
    @Autowired
    private TagService tagService;


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

    @Test
    @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
    void tagekModalTest() throws Exception {
        ZeneDto zene = demoServiceTestHelper.getDemoZenekar1ZeneDto();
        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/tagek/zene/" + zene.getId())
                .expectStatusIsOk()
                .expectContentContainsString(zene.getTagStringList().get(0));

        var tag = tagService.createTag(TagDto.builder()
                .tagNev("ControllerTeszt")
                .tagKategoria(TagKategoria.MUFAJ)
                .build());

        MockMvcTestHelper
                .testRequest(mockMvc)
                .postRequest("/zenekarbantartas/zene/" + zene.getId() + "/tag/" + tag.getId() + "/add")
                .expectRedirectedToUrlPattern("/tagek/zene/?**");

        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/zene")
                .expectContentContainsString("ControllerTeszt");

        MockMvcTestHelper
                .testRequest(mockMvc)
                .postRequest("/zenekarbantartas/zene/" + zene.getId() + "/tag/" + tag.getId() + "/remove")
                .expectRedirectedToUrlPattern("/tagek/zene/?**");

        tagService.deleteTagById(tag.getId());
    }

    @Test
    @WithUserDetails(DemoService.ZENEKAR_1_FELHASZNALO)
    void hibaUzenetZeneTest() throws Exception {
        Integer id = demoServiceTestHelper.getdemoZeneKar1Id();
        MockMvcTestHelper
                .testRequest(mockMvc)
                .postRequestBuilder("/zenekarbantartas/zene")
                .addFormParameter("zenekarId", "" + id)
                .addFormParameter("hosszMp", "3")
                .addFormParameter("eloado", "")
                .addFormParameter("cim", "")
                .buildRequest()
                .expectContentContainsString("Nem lehet üres")
                .expectContentContainsString("A zene hossza minimum 120 másodperc!");
    }

    @Test
    @WithUserDetails(DemoService.ZENEKAR_1_FELHASZNALO)
    void addZeneAndDeleteZeneTest() throws Exception {
        Integer id = demoServiceTestHelper.getdemoZeneKar1Id();
        MockMvcTestHelper
                .testRequest(mockMvc)
                .postRequestBuilder("/zenekarbantartas/zene")
                .addFormParameter("zenekarId", "" + id)
                .addFormParameter("hosszMp", "320")
                .addFormParameter("eloado", "Jokis eloado")
                .addFormParameter("cim", "Jokis zene")
                .buildRequest()
                .expectRedirectedToUrlPattern("/zene?**")
                .expectContentNotContainsString("Nem lehet üres");

        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/zene")
                .expectStatusIsOk()
                .expectContentContainsString("Jokis eloado")
                .expectContentContainsString("Jokis zene");

        var zene = zeneService.getZeneByNev("Jokis zene");

        MockMvcTestHelper
                .testRequest(mockMvc)
                .postRequest("/zenekarbantartas/zene/delete/" + zene.getId())
                .expectRedirectedToUrlPattern("/zene?**");

        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/zene")
                .expectStatusIsOk()
                .expectContentNotContainsString("Jokis zene");
    }

    @Test
    @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
    void editZeneTest() throws Exception{
        var zene = demoServiceTestHelper.getDemoZenekar1ZeneDto();

        MockMvcTestHelper
                .testRequest(mockMvc)
                .postRequestBuilder("/zenekarbantartas/zene/" + zene.getId())
                .addFormParameter("id" ,"" + zene.getId())
                .addFormParameter("cim", "ControllerTeszt")
                .addFormParameter("eloado", "Modositott")
                .addFormParameter("hosszMp", "300")
                .buildRequest()
                .expectRedirectedToUrlPattern("/zene?**");

        MockMvcTestHelper
                .testRequest(mockMvc)
                .getRequest("/zene")
                .expectStatusIsOk()
                .expectContentContainsString("ControllerTeszt")
                .expectContentContainsString("Modositott")
                .expectContentContainsString("300");

        MockMvcTestHelper
                .testRequest(mockMvc)
                .postRequestBuilder("/zenekarbantartas/zene/" + zene.getId())
                .addFormParameter("id" ,"" + zene.getId())
                .addFormParameter("cim", zene.getCim())
                .addFormParameter("eloado", zene.getEloado())
                .addFormParameter("hosszMp", "" + zene.getHosszMp())
                .buildRequest()
                .expectRedirectedToUrlPattern("/zene?**");

    }
}