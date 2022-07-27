package hu.progmatic.spotilive.email;

import hu.progmatic.spotilive.felhasznalo.FelhasznaloLetrehozasException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.util.Locale;


@Service
    @Log4j2
    public class EmailSenderService {

        @Autowired
        private JavaMailSender mailSender;

        public void emailKuldes(@Valid EmailCommand emailCommand) {
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper =
                        new MimeMessageHelper(mimeMessage, "utf-8");
                helper.setText(emailBodyBuilder(emailCommand.getEmailcim(),emailCommand.getMeghivoUuid()), true);
                helper.setTo(emailCommand.getEmailcim());
                helper.setSubject(emailCommand.getSubject());
                helper.setFrom("spotilive@gmail.com");
                mailSender.send(mimeMessage);
            } catch (Exception e) {
                log.warn("Email küldési hiba! " + e.getMessage());
                throw new EmailException("Email küldési hiba! Hibaüzenet: " + e.getMessage());
            }
        }

        public static String emailBodyBuilder(String felhasznalo, String link) {
            String body = "";
            body += String.format("<p>Kedves %s!</p>", felhasznalo.toUpperCase().charAt(0) + felhasznalo.split("@")[0].substring(1));
            body += "<p>Az alábbi linken tudod regisztrálni a meghívódat.</p>";
            body += String.format("<a href=\"%s\">Meghívó regisztrációs linkje</a>", "http://167.71.36.154/public/meghivo/" + link);
            body += "<p></p>";
            body += String.format("<a href=\"%s\">Meghívó regisztrációs linkje TESZTHEZ</a>", "http://localhost:8082/public/meghivo/" + link);
            body += "<p>Üdvözlettel:<br>";
            body += "Spotilive csapat</p>";
            return body;
        }
}
