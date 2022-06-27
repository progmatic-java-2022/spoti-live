package hu.progmatic.spotilive.zenekaradminfelulet;

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
    }

    @Test
    void listaUres() {
        List<ZenekarDto> lekertZenekarok = zenekarService.findAllDto();
        assertThat(lekertZenekarok).hasSize(0);
    }

    @BeforeEach
    void setUp() {
        zenekarService.deleteAll();
    }
    @Nested
    public class LetezoZenekarralTest {


        ZenekarDto testZenekar;

        @BeforeEach
        void setUp() {
            testZenekar = ZenekarDto.builder().nev("Teszt Zenekar").build();
            testZenekar = zenekarService.createZenekar(testZenekar);
        }

        @Test
        void deleteTest() {
            List<ZenekarDto> lekertZenekarok = zenekarService.findAllDto();
            assertThat(lekertZenekarok)
                    .hasSize(1);
            zenekarService.deleteById(testZenekar.getId());
            lekertZenekarok = zenekarService.findAllDto();
            assertThat(lekertZenekarok)
                    .hasSize(0);
        }

        @Test
        void getByNameTest() {
            assertEquals(testZenekar.getNev(),zenekarService.getByName("Teszt Zenekar").getNev());
        }

        @Test
        void editTest() {
            ZenekarDto dto = ZenekarDto.builder()
                    .id(testZenekar.getId())
                    .nev("Edited name")
                    .build();
            var modositott = zenekarService.editZenekarNev(dto);
            assertEquals("Edited name",modositott.getNev());

        }
    }
}