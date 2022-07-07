package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.tag.TagDto;
import hu.progmatic.spotilive.tag.TagKategoria;
import hu.progmatic.spotilive.tag.TagService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ZeneServiceTest {

    @Autowired
    ZeneService zeneKarbantartasService;

    @Autowired
    TagService tagService;

    @Test
    void createZeneTest() {
        ZeneDto zeneDto = ZeneDto.builder()
                .cim("Create zene cim")
                .hosszMp(123)
                .eloado("Valami előadó")
                .build();
        ZeneDto mentettZene = zeneKarbantartasService.createZene(zeneDto);

        assertThat(mentettZene).extracting(ZeneDto::getId).isNotNull();
    }

    @Nested
    public class LetezoZenevelTest {
        ZeneDto testZene;

        @AfterEach
        void tearDown() {
            zeneKarbantartasService.deleteZeneById(testZene.getId());
        }

        @BeforeEach
        void setUp() {
            testZene = ZeneDto.builder()
                    .cim("Teszt Zene")
                    .eloado("teszt előadó")
                    .hosszMp(123)
                    .build();
            testZene = zeneKarbantartasService.createZene(testZene);
        }

        @Test
        void deleteTest() {
            ZeneDto deletezene = zeneKarbantartasService.createZene(ZeneDto.builder()
                    .cim("Delete zenecim")
                    .eloado("delete előadó")
                    .hosszMp(123)
                    .build());
            List<ZeneDto> lekertZenek = zeneKarbantartasService.findAllDto();
            assertThat(lekertZenek)
                    .extracting(ZeneDto::getCim)
                    .contains("Delete zenecim");
            zeneKarbantartasService.deleteZeneById(deletezene.getId());
            lekertZenek = zeneKarbantartasService.findAllDto();
            assertThat(lekertZenek)
                    .extracting(ZeneDto::getCim)
                    .doesNotContain("Delete zenecim");
        }

        @Test
        void getByCimTest() {
            assertEquals(testZene.getCim(), zeneKarbantartasService.getBycim("Teszt Zene").getCim());
        }

        @Test
        void editTest() {
            ZeneDto dto = ZeneDto.builder()
                    .id(testZene.getId())
                    .cim("Edited cim")
                    .eloado("teszt előadó")
                    .hosszMp(123)
                    .build();
            var modositott = zeneKarbantartasService.editZene(dto);
            assertEquals("Edited cim", modositott.getCim());

        }

        @Test
        void getByIdTest() {
            assertEquals(testZene.getId(), zeneKarbantartasService.getZeneDtoById(testZene.getId()).getId());
        }

        @Test
        void addTagTest() {
            TagDto dto = TagDto.builder()
                    .tagNev("Teszt tag")
                    .tagKategoria(TagKategoria.MUFAJ)
                    .build();

            TagDto mentettTag = tagService.createTag(dto);

            zeneKarbantartasService.addTag(testZene.getId(), mentettTag.getId());
            ZeneDto modositottZene = zeneKarbantartasService.getZeneDtoById(testZene.getId());
            assertThat(modositottZene.getTagStringList()).hasSize(1).contains("Teszt tag");
        }

        @Test
        void deleteTestfromZeneById() {
            TagDto dto = TagDto.builder()
                    .tagNev("Teszt tag")
                    .tagKategoria(TagKategoria.HANGULAT)
                    .build();
            TagDto mentettTag = tagService.createTag(dto);

            zeneKarbantartasService.addTag(testZene.getId(), mentettTag.getId());
            testZene = zeneKarbantartasService.getZeneDtoById(testZene.getId());
            assertThat(testZene.getTagStringList()).hasSize(1);
            zeneKarbantartasService.deleteTagFromZene(mentettTag.getId(), testZene.getId());
            testZene = zeneKarbantartasService.getZeneDtoById(testZene.getId());
            assertThat(testZene.getTagStringList()).hasSize(0);

        }
    }
}