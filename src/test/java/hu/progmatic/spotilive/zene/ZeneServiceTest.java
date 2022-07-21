package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.DemoServiceTestHelper;
import hu.progmatic.spotilive.demo.DemoService;
import hu.progmatic.spotilive.felhasznalo.FelhasznaloService;
import hu.progmatic.spotilive.felhasznalo.NincsJogosultsagAZenekarhozException;
import hu.progmatic.spotilive.tag.TagDto;
import hu.progmatic.spotilive.tag.TagKategoria;
import hu.progmatic.spotilive.tag.TagService;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static hu.progmatic.spotilive.demo.DemoService.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ZeneServiceTest {

    @Autowired
    ZeneService zeneService;
    @Autowired
    TagService tagService;
    @Autowired
    ZenekarService zenekarService;

    private Integer demoZenekarId;

    @BeforeEach
    void setUp() {
        demoZenekarId = demoServiceTestHelper.getdemoZeneKar1Id();
        demozene = demoServiceTestHelper.getDemoZenekar1ZeneDto();
    }

    @Autowired
    DemoServiceTestHelper demoServiceTestHelper;

    @Autowired
    FelhasznaloService felhasznaloService;

    private ZeneDto demozene;

    @Test
    @WithUserDetails(ZENEKAR_1_FELHASZNALO)
    void zenekarCsakASajatSzamaitLatjaZenekar1() {
        var sajatLista = zeneService.findAllModosithatoDto();
        assertThat(sajatLista)
                .extracting(ZeneDto::getZenekarId)
                .containsOnly(demoZenekarId);
        assertThat(sajatLista)
                .extracting(ZeneDto::getCim)
                .contains(demoServiceTestHelper.getDemoZenekar1ZeneDto().getCim());
    }

    @Test
    @WithUserDetails(ZENEKAR_2_FELHASZNALO)
    void zenekarCsakASajatSzamaitLatjaZenekar2() {
        var sajatLista = zeneService.findAllModosithatoDto();
        assertThat(sajatLista)
                .extracting(ZeneDto::getZenekarId)
                .doesNotContain(demoZenekarId);
        assertThat(sajatLista)
                .extracting(ZeneDto::getCim)
                .doesNotContain(demoServiceTestHelper.getDemoZenekar1ZeneDto().getCim());
    }

    @Test
    @WithUserDetails(ADMIN_FELHASZNALO)
    void zenekarCsakASajatSzamaitLatjaAdmin() {
        var sajatLista = zeneService.findAllModosithatoDto();
        assertThat(sajatLista)
                .extracting(ZeneDto::getZenekarId)
                .contains(demoZenekarId);
        assertThat(sajatLista)
                .extracting(ZeneDto::getCim)
                .contains(demoServiceTestHelper.getDemoZenekar1ZeneDto().getCim());
        assertThat(sajatLista)
                .extracting(ZeneDto::getCim)
                .contains(demoServiceTestHelper.getDemoZenekar2ZeneDto().getCim());

    }

    @Test
    @WithUserDetails(ADMIN_FELHASZNALO)
    void createZeneTest() {
        CreateZeneCommand zene = CreateZeneCommand.builder()
                .cim("Create zene cim")
                .hosszMp(123)
                .eloado("Valami előadó")
                .zenekarId(demoZenekarId)
                .build();

        ZeneDto mentettZene = zeneService.createZene(zene);

        assertThat(mentettZene).extracting(ZeneDto::getId).isNotNull();
        assertNotNull(mentettZene.getZenekarId());
        assertEquals(demoZenekarId, mentettZene.getZenekarId());

        String hiba = "";
        try {
            zeneService.createZene(zene);
        } catch (CreateZeneExeption e) {
            hiba = e.getMessage();
        }
        assertEquals("Zene már létezik ilyen címmel", hiba);

        zeneService.deleteZeneById(mentettZene.getId());
    }

    @Test
    @WithUserDetails(ZENEKAR_1_FELHASZNALO)
    void createZeneTestJogosultsagNelkul() {
        CreateZeneCommand zene = CreateZeneCommand.builder()
                .cim("Create zene cim jogosultság")
                .hosszMp(123)
                .eloado("Valami előadó jogosultság")
                .build();

        ZeneDto mentettZene = zeneService.createZene(zene);

        assertThat(mentettZene).extracting(ZeneDto::getId).isNotNull();
        assertThat(mentettZene.getZenekarId()).isEqualTo(felhasznaloService.getZenekarId());
        zeneService.deleteZeneById(mentettZene.getId());
    }

    @Nested
    @WithUserDetails(ZENEKAR_1_FELHASZNALO)
    public class LetezoZenevelTest {

        ZeneDto testZene;

        @AfterEach
        void tearDown() {
            zeneService.deleteZeneById(testZene.getId());
        }

        @BeforeEach
        void setUp() {
            testZene = zeneService.createZene(CreateZeneCommand.builder()
                    .cim("Teszt Zene")
                    .eloado("teszt előadó")
                    .hosszMp(123)
                    .zenekarId(demoZenekarId)
                    .build());
        }

        @Test
        void deleteTestNincsJogosultsag() {
            Integer masikZenekarZeneId = demoServiceTestHelper.getDemoZenekar2ZeneDto().getId();
            assertThatThrownBy(() ->
                    zeneService.deleteZeneById(masikZenekarZeneId)
            )
                    .isInstanceOf(NincsJogosultsagAZenekarhozException.class)
                    .hasMessageContaining("Zenekar jogosultsággal nem módosítható más zenéje!");
        }

        @Test
        void deleteTest() {
            ZeneDto deletezene = zeneService.createZene(CreateZeneCommand.builder()
                    .cim("Delete zenecim")
                    .eloado("delete előadó")
                    .hosszMp(123)
                    .zenekarId(demoZenekarId)
                    .build());
            List<ZeneDto> lekertZenek = zeneService.findAllDto();
            assertThat(lekertZenek)
                    .extracting(ZeneDto::getCim)
                    .contains("Delete zenecim");
            zeneService.deleteZeneById(deletezene.getId());
            lekertZenek = zeneService.findAllDto();
            assertThat(lekertZenek)
                    .extracting(ZeneDto::getCim)
                    .doesNotContain("Delete zenecim");
        }

        @Test
        void getByCimTest() {
            assertEquals(testZene.getCim(), zeneService.getBycim("Teszt Zene").getCim());
        }

        @Test
        void editTest() {
            ZeneDto dto = ZeneDto.builder()
                    .id(testZene.getId())
                    .cim("Edited cim")
                    .eloado("teszt előadó")
                    .hosszMp(123)
                    .build();
            var modositott = zeneService.editZene(dto);
            assertEquals("Edited cim", modositott.getCim());

        }

        @Test
        void editTestNincsJogosultsaga() {
            var masikZenekarZene = demoServiceTestHelper.getDemoZenekar2ZeneDto();
            assertThatThrownBy(() ->
                    zeneService.editZene(masikZenekarZene)
            )
                    .isInstanceOf(NincsJogosultsagAZenekarhozException.class)
                    .hasMessageContaining("Zenekar jogosultsággal nem módosítható más zenéje!");

        }

        @Test
        void getByIdTest() {
            assertEquals(testZene.getId(), zeneService.getZeneDtoById(testZene.getId()).getId());
        }

        @Test
        void addTagTest() {
            TagDto dto = TagDto.builder()
                    .tagNev("Teszt tag 1")
                    .tagKategoria(TagKategoria.MUFAJ)
                    .build();

            TagDto mentettTag = tagService.createTag(dto);

            zeneService.addTag(testZene.getId(), mentettTag.getId());
            ZeneDto modositottZene = zeneService.getZeneDtoById(testZene.getId());
            assertThat(modositottZene.getTagStringList()).hasSize(1).contains("Teszt tag 1");
        }

        @Test
        void deleteTestfromZeneById() {
            TagDto dto = TagDto.builder()
                    .tagNev("Teszt tag")
                    .tagKategoria(TagKategoria.HANGULAT)
                    .build();
            TagDto mentettTag = tagService.createTag(dto);

            zeneService.addTag(testZene.getId(), mentettTag.getId());
            testZene = zeneService.getZeneDtoById(testZene.getId());
            assertThat(testZene.getTagStringList()).hasSize(1);
            zeneService.deleteTagFromZene(mentettTag.getId(), testZene.getId());
            testZene = zeneService.getZeneDtoById(testZene.getId());
            assertThat(testZene.getTagStringList()).hasSize(0);

        }

        @Test
        void zeneTagSzerkesztesListaDto() {
            Integer testZeneId = demoServiceTestHelper.getDemoZenekar1ZeneDto().getId();
            ZeneTagSzerkesztesListaDto dto = tagService.getZeneTagSzerkesztesListaDto(testZeneId);
            assertEquals(testZeneId, dto.getZeneId());
            assertThat(dto.getTagByKategoria())
                    .containsKeys(TagKategoria.HANGULAT, TagKategoria.MUFAJ, TagKategoria.TEMPO);
            assertThat(dto.getTagByKategoria().get(TagKategoria.MUFAJ).getHozzaadott())
                    .hasSize(1)
                    .extracting(TagDto::getTagNev)
                    .contains(DemoService.DEMO_TAG);
            assertThat(dto.getTagByKategoria().get(TagKategoria.MUFAJ).getNemHozzaadott())
                    .hasSize(1)
                    .extracting(TagDto::getTagNev)
                    .contains("Demo tag 2")
                    .doesNotContain(DemoService.DEMO_TAG);
            assertThat(dto.getTagByKategoria().get(TagKategoria.HANGULAT).getHozzaadott())
                    .hasSize(1)
                    .extracting(TagDto::getTagNev)
                    .contains("Hangulat tag");
        }
    }

    @Test
    void getZeneByNevTest() {
        assertThat(zeneService.getZeneByNev(demozene.getCim())).extracting(ZeneDto::getCim).isEqualTo(demozene.getCim());
    }

    @Test
    void getZeneByTag() {
        var command = FilterByTagCommand.builder()
                .tagLista(List.of(demoServiceTestHelper.getDemoTagDto().getTagNev()))
                .build();

        var filterLista = zeneService.getZenekByTagList(command);
        assertThat(filterLista).extracting(ZeneDto::getCim)
                .containsExactlyInAnyOrder("1_zenekar_Demo zene cím", "2_zenekar_Demo zene cím");
    }
}