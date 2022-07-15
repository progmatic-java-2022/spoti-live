package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.DemoServiceTestHelper;
import hu.progmatic.spotilive.MockMvcTestHelper;
import hu.progmatic.spotilive.demo.DemoService;
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

import static hu.progmatic.spotilive.demo.DemoService.ADMIN_FELHASZNALO;
import static hu.progmatic.spotilive.demo.DemoService.ZENEKAR_1_FELHASZNALO;


@SpringBootTest
@AutoConfigureMockMvc
class EsemenyControllerTest {


  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private EsemenyService esemenyService;

  @Autowired
  private DemoServiceTestHelper demoServiceTestHelper;

  private Integer demoZenekarId;
  private Integer demoEsemenyId;

  @BeforeEach
  void setUp() {
    demoZenekarId = demoServiceTestHelper.getdemoZeneKar1Id();
    demoEsemenyId = demoServiceTestHelper.getZenekar1demoEsemenyId();
  }

  @Test
  @WithMockUser
  void oldalbetolteseTest() throws Exception {
    oldalBetolteseSzovegLatszik("Demo esemény");
  }

  private void oldalBetolteseSzovegLatszik(String szoveg) throws Exception {
    MockMvcTestHelper
            .testRequest(mockMvc)
            .getRequest("/esemeny")
            .expectStatusIsOk()
            .expectContentContainsString(szoveg);
  }

