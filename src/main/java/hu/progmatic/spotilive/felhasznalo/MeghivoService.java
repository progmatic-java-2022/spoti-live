package hu.progmatic.spotilive.felhasznalo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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


    public MeghivoDto meghivoLetrehozasa() {
        Kredit kredit = kreditRepository.save(Kredit.builder().build());
        Meghivo meghivo = meghivoRepository.save(Meghivo.builder().uuid(UUID.randomUUID().toString()).build());
        meghivo.setKredit(kredit);
        kredit.setMeghivo(meghivo);
        return MeghivoDto.factory(meghivo);
    }

    public void meghivoFelhasznalasa(MeghivoFelhasznalasaCommand command) {
        if(!meghivoFelVanHasznalva(command.getUuid())) {
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
}
