package hu.progmatic.spotilive.zene;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ZeneKarbantartasServiceTest {

    @Autowired
    ZeneKarbantartasService zeneKarbantartasService;

    @Test
    void createZeneTest() {
    ZeneDto zeneDto = ZeneDto.builder().cim("Create zene cim").build();
    ZeneDto mentettZene = zeneKarbantartasService.createZene(zeneDto);

    assertThat(mentettZene).extracting(ZeneDto::getId).isNotNull();
    }

    @Nested
    public class LetezoZenevelTest {
        ZeneDto testZene;

        @AfterEach
        void tearDown() {
            zeneKarbantartasService.deleteById(testZene.getId());
        }

        @BeforeEach
        void setUp() {
            testZene = ZeneDto.builder().cim("Teszt Zene").eloado("teszt előadó").build();
            testZene = zeneKarbantartasService.createZene(testZene);
        }

        @Test
        void deleteTest() {
            ZeneDto deletezene = zeneKarbantartasService.createZene(ZeneDto.builder().cim("Delete zenecim").eloado("delete előadó").build());
            List<ZeneDto> lekertZenek = zeneKarbantartasService.findAllDto();
            assertThat(lekertZenek)
                    .extracting(ZeneDto::getCim)
                    .contains("Delete zenecim");
            zeneKarbantartasService.deleteById(deletezene.getId());
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
                    .build();
            var modositott = zeneKarbantartasService.editZene(dto);
            assertEquals("Edited cim", modositott.getCim());

        }

        @Test
        void getByIdTest() {
            assertEquals(testZene.getId(), zeneKarbantartasService.getById(testZene.getId()).getId());
        }

        @Test
        void addTagTest() {
            TagDto dto = TagDto.builder()
                    .tagNev("Teszt tag")
                    .zeneId(testZene.getId())
                    .build();

            zeneKarbantartasService.addTag(dto);

            var hozzaadott = zeneKarbantartasService.getById(testZene.getId());
            assertNotNull(hozzaadott.getTagDtoList().get(0).getId());
            assertEquals("Teszt tag",hozzaadott.getTagDtoList().get(0).getTagNev());
        }

        @Test
        void deleteTagTest() {
            TagDto dto = TagDto.builder()
                    .tagNev("Teszt tag")
                    .zeneId(testZene.getId())
                    .build();

            zeneKarbantartasService.addTag(dto);

            var hozzaadott = zeneKarbantartasService.getById(testZene.getId());
            Integer tagId = hozzaadott.getTagDtoList().get(0).getId();
            assertNotNull(hozzaadott.getTagDtoList().get(0).getId());
            assertEquals("Teszt tag",hozzaadott.getTagDtoList().get(0).getTagNev());

            zeneKarbantartasService.deleteTagById(tagId);
            var toroltTagesZene = zeneKarbantartasService.getById(testZene.getId());
            assertThat(toroltTagesZene.getTagDtoList()).hasSize(0);
        }

        @Test
        void editTagTest() {
            TagDto dto = TagDto.builder()
                    .tagNev("Teszt tag")
                    .zeneId(testZene.getId())
                    .build();

            zeneKarbantartasService.addTag(dto);

            var hozzaadott = zeneKarbantartasService.getById(testZene.getId());
            Integer tagId = hozzaadott.getTagDtoList().get(0).getId();
            assertNotNull(hozzaadott.getTagDtoList().get(0).getId());
            assertEquals("Teszt tag",hozzaadott.getTagDtoList().get(0).getTagNev());

            TagEditCommand command = TagEditCommand.builder()
                    .tagId(tagId)
                    .tagNev("Teszt Edited Tag")
                    .build();

            zeneKarbantartasService.editTagById(command);
            var editTagesZene = zeneKarbantartasService.getById(testZene.getId());
            assertEquals("Teszt Edited Tag",editTagesZene.getTagDtoList().get(0).getTagNev());
        }

        @Test
        void listAllTagTest() {
            TagDto dto = TagDto.builder()
                    .tagNev("Teszt tag")
                    .zeneId(testZene.getId())
                    .build();

            zeneKarbantartasService.addTag(dto);

            var hozzaadott = zeneKarbantartasService.getById(testZene.getId());

            TagDto dto2 = TagDto.builder()
                    .tagNev("Teszt tag 2")
                    .zeneId(testZene.getId())
                    .build();
            zeneKarbantartasService.addTag(dto2);

            var kettovelRendelkezo = zeneKarbantartasService.getById(testZene.getId());

            List<TagDto> listAllTagByZeneId = zeneKarbantartasService.listAllTagByZeneId(testZene.getId());

            assertThat(listAllTagByZeneId).hasSize(2).extracting(TagDto::getTagNev).containsExactly("Teszt tag", "Teszt tag 2");
        }
    }


}