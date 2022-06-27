package hu.progmatic.spotilive.esemeny;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EsemenyServiceTest {

    @Autowired
    private EsemenyService esemenyService;

    private List<EsemenyDto> esemenyList = new ArrayList<>();
    private EsemenyDto esemeny1;
    private EsemenyDto esemeny2;

    @BeforeEach
    void setUp() {
        esemenyList = new ArrayList<>(List.of(
                esemeny1 = esemenyService.createEsemeny(EsemenyDto.builder()
                        .nev("Tódor Szülcsi Napcsija")
                        .idoPont(LocalDateTime.parse("2022-06-27T15:30"))
                        .build()),
                esemeny2 = esemenyService.createEsemeny(EsemenyDto.builder()
                        .nev("Tivadar Névcsi Napcsijaj")
                        .idoPont(LocalDateTime.parse("2022-07-27T17:30"))
                        .build())
        ));
    }

    @Test
    void esemenyLetrehozasaTest() {
        EsemenyDto esemeny = EsemenyDto.builder()
                .nev("Tódor Napcsija")
                .idoPont(LocalDateTime.parse("2022-06-27T15:30"))
                .build();
        EsemenyDto letrehozott = esemenyService.createEsemeny(esemeny);

        assertNotNull(letrehozott.getId());
        assertEquals("Tódor Napcsija", letrehozott.getNev());
    }

    @Test
    void esemenyTorleseTest() {
        assertEquals(2, esemenyService.countAllEsemeny());

        esemenyService.deleteEsemeny(esemeny1.getId());

        assertEquals(1, esemenyService.countAllEsemeny());
    }

    @Test
    @Disabled
    void esemenyListazasTest() {
        assertThat(esemenyList)
                .hasSize(2)
                .extracting(EsemenyDto::getNev)
                .contains("Tódor Szülcsi Napcsija");

        esemenyService.deleteEsemeny(esemeny2.getId());

        assertEquals(1, esemenyList.size());
    }
}
