package hu.progmatic.spotilive.email;

import com.dumbster.smtp.SimpleSmtpServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmailSenderServiceTest {

  @Value("${spring.mail.port}")
  private Integer emailPort;

  @Autowired
  private EmailSenderService emailSenderService;

  @Test
  void emailKuldes() throws IOException {
    try (SimpleSmtpServer dumbster = SimpleSmtpServer.start(emailPort)) {

      emailSenderService.emailKuldes(EmailCommand.builder().emailcim("to@here.com").subject("Test").meghivoUuid("Test Body").build());

      var emails = dumbster.getReceivedEmails();
      assertThat(emails).hasSize(1);
      var email = emails.get(0);
      assertThat(email.getHeaderValue("Subject")).isEqualTo("Test");
      assertThat(email.getBody()).contains("Test Body");
      assertThat(email.getHeaderValue("To")).isEqualTo("to@here.com");
    }

  }
}