package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.DemoServiceTestHelper;
import hu.progmatic.spotilive.demo.DemoService;
import hu.progmatic.spotilive.felhasznalo.NincsJogosultsagAZenekarhozException;
import hu.progmatic.spotilive.felhasznalo.UserType;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser(roles = UserType.Roles.ESEMENY_KEZELES_ROLE)
class EsemenyServiceTest {
    @Autowired
    private EsemenyService esemenyService;

    @Autowired
    private ZenekarService zenekarService;




    @Autowired
    private DemoServiceTestHelper demoServiceTestHelper;
    private Integer demoZenekar1Id;
    @Autowired
    private ZeneService zeneService;
    @Autowired
    private SzavazatService szavazatService;

    @BeforeEach
    void setUp() {
        demoZenekar1Id = demoServiceTestHelper.getdemoZeneKar1Id();
    }

    @Test
    @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
    void esemenyLetrehozasaTest() {
        CreateEsemenyCommand esemeny = CreateEsemenyCommand.builder()
                .nev("Tódor Születésnapja")
                .idoPont(LocalDateTime.parse("2022-06-27T15:30"))
                .zenekarId(demoZenekar1Id)
                .build();
        EsemenyDto letrehozott = esemenyService.createEsemeny(esemeny);

        assertNotNull(letrehozott.getId());
        assertEquals("Tódor Születésnapja", letrehozott.getNev());
        assertEquals(demoServiceTestHelper.getdemoZeneKarNev(), letrehozott.getZenekarNev());

        var esemenyek = esemenyService.findAllEsemeny();
        assertThat(esemenyek).extracting(EsemenyDto::getNev)
            .contains("Tódor Születésnapja");
        esemenyService.deleteEsemeny(letrehozott.getId());
    }


    @Test
    @WithUserDetails(DemoService.ZENEKAR_1_FELHASZNALO)
    void esemenyTorleseTest() {
        CreateEsemenyCommand esemeny = CreateEsemenyCommand.builder()
                .nev("Törlendő esemény")
                .idoPont(LocalDateTime.parse("2022-06-27T15:30"))
                .zenekarId(demoZenekar1Id)
                .build();
        EsemenyDto letrehozott = esemenyService.createEsemeny(esemeny);
        assertEquals("Törlendő esemény", letrehozott.getNev());
        assertNotNull(letrehozott.getId());

        esemenyService.deleteEsemeny(letrehozott.getId());
        var esemenyek = esemenyService.findAllEsemeny();
        assertThat(esemenyek).extracting(EsemenyDto::getNev)
                .doesNotContain("Törlendő esemény");

    }

    @Nested
    @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
    class TesztLetezoEsemenyekkel {
        EsemenyDto esemeny1;
        EsemenyDto esemeny2;


        @BeforeEach
        void setUp() {
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

        }

        @AfterEach
        void tearDown() {
            esemenyService.deleteEsemeny(esemeny1.getId());
            esemenyService.deleteEsemeny(esemeny2.getId());
        }

        @Test
        void esemenyListazasTest() {
            var esemenyList = esemenyService.findAllModosithatoDto();
            assertThat(esemenyList)
                    .hasSizeGreaterThan(2)
                    .extracting(EsemenyDto::getNev)
                    .contains("Tódor Születésnapja")
                    .contains(demoServiceTestHelper.getZenekar1DemoEsemeny().getNev())
                    .contains(demoServiceTestHelper.getZenekar2DemoEsemeny().getNev());
        }
        @Test
        @WithUserDetails(DemoService.ZENEKAR_1_FELHASZNALO)
        void esemenyListazasZenekaronkentTest(){
            var esemenyList = esemenyService.findAllModosithatoDto();
            assertThat(esemenyList)
                    .hasSizeGreaterThan(2)
                    .extracting(EsemenyDto::getNev)
                    .contains("Tódor Születésnapja")
                    .contains(demoServiceTestHelper.getZenekar1DemoEsemeny().getNev())
                    .doesNotContain(demoServiceTestHelper.getZenekar2DemoEsemeny().getNev());
        }



        @Test
        @WithUserDetails(DemoService.ZENEKAR_1_FELHASZNALO)
        void esemenySzerkeszteseTest() {
            EsemenyDto modositando;

            modositando = esemenyService.createEsemeny(CreateEsemenyCommand.builder()
                    .nev(esemeny1.getNev())
                    .idoPont(esemeny1.getIdoPont())
                    .zenekarId(demoZenekar1Id)
                    .build());


            EsemenyDto modosito = EsemenyDto.builder()
                    .nev("modositott név")
                    .idoPont(LocalDateTime.parse("2022-06-27T15:30"))
                    .build();
            esemenyService.update(modosito, modositando.getId());
            EsemenyDto updatelt = esemenyService.getEsemenyDtoById(modositando.getId());
            assertEquals("modositott név", updatelt.getNev());

            esemenyService.deleteEsemeny(updatelt.getId());
        }
        @Test
        @WithUserDetails(DemoService.ZENEKAR_2_FELHASZNALO)
        void esemenySzerkeszteseTestJogosultsagNelkul() {
            EsemenyDto modositandoEsemeny = demoServiceTestHelper.getZenekar1DemoEsemeny();
            assertThatThrownBy(() -> esemenyService.update(modositandoEsemeny,modositandoEsemeny.getId())
            ).isInstanceOf(NincsJogosultsagAZenekarhozException.class)
                    .hasMessageContaining("Zenekar jogosultsággal nem módosítható más eseménye!");
        }

