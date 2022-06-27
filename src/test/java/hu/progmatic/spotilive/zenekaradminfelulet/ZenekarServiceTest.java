package hu.progmatic.spotilive.zenekaradminfelulet;

import org.junit.jupiter.api.Disabled;
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
}