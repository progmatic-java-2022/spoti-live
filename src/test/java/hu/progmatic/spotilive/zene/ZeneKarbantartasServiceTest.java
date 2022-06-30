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
            zeneKarbantartasService.deleteZeneById(testZene.getId());
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
                    .build();
            var modositott = zeneKarbantartasService.editZene(dto);
            assertEquals("Edited cim", modositott.getCim());

        }

        @Test
        void getByIdTest() {
            assertEquals(testZene.getId(), zeneKarbantartasService.getZeneDtoById(testZene.getId()).getId());
        }

        @Test
        void createTagTest() {
            TagDto dto = TagDto.builder()
                    .tagNev("Teszt tag")
                    .build();

            TagDto mentettTag = zeneKarbantartasService.createTag(dto);
            assertThat(zeneKarbantartasService.getTagById(mentettTag.getId())).isNotNull();

        }

        @Test
        void addTagTest() {
            TagDto dto = TagDto.builder()
                    .tagNev("Teszt tag")
                    .build();

            TagDto mentettTag = zeneKarbantartasService.createTag(dto);

            zeneKarbantartasService.addTag(testZene.getId(), mentettTag.getId());
            ZeneDto modositottZene = zeneKarbantartasService.getZeneDtoById(testZene.getId());
            assertThat(modositottZene.getTagStringList()).hasSize(1).contains("Teszt tag");
        }

        @Test
        void editTagTest() {
            TagDto dto = TagDto.builder()
                    .tagNev("Teszt tag")
                    .build();

            TagDto mentettTag = zeneKarbantartasService.createTag(dto);

            zeneKarbantartasService.addTag(testZene.getId(), mentettTag.getId());

            var hozzaadott = zeneKarbantartasService.getZeneDtoById(testZene.getId());
            String tagnev = hozzaadott.getTagStringList().get(0);
            assertNotNull(tagnev);
            assertEquals("Teszt tag", tagnev);


            TagEditCommand command = TagEditCommand.builder()
                    .tagId(mentettTag.getId())
                    .tagNev("Teszt Edited Tag")
                    .build();

            zeneKarbantartasService.editTagById(command);
            var editTagesZene = zeneKarbantartasService.getZeneDtoById(testZene.getId());
            assertEquals("Teszt Edited Tag", editTagesZene.getTagStringList().get(0));
        }

        @Test
        void listAllTagTest() {
            TagDto dto = TagDto.builder()
                    .tagNev("Teszt tag")
                    .build();

            TagDto dto2 = TagDto.builder()
                    .tagNev("Teszt tag 2")
                    .build();

            TagDto mentettTag = zeneKarbantartasService.createTag(dto);
            TagDto mentettTag2 = zeneKarbantartasService.createTag(dto2);

            zeneKarbantartasService.addTag(testZene.getId(), mentettTag.getId());
            zeneKarbantartasService.addTag(testZene.getId(), mentettTag2.getId());

            var kettovelRendelkezo = zeneKarbantartasService.getZeneDtoById(testZene.getId());

            List<String> listAllTagByZeneId = zeneKarbantartasService.listAllTagStringByZeneId(kettovelRendelkezo.getId());
            List<TagDto> listAllTagDtoByZeneId = zeneKarbantartasService.listAllTagDtoByZeneId(kettovelRendelkezo.getId());

            assertThat(listAllTagByZeneId).hasSize(2).containsExactly("Teszt tag", "Teszt tag 2");
            assertThat(listAllTagDtoByZeneId).hasSize(2).extracting(TagDto::getTagNev).containsExactly("Teszt tag", "Teszt tag 2");

            assertThat(zeneKarbantartasService.getAllTag()).hasSize(5);

            zeneKarbantartasService.deleteTagFromZene(mentettTag2.getId(), kettovelRendelkezo.getId());

            listAllTagByZeneId = zeneKarbantartasService.listAllTagStringByZeneId(kettovelRendelkezo.getId());

            assertThat(listAllTagByZeneId).hasSize(1).containsExactly("Teszt tag");

            assertThat(zeneKarbantartasService.getAllTag()).hasSize(5);

            assertThat(kettovelRendelkezo.getTagStringList()).hasSize(2);

            zeneKarbantartasService.deleteTagById(mentettTag2.getId());

            assertThat(zeneKarbantartasService.getAllTag()).hasSize(4);
        }
    }


}