package hu.progmatic.spotilive.esemeny;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class EsemenyServiceTest {

    @Autowired
    private EsemenyService esemenyService;

    @BeforeEach
    void setUp() {
        esemenyService.deleteAllEsemeny();
    }

    @Test
    void esemenyLetrehozasaTest() {
        EsemenyDto esemeny = EsemenyDto.builder()
                .nev("Tódor Születésnapja")
                .idoPont(LocalDateTime.parse("2022-06-27T15:30"))
                .build();
        EsemenyDto letrehozott = esemenyService.createEsemeny(esemeny);

        assertNotNull(letrehozott.getId());
        assertEquals("Tódor Születésnapja", letrehozott.getNev());
    }

    @Test
    void esemenyTorleseTest() {
        EsemenyDto esemeny = EsemenyDto.builder()
                .nev("Törlendő esemény")
                .idoPont(LocalDateTime.parse("2022-06-27T15:30"))
                .build();
        EsemenyDto letrehozott = esemenyService.createEsemeny(esemeny);
        assertEquals("Törlendő esemény",letrehozott.getNev());
        esemenyService.deleteEsemeny(letrehozott.getId());
        assertEquals(0, esemenyService.countAllEsemeny());
    }

    @Nested
    class testLetezoEsemenyekkel {
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

        @Test
        void esemenyListazasTest() {

            List<EsemenyDto> esemenyList = esemenyService.findAllEsemeny();
            assertThat(esemenyList)
                    .hasSize(2)
                    .extracting(EsemenyDto::getNev)
                    .contains("Tódor Születésnapja");

            esemenyService.deleteEsemeny(esemeny2.getId());
            esemenyList = esemenyService.findAllEsemeny();

            assertEquals(1, esemenyList.size());
        }
        @Test
        void esemenySzerkeszteseTest() {
         assertEquals("Tódor Születésnapja",esemeny1.getNev());
         EsemenyDto modositott = EsemenyDto.builder()
                 .nev("modositott név")
                 .idoPont(LocalDateTime.parse("2022-06-27T15:30"))
                 .build();
         esemenyService.udpate(modositott,esemeny1.getId());
         EsemenyDto updatelt = esemenyService.getById(esemeny1.getId());
         assertEquals("modositott név", updatelt.getNev());
        }
    }
}