        @Test
        void addZeneToEsemenyTest() {
            var zene = zeneService.createZene(CreateZeneCommand.builder()
                    .cim("Valami cím")
                    .eloado("Valami előadó")
                    .hosszMp(123)
                    .zenekarId(demoZenekar1Id)
                    .build());

            esemenyService.addSzavazat(AddZeneToEsemenyCommand.builder()
                    .esemenyId(esemeny1.getId())
                    .zeneId(zene.getId())
                    .build());

            var esemenyZenevel = esemenyService.getEsemenyDtoById(esemeny1.getId());
            assertEquals("Tódor Születésnapja", esemenyZenevel.getNev());

            assertThat(esemenyZenevel.getSzavazatDtos())
                    .hasSize(1)
                    ;

        }

        @Nested
        @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
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
                        .zenekarId(demoZenekar1Id)
                        .build());
                zene2 = zeneService.createZene(CreateZeneCommand
                        .builder()
                        .eloado("Teszt eloado2")
                        .hosszMp(123)
                        .cim("Teszt zene2")
                        .zenekarId(demoZenekar1Id)
                        .build());

                esemenyService.addSzavazat(AddZeneToEsemenyCommand
                        .builder()
                        .zeneId(zene1.getId())
                        .esemenyId(esemeny1.getId())
                        .build());

                esemenyService.addSzavazat(AddZeneToEsemenyCommand
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


            @Nested
            class ZenekSzavazattalTest {
                @BeforeEach
                void setUp() {

                    esemenyService.addSzavazat(AddZeneToEsemenyCommand.builder()
                            .esemenyId(esemeny1.getId())
                            .zeneId(zene1.getId())
                            .build());

                    esemenyService.addSzavazat(AddZeneToEsemenyCommand.builder()
                            .esemenyId(esemeny1.getId())
                            .zeneId(zene1.getId())
                            .build());

                    esemenyService.addSzavazat(AddZeneToEsemenyCommand.builder()
                            .esemenyId(esemeny1.getId())
                            .zeneId(zene1.getId())
                            .build());

                    esemenyService.addSzavazat(AddZeneToEsemenyCommand.builder()
                            .esemenyId(esemeny1.getId())
                            .zeneId(zene2.getId())
                            .build());
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
                                    "1_zenekar_Demo zene cím 3",
                                    "Valami cím");

                }


            }
        }
    }
    @Test
    @WithUserDetails(DemoService.ZENEKAR_1_FELHASZNALO)
    void esemenyListazasCsakUserSajatja() {
        var sajatLista = esemenyService.findAllModosithatoDto();
        assertThat(sajatLista)
                .extracting(EsemenyDto::getZenekarNev)
                .containsOnly(demoServiceTestHelper.getDemoZenekar1ZeneDto().getZenekarNev());
    }

    @Test
    @WithUserDetails(DemoService.ZENEKAR_2_FELHASZNALO)
    void esemenyListazasCsakUser2Sajatja() {
        var sajatLista = esemenyService.findAllModosithatoDto();
        assertThat(sajatLista)
                .extracting(EsemenyDto::getZenekarNev)
                .containsOnly(demoServiceTestHelper.getDemoZenekar2ZeneDto().getZenekarNev());
    }

    @Test
    @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
    void esemenyListazasAdmin() {
        var sajatLista = esemenyService.findAllModosithatoDto();
        assertThat(sajatLista)
                .extracting(EsemenyDto::getZenekarNev)
                .contains(demoServiceTestHelper.getDemoZenekar2ZeneDto().getZenekarNev())
                .contains(demoServiceTestHelper.getDemoZenekar1ZeneDto().getZenekarNev());
    }

    @Test
    @WithUserDetails(DemoService.ZENEKAR_2_FELHASZNALO)
    void deleteTestNincsJogosultsag() {
        Integer zenekar1demoEsemenyId = demoServiceTestHelper.getZenekar1demoEsemenyId();
        assertThatThrownBy(() ->
                esemenyService.deleteEsemeny(zenekar1demoEsemenyId)
        )
                .isInstanceOf(NincsJogosultsagAZenekarhozException.class)
                .hasMessageContaining("Zenekar jogosultsággal nem módosítható más eseménye!");
    }

    @Test
    void szavazatListTest() {
        var esemenyId = demoServiceTestHelper.getZenekar1demoEsemenyId();
        List<SzavazatTracklistDto> list = szavazatService.getEsemenyTrackList(esemenyId);
        assertThat(list)
                .hasSize(3);
    }
}
