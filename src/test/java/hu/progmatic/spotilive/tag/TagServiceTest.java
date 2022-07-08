package hu.progmatic.spotilive.tag;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TagServiceTest {

    @Autowired
    TagService tagService;

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
        assertEquals("Teszt Edited Tag" ,tesztDto.getTagNev());
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

        assertThat(tagService.getAllTag()).hasSize(5);

    }
}