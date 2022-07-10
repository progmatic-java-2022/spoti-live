package hu.progmatic.spotilive.tag;

import hu.progmatic.spotilive.zene.ZeneDto;
import hu.progmatic.spotilive.zene.ZeneService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TagServiceTest {

    @Autowired
    TagService tagService;
    @Autowired
    ZeneService zeneService;

    @AfterEach
    void tearDown() {
        tagService.deleteAlltag();
    }

    @Test
    void createTagTest() {
        TagDto dto = TagDto.builder()
                .tagNev("Teszt tag")
                .tagKategoria(TagKategoria.MUFAJ)
                .build();

        TagDto mentettTag = tagService.createTag(dto);
        assertThat(tagService.getTagById(mentettTag.getId())).isNotNull();

    }

    @Test
    void editTagTest() {
        TagDto dto = TagDto.builder()
                .tagNev("Teszt tag")
                .tagKategoria(TagKategoria.MUFAJ)
                .build();

        TagDto mentettTag = tagService.createTag(dto);

        TagEditCommand command = TagEditCommand.builder()
                .tagId(mentettTag.getId())
                .tagNev("Teszt Edited Tag")
                .tagKategoria(TagKategoria.HANGULAT)
                .build();

        tagService.editTagById(command);

        TagDto tesztDto = tagService.getTagById(mentettTag.getId());
        assertEquals("Teszt Edited Tag", tesztDto.getTagNev());
        assertEquals(TagKategoria.HANGULAT, tesztDto.getTagKategoria());
    }

    @Test
    void listAllTagTest() {
        TagDto dto = TagDto.builder()
                .tagNev("Teszt tag")
                .tagKategoria(TagKategoria.MUFAJ)
                .build();

        TagDto dto2 = TagDto.builder()
                .tagNev("Teszt tag 2")
                .tagKategoria(TagKategoria.HANGULAT)
                .build();

        TagDto mentettTag = tagService.createTag(dto);
        TagDto mentettTag2 = tagService.createTag(dto2);

        assertThat(tagService.getAllTag()).hasSize(2);

    }

    @Test
    void deleteTagByIdTest() {
        TagDto dto = TagDto.builder()
                .tagNev("Teszt tag")
                .tagKategoria(TagKategoria.MUFAJ)
                .build();

        TagDto dto2 = TagDto.builder()
                .tagNev("Teszt tag 2")
                .tagKategoria(TagKategoria.HANGULAT)
                .build();


        TagDto mentettTag = tagService.createTag(dto);
        TagDto mentettTag2 = tagService.createTag(dto2);


        tagService.deleteTagById(mentettTag.getId());

        List<TagDto> osszesTag = tagService.getAllTag();

        assertThat(osszesTag)
                .hasSize(4)
                .extracting(TagDto::getTagNev)
                .contains("Teszt tag 2");

        ZeneDto zene = zeneService.createZene(ZeneDto.builder()
                .cim("Cim")
                .eloado("eloado")
                .hosszMp(50)
                .build());

        zeneService.addTag(zene.getId(), mentettTag2.getId());
        zene = zeneService.getBycim("Cim");
        assertEquals(1, zene.getTagStringList().size());

        tagService.deleteTagById(mentettTag2.getId());

        zene = zeneService.getBycim("Cim");
        assertEquals(0, zene.getTagStringList().size());
    }
}