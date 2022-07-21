package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.DemoServiceTestHelper;
import hu.progmatic.spotilive.demo.DemoService;

import hu.progmatic.spotilive.zene.ZeneDto;
import hu.progmatic.spotilive.zene.ZeneService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SzavazatServiceMasodikTest {

    @Autowired
    private EsemenyService esemenyService;
    @Autowired
    private SzavazatService szavazatService;
    @Autowired
    private DemoServiceTestHelper demoServiceTestHelper;

    private EsemenyDto esemeny1;
    private ZeneDto zene1;

    @Test
    @Disabled
    @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
    void zeneListWithSzavazatTest() {

        esemeny1 = demoServiceTestHelper.getZenekar1DemoEsemeny();
        zene1 = demoServiceTestHelper.getDemoZenekar1ZeneDto();

        esemenyService.addSzavazat(SzavazatCommand.builder()
                .esemenyId(esemeny1.getId())
                .zeneId(zene1.getId())
                .build());

        esemenyService.addSzavazat(SzavazatCommand.builder()
                .esemenyId(esemeny1.getId())
                .zeneId(zene1.getId())
                .build());


        var szavazatok = szavazatService.getEsemenyTrackList(esemeny1.getId());

        assertEquals(3, szavazatok.stream()
                .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                .findFirst()
                .orElseThrow()
                .getOsszSzavazat());

        assertEquals(2, szavazatok.stream()
                .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                .findFirst()
                .orElseThrow()
                .getSzavazatByFelhasznalo());

        esemenyService.deleteSzavazat(SzavazatCommand.builder()
                .esemenyId(esemeny1.getId())
                .zeneId(zene1.getId())
                .build());

        var szavazatokTorlessel = szavazatService.getEsemenyTrackList(esemeny1.getId());

        assertEquals(2, szavazatokTorlessel.stream()
                .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                .findFirst()
                .orElseThrow()
                .getOsszSzavazat());

        assertEquals(1, szavazatokTorlessel.stream()
                .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                .findFirst()
                .orElseThrow()
                .getSzavazatByFelhasznalo());

    }

    @Test
    @Disabled
    @WithUserDetails(DemoService.ZENEKAR_1_FELHASZNALO)
    void zeneListWithOtherUser() {

        esemeny1 = demoServiceTestHelper.getZenekar1DemoEsemeny();
        zene1 = demoServiceTestHelper.getDemoZenekar1ZeneDto();

        esemenyService.addSzavazat(SzavazatCommand.builder()
                .esemenyId(esemeny1.getId())
                .zeneId(zene1.getId())
                .build());

        var szavazatok = szavazatService.getEsemenyTrackList(esemeny1.getId());

        assertEquals(1, szavazatok.stream()
                .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                .findFirst()
                .orElseThrow()
                .getOsszSzavazat());

        assertEquals(1, szavazatok.stream()
                .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                .findFirst()
                .orElseThrow()
                .getSzavazatByFelhasznalo());

    }

}
