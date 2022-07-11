package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.demo.DemoService;
import hu.progmatic.spotilive.felhasznalo.UserType;
import hu.progmatic.spotilive.zene.CreateZeneCommand;
import hu.progmatic.spotilive.zene.Zene;
import hu.progmatic.spotilive.zene.ZeneDto;
import hu.progmatic.spotilive.zene.ZeneService;
import hu.progmatic.spotilive.zenekar.ZenekarService;
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

    @Autowired
    private ZenekarService zenekarService;


    private Integer demoZenekarId;
    @Autowired
    private ZeneService zeneService;

    @BeforeEach
    void setUp() {
        demoZenekarId = zenekarService.getByName(DemoService.DEMO_ZENEKAR).getId();
    }

    @Test
    void esemenyLetrehozasaTest() {
        CreateEsemenyCommand esemeny = CreateEsemenyCommand.builder()
                .nev("Tódor Születésnapja")
                .idoPont(LocalDateTime.parse("2022-06-27T15:30"))
                .zenekarId(demoZenekarId)
                .build();
        EsemenyDto letrehozott = esemenyService.createEsemeny(esemeny);

        assertNotNull(letrehozott.getId());
        assertEquals("Tódor Születésnapja", letrehozott.getNev());
        assertEquals(DemoService.DEMO_ZENEKAR, letrehozott.getZenekarNev());

        esemenyService.deleteEsemeny(letrehozott.getId());
        assertEquals(1, esemenyService.countAllEsemeny());

        var esemenyek = esemenyService.findAllEsemeny();
        assertThat(esemenyek).extracting(EsemenyDto::getNev)
                .contains("Demo esemény");
    }


    @Test
    void esemenyTorleseTest() {
        CreateEsemenyCommand esemeny = CreateEsemenyCommand.builder()
                .nev("Törlendő esemény")
                .idoPont(LocalDateTime.parse("2022-06-27T15:30"))
                .zenekarId(demoZenekarId)
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
            esemeny1 = esemenyService.createEsemeny(CreateEsemenyCommand.builder()
                    .nev("Tódor Születésnapja")
                    .idoPont(LocalDateTime.parse("2022-05-27T15:30"))
                    .zenekarId(demoZenekarId)
                    .build());
            esemeny2 = esemenyService.createEsemeny(CreateEsemenyCommand.builder()
                    .nev("Tivadar Névnapja")
                    .idoPont(LocalDateTime.parse("2002-10-19T10:45"))
                    .zenekarId(demoZenekarId)
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

            modositando = esemenyService.createEsemeny(CreateEsemenyCommand.builder()
                    .nev(esemeny1.getNev())
                    .idoPont(esemeny1.getIdoPont())
                    .zenekarId(demoZenekarId)
                    .build());


            EsemenyDto modosito = EsemenyDto.builder()
                    .nev("modositott név")
                    .idoPont(LocalDateTime.parse("2022-06-27T15:30"))
                    .build();
            esemenyService.udpate(modosito, modositando.getId());
            EsemenyDto updatelt = esemenyService.getEsemenyDtoById(modositando.getId());
            assertEquals("modositott név", updatelt.getNev());

            esemenyService.deleteEsemeny(updatelt.getId());
            assertThat(esemenyService.findAllEsemeny())
                    .hasSize(3);
        }

        @Test
        void addZeneToEsemenyTest() {
            var zene = zeneService.createZene(CreateZeneCommand.builder()
                    .cim("Valami cím")
                    .eloado("Valami előadó")
                    .hosszMp(123)
                    .zenekarId(demoZenekarId)
                    .build());

            esemenyService.addZenetoEsemenyByZeneId(AddZeneToEsemenyCommand.builder()
                    .esemenyId(esemeny1.getId())
                    .zeneId(zene.getId())
                    .build());

            var esemenyZenevel = esemenyService.getEsemenyDtoById(esemeny1.getId());
            assertEquals("Tódor Születésnapja", esemenyZenevel.getNev());

            assertThat(esemenyZenevel.getZenek())
                    .hasSize(1)
                    .extracting(zenek -> zenek.getZene().getEloado())
                    .contains("Valami előadó");

        }

        @Nested
        class EsemenyZenekkelTest {
            ZeneDto zene1;
            ZeneDto zene2;

            @BeforeEach
            void setUp() {
                zene1 = zeneService.createZene(CreateZeneCommand
                        .builder()
                        .cim("Teszt zene1")
                        .hosszMp(123)
                        .eloado("Teszt eloado1")
                        .zenekarId(demoZenekarId)
                        .build());
                zene2 = zeneService.createZene(CreateZeneCommand
                        .builder()
                        .eloado("Teszt eloado2")
                        .hosszMp(123)
                        .cim("Teszt zene2")
                        .zenekarId(demoZenekarId)
                        .build());

                esemenyService.addZenetoEsemenyByZeneId(AddZeneToEsemenyCommand
                        .builder()
                        .zeneId(zene1.getId())
                        .esemenyId(esemeny1.getId())
                        .build());

                esemenyService.addZenetoEsemenyByZeneId(AddZeneToEsemenyCommand
                        .builder()
                        .esemenyId(esemeny1.getId())
                        .zeneId(zene2.getId())
                        .build());
            }

            @AfterEach
            void tearDown() {
                zeneService.deleteZeneById(zene1.getId());
                zeneService.deleteZeneById(zene2.getId());
            }

            @Test
            void addSzavazatToZene() {
                var esemenyZenevel = esemenyService.getEsemenyDtoById(esemeny1.getId());
                assertEquals(2, esemenyZenevel.getZenek().size());
                assertEquals("Teszt zene1", zeneService.getBycim("Teszt zene1").getCim());

                esemenyService.addSzavazat(AddSzavazatCommand.builder()
                        .esemenyId(esemenyZenevel.getId())
                        .zeneId(zeneService.getBycim("Teszt zene1").getId())
                        .build());
                var modositottEsemeny = esemenyService.getEsemenyDtoById(esemeny1.getId());
                var zeneSzavazattal = modositottEsemeny.getZenek().stream()
                        .filter(zene -> zene.getZene().getId().equals(
                                zeneService.getBycim("Teszt zene1").getId()))
                        .findFirst()
                        .orElseThrow();
                assertEquals(1, zeneSzavazattal.getSzavazat());

            }

            @Nested
            class ZenekSzavazattalTest {
                @BeforeEach
                void setUp() {


                    esemenyService.addSzavazat(AddSzavazatCommand.builder()
                            .esemenyId(esemeny1.getId())
                            .zeneId(zene1.getId())
                            .build());

                    esemenyService.addSzavazat(AddSzavazatCommand.builder()
                            .esemenyId(esemeny1.getId())
                            .zeneId(zene1.getId())
                            .build());

                    esemenyService.addSzavazat(AddSzavazatCommand.builder()
                            .esemenyId(esemeny1.getId())
                            .zeneId(zene1.getId())
                            .build());

                    esemenyService.addSzavazat(AddSzavazatCommand.builder()
                            .esemenyId(esemeny1.getId())
                            .zeneId(zene2.getId())
                            .build());
                }

                @Test
                void zeneListBySzavazat() {
                    var esemenyZenevel = esemenyService.getEsemenyDtoById(esemeny1.getId());
                    assertEquals(3, esemenyZenevel.getZenek().stream()
                            .filter(zene -> zene.getZene().getId().equals(
                                    zeneService.getBycim("Teszt zene1").getId()))
                            .findFirst()
                            .orElseThrow().getSzavazat());

                    assertEquals(1, esemenyZenevel.getZenek().stream()
                            .filter(zene -> zene.getZene().getId().equals(
                                    zeneService.getBycim("Teszt zene2").getId()))
                            .findFirst()
                            .orElseThrow().getSzavazat());

                    List<ZeneToEsemenyDto> rendezettLista = esemenyService.listaBySzavazat(esemenyZenevel.getId());
                    assertThat(rendezettLista)
                            .hasSize(2)
                            .extracting(ZeneToEsemenyDto::getZene)
                            .extracting(Zene::getCim)
                            .containsExactly("Teszt zene1", "Teszt zene2");
                }
            }
        }
    }
}
