package hu.progmatic.spotilive.zenekar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
}