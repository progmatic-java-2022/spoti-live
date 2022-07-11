package hu.progmatic.spotilive.zenekar;

import hu.progmatic.spotilive.felhasznalo.UserType;
import hu.progmatic.spotilive.zene.Zene;
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


    @Test
    @WithMockUser(roles = UserType.Roles.ZENEKAR_KEZELES_ROLE)
    void createZenekar() {
        ZenekarDto zenekarDto = ZenekarDto.builder().nev("Teszt Zenekar").email("teszt2@gmail.com").varos("Győr").build();
        ZenekarDto mentettDto = zenekarService.createZenekar(zenekarDto);

        assertNotNull(mentettDto.getId());
        assertEquals("Teszt Zenekar", mentettDto.getNev());
        assertEquals("Győr", mentettDto.getVaros());
        zenekarService.deleteById(mentettDto.getId());
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
        void editTest() {
            ZenekarDto dto = ZenekarDto.builder()
                    .id(testZenekar.getId())
                    .nev("Edited name")
                    .email("teszt5@gmail.com")
                    .varos("Budapest volt")
                    .build();
            var modositott = zenekarService.editZenekar(dto);
            assertEquals("Edited name", modositott.getNev());
            assertEquals("Budapest volt", modositott.getVaros());

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
                zene1 = zeneService.createZene(ZeneDto.builder()
                        .cim("Egyes Zene Cime")
                        .eloado("Egyes zene Előadója")
                        .hosszMp(125)
                        .build());
                zene2 = zeneService.createZene(ZeneDto.builder()
                        .cim("Kettes zene Cime")
                        .eloado("Kettes zene Előadója")
                        .hosszMp(124)
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









