package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.felhasznalo.UserType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;


import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser(roles = UserType.Roles.ESEMENY_KEZELES_ROLE)
class EsemenyServiceTest {
    @Autowired
    private EsemenyService esemenyService;


    @Test
    void esemenyLetrehozasaTest() {
        EsemenyDto esemeny = EsemenyDto.builder()
                .nev("Tódor Születésnapja")
                .idoPont(LocalDateTime.parse("2022-06-27T15:30"))
                .build();
        EsemenyDto letrehozott = esemenyService.createEsemeny(esemeny);

        assertNotNull(letrehozott.getId());
        assertEquals("Tódor Születésnapja", letrehozott.getNev());

        esemenyService.deleteEsemeny(letrehozott.getId());
        assertEquals(1, esemenyService.countAllEsemeny());

        var esemenyek = esemenyService.findAllEsemeny();
        assertThat(esemenyek).extracting(EsemenyDto::getNev)
                .contains("Demo esemény");
    }


    @Test
    void esemenyTorleseTest() {
        EsemenyDto esemeny = EsemenyDto.builder()
                .nev("Törlendő esemény")
                .idoPont(LocalDateTime.parse("2022-06-27T15:30"))
                .build();
        EsemenyDto letrehozott = esemenyService.createEsemeny(esemeny);
        assertEquals("Törlendő esemény", letrehozott.getNev());
        assertNotNull(letrehozott.getId());

        esemenyService.deleteEsemeny(letrehozott.getId());
        assertEquals(1, esemenyService.countAllEsemeny());
        var esemenyek = esemenyService.findAllEsemeny();
        assertThat(esemenyek).extracting(EsemenyDto::getNev)
                .contains("Demo esemény");

    }

    @Nested
    class TesztLetezoEsemenyekkel {
        EsemenyDto esemeny1;
        EsemenyDto esemeny2;


        @BeforeEach
        void setUp() {
            esemeny1 = esemenyService.createEsemeny(EsemenyDto.builder()
                    .nev("Tódor Születésnapja")
                    .idoPont(LocalDateTime.parse("2022-05-27T15:30"))
                    .build());
            esemeny2 = esemenyService.createEsemeny(EsemenyDto.builder()
                    .nev("Tivadar Névnapja")
                    .idoPont(LocalDateTime.parse("2002-10-19T10:45"))
                    .build());

        }

        @AfterEach
        void tearDown() {
            esemenyService.deleteEsemeny(esemeny1.getId());
            esemenyService.deleteEsemeny(esemeny2.getId());
        }

        @Test
        void esemenyListazasTest() {
            var esemenyList = esemenyService.findAllEsemeny();
            assertThat(esemenyList)
                    .hasSize(3)
                    .extracting(EsemenyDto::getNev)
                    .contains("Tódor Születésnapja", "Demo esemény");
        }

        @Test
        void esemenySzerkeszteseTest() {
            EsemenyDto modositando;

            modositando = esemenyService.createEsemeny(EsemenyDto.builder()
                    .nev(esemeny1.getNev())
                    .idoPont(esemeny1.getIdoPont())
                    .build());


            EsemenyDto modosito = EsemenyDto.builder()
                    .nev("modositott név")
                    .idoPont(LocalDateTime.parse("2022-06-27T15:30"))
                    .build();
            esemenyService.udpate(modosito, modositando.getId());
            EsemenyDto updatelt = esemenyService.getById(modositando.getId());
            assertEquals("modositott név", updatelt.getNev());

            esemenyService.deleteEsemeny(updatelt.getId());
            assertThat(esemenyService.findAllEsemeny())
                    .hasSize(3);
        }
    }

}
