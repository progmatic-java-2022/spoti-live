package hu.progmatic.spotilive.felhasznalo;

import hu.progmatic.spotilive.MockMvcTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class LoginFormTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Jogosultság nélkül átirányít a login oldalra")
  void root() throws Exception {
    MockMvcTestHelper
        .testRequest(mockMvc)
        .getRequest("/")
        .expectRedirectedToUrlPattern("**/login");
  }

  @Test
  @DisplayName("Jogosultság nélkül hozzáférünk a login képernyőhöz")
  void loginPage() throws Exception {
    MockMvcTestHelper
        .testRequest(mockMvc)
        .getRequest("/login")
        .expectStatusIsOk();
  }

  @Test
  @DisplayName("Sikeres bejelentkezéskor átirányít a kezdőlapra")
  void loginPost() throws Exception {
    MockMvcTestHelper
        .testRequest(mockMvc)
        .postRequestBuilder("/login")
        .addFormParameter("username", "admin")
        .addFormParameter("password", "adminpass")
        .buildRequest()
        .expectStatusIsOk()
        .expectForwardedToUrl("/");
  }

  @Nested
  @DisplayName("Bejelentkezett felhasználóval")
  @WithMockUser(username = "tesztfelhasznalonev")
  class BejelentkezveTest {

    @Test
    @DisplayName("Bejelentkezve hozzáférünk a kezdőlaphoz")
    void root() throws Exception {
      MockMvcTestHelper
          .testRequest(mockMvc)
          .getRequest("/")
          .expectStatusIsOk()
          .expectContentContainsString("tesztfelhasznalonev");
    }
  }
}
