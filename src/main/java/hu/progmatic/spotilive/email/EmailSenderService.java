package hu.progmatic.spotilive.email;

import hu.progmatic.spotilive.felhasznalo.FelhasznaloLetrehozasException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;


    @Service
    @Log4j2
    public class EmailSenderService {

        @Autowired
        private JavaMailSender mailSender;

        public void emailKuldes(String to, String subject, String body) {
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper =
                        new MimeMessageHelper(mimeMessage, "utf-8");
                helper.setText(body, true);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setFrom("carnivora.project@gmail.com");
                mailSender.send(mimeMessage);
            } catch (Exception e) {
                log.warn("Email küldési hiba! " + e.getMessage());
                throw new FelhasznaloLetrehozasException("Email küldési hiba! Sikertelen regisztráció! Próbálkozzon később!");
            }
        }

        public static String emailBodyBuilder(String felhasznalo, String link) {
            String body = "";
            body += String.format("<p>Kedves %s!</p>", felhasznalo);
            body += "<p>Az alábbi linken tudod megerősíteni a regisztrációt.</p>";
            body += String.format("<a href=\"%s\">Megerősítő link</a>", link);
            body += "<p>Üdvözlettel:<br>";
            body += "Carnivora csapat</p>";
            return body;
        }
}
