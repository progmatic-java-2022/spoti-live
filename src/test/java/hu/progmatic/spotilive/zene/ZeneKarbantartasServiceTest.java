package hu.progmatic.spotilive.zene;

import hu.progmatic.spotilive.zene.ZeneDto;
import hu.progmatic.spotilive.zene.ZeneKarbantartasService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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