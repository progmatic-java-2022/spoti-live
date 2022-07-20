package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.DemoServiceTestHelper;
import hu.progmatic.spotilive.demo.DemoService;
import hu.progmatic.spotilive.zene.CreateZeneCommand;
import hu.progmatic.spotilive.zene.ZeneDto;
import hu.progmatic.spotilive.zene.ZeneService;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SzavazatServiceTest {
    @Autowired
    ZeneService zeneService;
    @Autowired
    private EsemenyService esemenyService;
    @Autowired
    private DemoServiceTestHelper demoServiceTestHelper;
    private Integer demoZenekar1Id;
    @Autowired
    private SzavazatService szavazatService;
    ZeneDto zene1;
    ZeneDto zene2;
    EsemenyDto esemeny1;
    EsemenyDto esemeny2;

    @Nested
    @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
    class EsemenyZenekkelTest {
        @BeforeEach
        void setUp() {
            demoZenekar1Id = demoServiceTestHelper.getdemoZeneKar1Id();
            esemeny1 = esemenyService.createEsemeny(CreateEsemenyCommand.builder()
                    .nev("Tódor Születésnapja")
                    .idoPont(LocalDateTime.parse("2022-05-27T15:30"))
                    .zenekarId(demoZenekar1Id)
                    .build());
            esemeny2 = esemenyService.createEsemeny(CreateEsemenyCommand.builder()
                    .nev("Tivadar Névnapja")
                    .idoPont(LocalDateTime.parse("2002-10-19T10:45"))
                    .zenekarId(demoZenekar1Id)
                    .build());
            zene1 = zeneService.createZene(CreateZeneCommand
                    .builder()
                    .cim("Teszt zene1")
                    .hosszMp(123)
                    .eloado("Teszt eloado1")
                    .zenekarId(demoZenekar1Id)
                    .build());
            zene2 = zeneService.createZene(CreateZeneCommand
                    .builder()
                    .eloado("Teszt eloado2")
                    .hosszMp(123)
                    .cim("Teszt zene2")
                    .zenekarId(demoZenekar1Id)
                    .build());

            esemenyService.addSzavazat(SzavazatCommand
                    .builder()
                    .zeneId(zene1.getId())
                    .esemenyId(esemeny1.getId())
                    .build());

            esemenyService.addSzavazat(SzavazatCommand
                    .builder()
                    .esemenyId(esemeny1.getId())
                    .zeneId(zene2.getId())
                    .build());
            esemenyService.addSzavazat(SzavazatCommand.builder()
                    .esemenyId(esemeny1.getId())
                    .zeneId(zene1.getId())
                    .build());

            esemenyService.addSzavazat(SzavazatCommand.builder()
                    .esemenyId(esemeny1.getId())
                    .zeneId(zene1.getId())
                    .build());

            esemenyService.addSzavazat(SzavazatCommand.builder()
                    .esemenyId(esemeny1.getId())
                    .zeneId(zene1.getId())
                    .build());

            esemenyService.addSzavazat(SzavazatCommand.builder()
                    .esemenyId(esemeny1.getId())
                    .zeneId(zene2.getId())
                    .build());
        }

        @AfterEach
        void tearDown() {
            zeneService.deleteZeneById(zene1.getId());
            zeneService.deleteZeneById(zene2.getId());
            esemenyService.deleteEsemeny(esemeny1.getId());
            esemenyService.deleteEsemeny(esemeny2.getId());
        }
        @Test
        void zeneListBySzavazat() {
            var szavazatok1 = szavazatService.getEsemenyTrackList(esemeny1.getId());
            var szavazatok2 = szavazatService.getEsemenyTrackList(esemeny1.getId());

            assertEquals(4, szavazatok1.stream()
                    .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                    .findFirst()
                    .orElseThrow()
                    .getOsszSzavazat());

            assertEquals(4, szavazatok1.stream()
                    .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                    .findFirst()
                    .orElseThrow()
                    .getSzavazatByFelhasznalo());

            assertEquals(2, szavazatok2.stream()
                    .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene2.getId()))
                    .findFirst()
                    .orElseThrow()
                    .getOsszSzavazat());


            List<SzavazatTracklistDto> rendezettLista = szavazatService.getEsemenyTrackList(esemeny1.getId());
            assertThat(rendezettLista)
                    .extracting(SzavazatTracklistDto::getSzamCim)
                    .containsExactly(
                            "Teszt zene1",
                            "Teszt zene2",
                            "1_zenekar_Demo zene cím",
                            "1_zenekar_Demo zene cím 2",
                            "1_zenekar_Demo zene cím 3"
                    );

        }

        @Test
        void sajatSzavazatTorlese() {
            esemenyService.deleteSzavazat(SzavazatCommand.builder()
                    .esemenyId(esemeny1.getId())
                    .zeneId(zene1.getId())
                    .build());

            var szavazatok1 = szavazatService.getEsemenyTrackList(esemeny1.getId());

            assertEquals(3, szavazatok1.stream()
                    .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                    .findFirst()
                    .orElseThrow()
                    .getOsszSzavazat());
        }

        @Test
        @Disabled
        @WithUserDetails(DemoService.ZENEKAR_1_FELHASZNALO)
        void szavazatMasFelhasznaloval() {
            esemenyService.addSzavazat(
                    SzavazatCommand.builder()
                            .esemenyId(esemeny1.getId())
                            .zeneId(zene1.getId())
                            .build()
            );
            List<SzavazatTracklistDto> tracklist = szavazatService.getEsemenyTrackList(esemeny1.getId());

            assertEquals(5, tracklist.stream()
                    .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                    .findFirst()
                    .orElseThrow()
                    .getOsszSzavazat());
            assertEquals(1, tracklist.stream()
                    .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                    .findFirst()
                    .orElseThrow()
                    .getSzavazatByFelhasznalo());
        }

    }

}