  @Test
  @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
  void esemenyTorleseTest() throws Exception {
    var esemeny = esemenyService.createEsemeny(CreateEsemenyCommand.builder()
            .nev("Esküvő")
            .idoPont(LocalDateTime.parse("2000-12-12T14:21"))
            .zenekarId(demoZenekarId)
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

  private void oldalBetolteseSzovegNemLatszik(String szoveg) throws Exception {
    MockMvcTestHelper
            .testRequest(mockMvc)
            .getRequest("/esemeny")
            .expectStatusIsOk()
            .expectContentNotContainsString(szoveg);
  }

  @Test
  @WithUserDetails(ZENEKAR_1_FELHASZNALO)
  @DisplayName("Zenekarral belépve megjelenik a zenekar hozzáadása szöveg")
  void zenekarHozzaadasaMegjelenik() throws Exception {
    MockMvcTestHelper
            .testRequest(mockMvc)
            .getRequest("/esemeny")
            .expectStatusIsOk()
            .expectContentContainsString("Esemenyek")
            .expectContentContainsString("Új esemény hozzáadása");
  }

  @Test
  @WithUserDetails("guest")
  @DisplayName("Guest-el belépve nem jelenik meg a zenekar hozzáadása szöveg")
  void zenekarHozzaadasaNemJelenikMeg() throws Exception {
    MockMvcTestHelper
            .testRequest(mockMvc)
            .getRequest("/esemeny")
            .expectStatusIsOk()
            .expectContentContainsString("Esemenyek")
            .expectContentNotContainsString("Új esemény hozzáadása");
  }

  @Test
  @WithUserDetails(ZENEKAR_1_FELHASZNALO)
  @DisplayName("Esemény létrehozásakor megjelennek a hibaüzenetek, ha nincs rendesen kitöltve")
  void esemenyLetrehozasHibauzenetek() throws Exception {
    MockMvcTestHelper
            .testRequest(mockMvc)
            .postRequestBuilder("/esemeny")
            .addFormParameter("zenekarId", "" + demoZenekarId)
            .addFormParameter("nev", "")
            .addFormParameter("idoPont", "")
            .buildRequest()
            .expectStatusIsOk()
            .expectContentContainsString("Nem lehet üres")
            .expectContentContainsString("Meg kell adni időpontot!");
  }

  @Test
  @WithUserDetails(ZENEKAR_1_FELHASZNALO)
  @DisplayName("Esemény létrehozásakor létrejön az esemény")
  void esemenyLetrehozas() throws Exception {
    MockMvcTestHelper
            .testRequest(mockMvc)
            .postRequestBuilder("/esemeny")
            .addFormParameter("zenekarId", "" + demoZenekarId)
            .addFormParameter("nev", "Esemény létrehozása teszt esemény")
            .addFormParameter("idoPont", "2222-07-05T13:45")
            .buildRequest()
            .expectRedirectedToUrlPattern("/esemeny?**")
            .expectContentNotContainsString("Nem lehet üres")
            .expectContentNotContainsString("Meg kell adni időpontot!");

    Integer esemenyId = esemenyService.findAllEsemeny()
            .stream()
            .filter(esemeny -> esemeny.getNev().equals("Esemény létrehozása teszt esemény"))
            .map(EsemenyDto::getId)
            .findFirst()
            .orElseThrow();
    torles(esemenyId);
  }

  private void torles(Integer esemenyId) throws Exception {
    MockMvcTestHelper
        .testRequest(mockMvc)
            .postRequest("/esemeny/delete/" + esemenyId)
                .expectRedirectedToUrlPattern("/esemeny?**");
  }

  @Test
  @WithUserDetails(ADMIN_FELHASZNALO)
  @DisplayName("Esemény módosítás után elmentődik")
  void esemenyMenteseTest() throws Exception {
    MockMvcTestHelper
            .testRequest(mockMvc)
            .postRequestBuilder("/esemeny")
            .addFormParameter("zenekarId", "" + demoZenekarId)
            .addFormParameter("nev", "Esemény mentése teszt esemény")
            .addFormParameter("idoPont", "2222-07-05T13:45")
            .buildRequest()
            .expectRedirectedToUrlPattern("/esemeny?**")
            .expectContentNotContainsString("Nem lehet üres")
            .expectContentNotContainsString("Meg kell adni időpontot!");

    Integer esemenyId = esemenyService.findAllEsemeny()
            .stream()
            .filter(esemeny -> esemeny.getNev().equals("Esemény mentése teszt esemény"))
            .map(EsemenyDto::getId)
            .findFirst()
            .orElseThrow();

    MockMvcTestHelper
            .testRequest(mockMvc)
            .getRequest("/esemeny/" + esemenyId)
            .expectStatusIsOk()
            .expectContentContainsString("Esemény mentése teszt esemény")
            .expectContentContainsString("2222-07-05T13:45");

    MockMvcTestHelper
            .testRequest(mockMvc)
            .postRequestBuilder("/esemeny/" + esemenyId)
            .addFormParameter("zenekarId", "" + demoZenekarId)
            .addFormParameter("nev", "Esemény mentése teszt esemény módosított")
            .addFormParameter("idoPont", "2222-07-05T13:45")
            .buildRequest()
            .expectRedirectedToUrlPattern("/esemeny?**");


    MockMvcTestHelper
            .testRequest(mockMvc)
            .getRequest("/esemeny")
            .expectStatusIsOk()
            .expectContentContainsString("Esemény mentése teszt esemény módosított");

    torles(esemenyId);
  }

  @Test
  @WithUserDetails("guest")
  @DisplayName("Esemény modositás nem látszik guestként")
  void esemenyModositasGuest() throws Exception {
    MockMvcTestHelper
            .testRequest(mockMvc)
            .getRequest("/esemeny")
            .expectStatusIsOk()
            .printRequest()
            .expectContentContainsString("Esemenyek")
            .expectContentNotContainsString("módosít");
  }

  @Test
  @WithUserDetails("guest")
  @DisplayName("Esemény törlés nem látszik guest-ként")
  void esemenyTorleseGuest() throws Exception {
    MockMvcTestHelper
            .testRequest(mockMvc)
            .postRequest("/esemeny")
            .expectStatusIsOk()
            .printRequest()
            .expectContentContainsString("Esemenyek")
            .expectContentNotContainsString("törlés");

  }

  @Test
  @DisplayName("Esemény szerkesztésekor megjelennek az adatok")
  @WithUserDetails(ZENEKAR_1_FELHASZNALO)
  void esemenySzerkesztes() throws Exception {
    MockMvcTestHelper
            .testRequest(mockMvc)
            .getRequest("/esemeny/" + demoEsemenyId)
            .expectStatusIsOk()
            .printRequest()
            .expectContentContainsString("Demo zenekar")
            .expectContentContainsString("Demo esemény");
  }
}