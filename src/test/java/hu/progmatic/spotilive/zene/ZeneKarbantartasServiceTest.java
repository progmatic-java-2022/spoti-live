package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.zenekar.ZenekarDto;
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
        @Disabled
        void addTagTest() {
            TagDto dto = TagDto.builder()
                    .tagNev("Teszt tag")
                    .zeneSzam(testZene)
                    .build();

            zeneKarbantartasService.addTag(dto);

            var hozzaadott = zeneKarbantartasService.getById(testZene.getId());
            assertNotNull(hozzaadott.getTagDtoList().get(0).getId());
            assertEquals("Teszt tag",hozzaadott.getTagDtoList().get(0).getTagNev());
        }
    }


}