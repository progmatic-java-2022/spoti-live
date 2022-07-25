package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.DemoServiceTestHelper;
import hu.progmatic.spotilive.demo.DemoService;
import hu.progmatic.spotilive.felhasznalo.Felhasznalo;
import hu.progmatic.spotilive.felhasznalo.NincsJogosultsagAZenekarhozException;
import hu.progmatic.spotilive.felhasznalo.UserType;
import hu.progmatic.spotilive.zene.CreateZeneCommand;
import hu.progmatic.spotilive.zene.FilterByTagCommand;
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

    private  FilterByTagCommand command;

    @BeforeEach
    void setUp() {
        demoZenekar1Id = demoServiceTestHelper.getdemoZeneKar1Id();
        command = FilterByTagCommand
                .builder()
                .tagLista(List.of())
                .esemenyId(demoServiceTestHelper.getZenekar1demoEsemenyId())
                .build();

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

            esemenyService.addSzavazat(SzavazatCommand.builder()
                    .esemenyId(esemeny1.getId())
                    .zeneId(zene.getId())
                    .build());

            var esemenyZenevel = esemenyService.getEsemenyDtoById(esemeny1.getId());
            assertEquals("Tódor Születésnapja", esemenyZenevel.getNev());

            assertThat(esemenyZenevel.getSzavazatDtos())
                    .hasSize(1)
                    ;
            zeneService.deleteZeneById(zene.getId());

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
        List<SzavazatTracklistDto> list = szavazatService.getEsemenyTrackList(command);
        assertThat(list)
                .hasSize(3);
    }
}
