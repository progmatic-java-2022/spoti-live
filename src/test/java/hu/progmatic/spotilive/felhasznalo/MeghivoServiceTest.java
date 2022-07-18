package hu.progmatic.spotilive.felhasznalo;

import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MeghivoServiceTest {


    @Autowired
    private MeghivoService meghivoService;
    @Autowired
    private FelhasznaloService felhasznaloService;


    @Test
    void MeghivoHozzaadasTest() {

    }
}