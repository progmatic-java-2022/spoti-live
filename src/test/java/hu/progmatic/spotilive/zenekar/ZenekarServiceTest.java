package hu.progmatic.spotilive.zenekar;

import hu.progmatic.spotilive.DemoServiceTestHelper;
import hu.progmatic.spotilive.felhasznalo.UserType;
import hu.progmatic.spotilive.zene.CreateZeneCommand;
import hu.progmatic.spotilive.zene.ZeneDto;
import hu.progmatic.spotilive.zene.ZeneService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ZenekarServiceTest {

    @Autowired
    ZenekarService zenekarService;

    @Autowired
    ZeneService zeneService;

    @Autowired
    private DemoServiceTestHelper demoServiceTestHelper;

    private Integer demoZenekarId;

    @BeforeEach
    void setUp() {
        demoZenekarId = demoServiceTestHelper.getdemoZeneKar1Id();
    }


    @Test
    @WithMockUser(roles = UserType.Roles.ZENEKAR_KEZELES_ROLE)
    void createZenekarAndExeptionTest() {
        ZenekarDto zenekarDto = ZenekarDto.builder()
                .nev("Teszt Zenekar")
                .email("teszt2@gmail.com")
                .telefonszam("0630-164-2922")
                .varos("Győr").build();

        ZenekarDto mentettDto = zenekarService.createZenekar(zenekarDto);

        assertNotNull(mentettDto.getId());
        assertEquals("0630-164-2922", mentettDto.getTelefonszam());
        assertEquals("Teszt Zenekar", mentettDto.getNev());
        assertEquals("Győr", mentettDto.getVaros());

        String nevHiba = "";
        try {
            zenekarService.createZenekar(zenekarDto);
        } catch (AddZenekarExeption e) {
            nevHiba = e.getMessage();
        }
        assertEquals("Zenekar már létezik ilyen névvel!", nevHiba);

        ZenekarDto emailMarLetezik = ZenekarDto.builder()
                .nev("Mas nevu ugyanolyan email")
                .email("teszt2@gmail.com")
                .varos("Győr")
                .build();

        String emailHiba = "";
        try {
            zenekarService.createZenekar(emailMarLetezik);
        } catch (AddZenekarExeption e) {
            emailHiba = e.getMessage();
        }
        assertEquals("Zenekar már létezik ilyen email címmel!", emailHiba);


        ZenekarDto telefonMarLetezik = ZenekarDto.builder()
                .nev("Mas nevu ugyanolyan email")
                .email("teszt10@gmail.com")
                .telefonszam("0630-164-2922")
                .varos("Győr")
                .build();

        String telefonszamHiba = "";
        try {
            zenekarService.createZenekar(telefonMarLetezik);
        } catch (AddZenekarExeption e){
            telefonszamHiba = e.getMessage();
        }
        assertEquals("Zenekar már létezik ilyen telefonszámmal'", telefonszamHiba);

        zenekarService.deleteById(mentettDto.getId());

        ZenekarDto uresTelefonszamteszt = ZenekarDto.builder()
                .nev("Notelefon")
                .email("tomi@gmail.com")
                .varos("Budapest")
                .build();

        ZenekarDto uresTelefonszamteszt2 = ZenekarDto.builder()
                .nev("Notelefonwatsoever")
                .email("tomika@gmail.com")
                .varos("Budapest")
                .build();

        ZenekarDto notelefon1 = zenekarService.createZenekar(uresTelefonszamteszt);
        ZenekarDto notelefon2 = zenekarService.createZenekar(uresTelefonszamteszt2);

        assertNull(notelefon2.getTelefonszam());
        assertNull(notelefon1.getTelefonszam());

        assertNotNull(notelefon1.getId());
        assertNotNull(notelefon2.getId());

        zenekarService.deleteById(notelefon1.getId());
        zenekarService.deleteById(notelefon2.getId());

    }

    @Nested
    @WithMockUser(roles = UserType.Roles.ZENEKAR_KEZELES_ROLE)
    public class LetezoZenekarralTest {
        ZenekarDto testZenekar;

        @AfterEach
        void tearDown() {
            zenekarService.deleteById(testZenekar.getId());
        }

        @BeforeEach
        void setUp() {
            testZenekar = ZenekarDto.builder().nev("Teszt Zenekar").email("teszt3@gmail.com").build();
            testZenekar = zenekarService.createZenekar(testZenekar);
        }

        @Test
        void deleteTest() {
            ZenekarDto deletezenekar = zenekarService.createZenekar(ZenekarDto.builder().nev("Delete zenekar").email("teszt4@gmail.com").varos("Budapest").build());
            List<ZenekarDto> lekertZenekarok = zenekarService.findAllDto();
            assertThat(lekertZenekarok)
                    .extracting(ZenekarDto::getNev)
                    .contains("Delete zenekar");
            zenekarService.deleteById(deletezenekar.getId());
            lekertZenekarok = zenekarService.findAllDto();
            assertThat(lekertZenekarok)
                    .extracting(ZenekarDto::getNev)
                    .doesNotContain("Delete zenekar");
        }

        @Test
        void getByNameTest() {
            assertEquals(testZenekar.getNev(), zenekarService.getByName("Teszt Zenekar").getNev());
        }

        @Test
        void editAndExeptionTest() {
            ZenekarDto dto = ZenekarDto.builder()
                    .id(testZenekar.getId())
                    .nev("Edited name")
                    .email("teszt5@gmail.com")
                    .varos("Budapest volt")
                    .telefonszam("0630-111-1111")
                    .build();
            var modositott = zenekarService.editZenekar(dto);
            assertEquals("Edited name", modositott.getNev());
            assertEquals("Budapest volt", modositott.getVaros());
            assertEquals("0630-111-1111", modositott.getTelefonszam());

            var zenekarEx = zenekarService.createZenekar(
                    ZenekarDto.builder()
                            .nev("Exeption")
                            .email("exeption@gmail.com")
                            .varos("Exeption city")
                            .telefonszam("0630-666-9999")
                            .build());

            ZenekarDto nevEx = ZenekarDto.builder()
                    .id(testZenekar.getId())
                    .nev("Exeption")
                    .email("teszt5@gmail.com")
                    .varos("Budapest volt")
                    .telefonszam("0630-111-1111")
                    .build();

            ZenekarDto emailEx = ZenekarDto.builder()
                    .id(testZenekar.getId())
                    .nev("Edited name")
                    .email("exeption@gmail.com")
                    .varos("Budapest volt")
                    .telefonszam("0630-111-1111")
                    .build();

            ZenekarDto telefonszamEx = ZenekarDto.builder()
                    .id(testZenekar.getId())
                    .nev("Edited name")
                    .email("teszt5@gmail.com")
                    .varos("Budapest volt")
                    .telefonszam("0630-666-9999")
                    .build();

            String nevE = "";
            try {
                zenekarService.editZenekar(nevEx);
            }catch (AddZenekarExeption e){
                nevE = e.getMessage();
            }
            assertEquals("Zenekar már létezik ilyen névvel!", nevE);

            String emailE = "";
            try {
                zenekarService.editZenekar(emailEx);
            }catch (AddZenekarExeption e){
                emailE = e.getMessage();
            }
            assertEquals("Zenekar már létezik ilyen email címmel!", emailE);

            String telefonszamE = "";
            try {
                zenekarService.editZenekar(telefonszamEx);
            }catch (AddZenekarExeption e){
                telefonszamE = e.getMessage();
            }
            assertEquals("Zenekar már létezik ilyen telefonszámmal'", telefonszamE);

            zenekarService.deleteById(zenekarEx.getId());



        }

        @Test
        void getByIdTest() {
            assertEquals(testZenekar.getId(), zenekarService.getById(testZenekar.getId()).getId());
        }

        @Nested
        class MeglevoZenekarokkalTest {
            ZeneDto zene1;
            ZeneDto zene2;


            @BeforeEach
            void setUp() {
                zene1 = zeneService.createZene(CreateZeneCommand.builder()
                        .cim("Egyes Zene Cime")
                        .eloado("Egyes zene Előadója")
                        .hosszMp(125)
                        .zenekarId(demoZenekarId)
                        .build());
                zene2 = zeneService.createZene(CreateZeneCommand.builder()
                        .cim("Kettes zene Cime")
                        .eloado("Kettes zene Előadója")
                        .hosszMp(124)
                        .zenekarId(demoZenekarId)
                        .build());
            }

            @AfterEach
            void tearDown() {
                zeneService.deleteZeneById(zene1.getId());
                zeneService.deleteZeneById(zene2.getId());
            }


        }
    }
}









