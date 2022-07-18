package hu.progmatic.spotilive.felhasznalo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class MeghivoService {

    @Autowired
    private MeghivoRepository meghivoRepository;
    @Autowired
    private FelhasznaloService felhasznaloService;


    public MeghivoDto meghivoLetrehozasa() {
        Meghivo meghivo = meghivoRepository.save(Meghivo.builder().uuid("testUUId").build());
        return MeghivoDto.factory(meghivo);
    }

    public void meghivoMentese(MeghivoFelhasznalasaCommand command) {
        if(!meghivoFelVanHasznalva(command.getUuid())) {
            var felhasznalo = felhasznaloService.addGuest(UjVendegCommand.builder()
                    .nev(command.getFelhasznaloNev())
                    .jelszo1(command.getJelszo1())
                    .jelszo2(command.getJelszo2())
                    .build());
            var meghivo = meghivoRepository.findMeghivoByUuidEquals(command.getUuid());

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

    public void deleteAll() {
        meghivoRepository.deleteAll();
    }
}
