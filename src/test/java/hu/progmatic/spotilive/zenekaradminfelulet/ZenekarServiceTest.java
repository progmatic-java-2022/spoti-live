package hu.progmatic.spotilive.zenekaradminfelulet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        assertEquals("Teszt Zenekar",mentettDto.getNev());
    }

    @BeforeEach
    void setUp() {
        zenekarService.deleteAll();
    }

    @Nested
    public class LetezoZenekarralTest{

        ZenekarDto testZenekar;

        @BeforeEach
        void setUp() {
            testZenekar = ZenekarDto.builder().nev("Teszt Zenekar").build();
            zenekarService.createZenekar(testZenekar);
        }

        @Test
        @Disabled
        void deleteTest() {
            zenekarService.deleteById(testZenekar.getId());
        }
    }
}