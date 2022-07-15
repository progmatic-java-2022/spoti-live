package hu.progmatic.spotilive.tag;

import hu.progmatic.spotilive.DemoServiceTestHelper;
import hu.progmatic.spotilive.demo.DemoService;
import hu.progmatic.spotilive.zene.CreateZeneCommand;
import hu.progmatic.spotilive.zene.ZeneDto;
import hu.progmatic.spotilive.zene.ZeneService;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TagServiceTest {

    @Autowired
    TagService tagService;
    @Autowired
    ZeneService zeneService;
    @Autowired
    private ZenekarService zenekarService;

    @Autowired
    private DemoServiceTestHelper demoServiceTestHelper;

    private Integer demoZenekarId;

    private List<Integer> testTagIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        demoZenekarId = demoServiceTestHelper.getdemoZeneKar1Id();
    }

    @AfterEach
    void tearDown() {
        testTagIds
                .stream()
                .filter(tagService::isTagExistsById)
                .forEach(tagService::deleteTagById);
    }

    @Test
    void createTagTest() {
        TagDto dto = TagDto.builder()
                .tagNev("Teszt tag")
                .tagKategoria(TagKategoria.MUFAJ)
                .build();

        TagDto mentettTag = createTag(dto);
        assertThat(tagService.getTagById(mentettTag.getId())).isNotNull();

    }

    private TagDto createTag(TagDto dto) {
        tagService.getAllTag()
            .stream()
            .filter(tag -> tag.getTagNev().equals(dto.getTagNev()))
            .forEach(tag -> tagService.deleteTagById(tag.getId()));
        TagDto tag = tagService.createTag(dto);
        testTagIds.add(tag.getId());
        return tag;
    }

    @Test
    void editTagTest() {
        TagDto dto = TagDto.builder()
                .tagNev("Teszt tag")
                .tagKategoria(TagKategoria.MUFAJ)
                .build();

        TagDto mentettTag = createTag(dto);

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

        createTag(dto);
        createTag(dto2);

        assertThat(tagService.getAllTag())
                .extracting(TagDto::getTagNev)
                .containsAll(List.of("Teszt tag", "Teszt tag 2"));
    }

    @Test
    @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
    void deleteTagByIdTest() {
        TagDto dto = TagDto.builder()
                .tagNev("Teszt tag")
                .tagKategoria(TagKategoria.MUFAJ)
                .build();

        TagDto dto2 = TagDto.builder()
                .tagNev("Teszt tag 2")
                .tagKategoria(TagKategoria.HANGULAT)
                .build();


        TagDto mentettTag = createTag(dto);
        TagDto mentettTag2 = createTag(dto2);


        tagService.deleteTagById(mentettTag.getId());

        List<TagDto> osszesTag = tagService.getAllTag();

        assertThat(osszesTag)
                .extracting(TagDto::getTagNev)
                .contains("Teszt tag 2")
                .doesNotContain("Teszt tag");
    }

}