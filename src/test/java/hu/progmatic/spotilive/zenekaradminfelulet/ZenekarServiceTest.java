package hu.progmatic.spotilive.zenekaradminfelulet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ZenekarServiceTest {

    @Autowired
    ZenekarService zenekarService;


    @Test
    void createZenekar() {
        ZenekarDto zenekarDto = ZenekarDto.builder().nev("Teszt Zenekar").build();
        ZenekarDto mentettDto = zenekarService.createZenekar(zenekarDto);

        assertNotNull(mentettDto.getId());
        assertEquals("Teszt Zenekar", mentettDto.getNev());
        zenekarService.deleteById(mentettDto.getId());
    }

    @Nested
    public class LetezoZenekarralTest {
        ZenekarDto testZenekar;

        @AfterEach
        void tearDown() {
            zenekarService.deleteById(testZenekar.getId());
        }

        @BeforeEach
        void setUp() {
            testZenekar = ZenekarDto.builder().nev("Teszt Zenekar").build();
            testZenekar = zenekarService.createZenekar(testZenekar);
        }

        @Test
        void deleteTest() {
            ZenekarDto deletezenekar = zenekarService.createZenekar(ZenekarDto.builder().nev("Delete zenekar").build());
            List<ZenekarDto> lekertZenekarok = zenekarService.findAllDto();
            assertThat(lekertZenekarok)
                    .extracting(ZenekarDto::getNev)
                    .contains("Delete zenekar");
            zenekarService.deleteById(deletezenekar.getId());
            lekertZenekarok = zenekarService.findAllDto();
            assertThat(lekertZenekarok)
                    .extracting(ZenekarDto::getNev)
                    .doesNotContain("Delete zenekar");
        }

        @Test
        void getByNameTest() {
            assertEquals(testZenekar.getNev(), zenekarService.getByName("Teszt Zenekar").getNev());
        }

        @Test
        void editTest() {
            ZenekarDto dto = ZenekarDto.builder()
                    .id(testZenekar.getId())
                    .nev("Edited name")
                    .build();
            var modositott = zenekarService.editZenekarNev(dto);
            assertEquals("Edited name", modositott.getNev());

        }

        @Test
        void getByIdTest() {

            assertEquals(testZenekar.getId(), zenekarService.getById(testZenekar.getId()).getId());
        }
    }
}