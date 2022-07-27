package hu.progmatic.spotilive.felhasznalo;

import hu.progmatic.spotilive.demo.FakeAuthenticationHandler;
import hu.progmatic.spotilive.email.EmailCommand;
import hu.progmatic.spotilive.email.EmailException;
import hu.progmatic.spotilive.email.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class MeghivoService {

    @Autowired
    private MeghivoRepository meghivoRepository;
    @Autowired
    private FelhasznaloService felhasznaloService;
    @Autowired
    KreditRepository kreditRepository;

    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;


    public MeghivoDto meghivoLetrehozasa(Integer kreditMennyiseg) {
        Kredit kredit = kreditRepository.save(Kredit.builder().build());
        Meghivo meghivo = meghivoRepository.save(Meghivo.builder().uuid(UUID.randomUUID().toString()).build());
        meghivo.setKredit(kredit);
        kredit.setMeghivo(meghivo);
        kredit.setKreditMennyiseg(kreditMennyiseg);
        return MeghivoDto.factory(meghivo);
    }

    public void meghivoFelhasznalasa(MeghivoFelhasznalasaCommand command) {
        if (!meghivoFelVanHasznalva(command.getUuid())) {
            var felhasznalo = felhasznaloService.addGuest(MeghivoFelhasznalasaCommand.builder()
                    .uuid(command.getUuid())
                    .felhasznaloNev(command.getFelhasznaloNev())
                    .jelszo1(command.getJelszo1())
                    .jelszo2(command.getJelszo2())
                    .build());
            var meghivo = meghivoRepository.findMeghivoByUuidEquals(command.getUuid());
            meghivo.getKredit().setFelhasznalo(felhasznalo);
            felhasznalo.setKredit(meghivo.getKredit());
            felhasznalo.setMeghivo(meghivo);
            meghivo.setFelhasznalo(felhasznalo);

            try {
                new FakeAuthenticationHandler(authenticationConfiguration)
                        .loginAsUser(command.getFelhasznaloNev(),
                                command.getJelszo1());
            } catch (Exception e) {
                throw new RuntimeException("Sikertelen bejelentkezés.");
            }

        } else {
            throw new RuntimeException("Ezt már elhasználták!");
        }
    }

    public boolean meghivoFelVanHasznalva(String uuid) {
        return meghivoRepository.findMeghivoByUuidEquals(uuid).getFelhasznalo() != null;
    }

    public MeghivoDto findMeghivoByUUId(String uuid) {
        return MeghivoDto.factory(meghivoRepository.findMeghivoByUuidEquals(uuid));
    }

    public void deleteById(Integer id) {
        meghivoRepository.deleteById(id);
    }



    public List<MeghivoKikuldeseEredmenyDto> meghivokKikuldeseEmailben(MeghivoKredittelCommand meghivoKredittelCommand) {
        String[] mailTomb = meghivoKredittelCommand.getEmailCim().split("[\\s,;]+");
        List<MeghivoKikuldeseEredmenyDto> eredmenyDtoList = new ArrayList<>();
        for (String mailCim : mailTomb){
            var meghivo = meghivoLetrehozasa(meghivoKredittelCommand.getKreditekSzama());

            var emailCommand = EmailCommand.builder()
                    .emailcim(mailCim)
                    .meghivoUuid(meghivo.getUuid())
                    .build();
            var eredmeny = MeghivoKikuldeseEredmenyDto.builder()
                    .emailCim(mailCim)
                    .kreditekSzama(meghivoKredittelCommand.getKreditekSzama())
                    .sikeresKuldes(true)
                    .build();
            try{
                emailSenderService.emailKuldes(emailCommand);
            } catch (EmailException emailException){
                eredmeny.setSikeresKuldes(false);
            }

           eredmenyDtoList.add(eredmeny);
        }
    return eredmenyDtoList;
    }
}
