package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.DemoServiceTestHelper;
import hu.progmatic.spotilive.demo.DemoService;

import hu.progmatic.spotilive.zene.ZeneDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SzavazatServiceTest {

    @Autowired
    private EsemenyService esemenyService;
    @Autowired
    private SzavazatService szavazatService;
    @Autowired
    private DemoServiceTestHelper demoServiceTestHelper;

    private EsemenyDto esemeny1;
    private ZeneDto zene1;

    @Test
    @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
    void zeneListOsszesTest() {
        esemeny1 = demoServiceTestHelper.getZenekar1DemoEsemeny();
        List<SzavazatTracklistDto> tracklistBySzavazat = szavazatService.getEsemenyTrackList(esemeny1.getId());
        assertThat(tracklistBySzavazat)
                .hasSize(3)
                .extracting(SzavazatTracklistDto::getOsszSzavazat)
                .contains(0);
        assertThat(tracklistBySzavazat)
                .hasSize(3)
                .extracting(SzavazatTracklistDto::getSzamCim)
                .containsExactly(
                        "1_zenekar_Demo zene cím",
                        "1_zenekar_Demo zene cím 3",
                        "1_zenekar_Demo zene cím 2"
                );
    }

    @Test
    @WithUserDetails(DemoService.ADMIN_FELHASZNALO)
    void zeneListWithSzavazatTest() {

        esemeny1 = demoServiceTestHelper.getZenekar1DemoEsemeny();
        zene1 = demoServiceTestHelper.getDemoZenekar1ZeneDto();

        var szavazatok = szavazatService.getEsemenyTrackList(esemeny1.getId());

        assertEquals(2, szavazatok.stream()
                .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                .findFirst()
                .orElseThrow()
                .getOsszSzavazat());

        assertEquals(2, szavazatok.stream()
                .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                .findFirst()
                .orElseThrow()
                .getSzavazatByFelhasznalo());

    }

    @Test
    @WithUserDetails(DemoService.ZENEKAR_1_FELHASZNALO)
    void zeneSzavazatListandDeleteWithOtherUser() {

        esemeny1 = demoServiceTestHelper.getZenekar1DemoEsemeny();
        zene1 = demoServiceTestHelper.getDemoZenekar1ZeneDto();

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

        assertEquals(1, szavazatok.stream()
                .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                .findFirst()
                .orElseThrow()
                .getSzavazatByFelhasznalo());

        esemenyService.deleteSzavazat(SzavazatCommand.builder()
                .zeneId(zene1.getId())
                .esemenyId(esemeny1.getId())
                .build());

        var szavazatokTorles = szavazatService.getEsemenyTrackList(esemeny1.getId());

        assertEquals(2, szavazatokTorles.stream()
                .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                .findFirst()
                .orElseThrow()
                .getOsszSzavazat());

        assertEquals(0, szavazatokTorles.stream()
                .filter(szavazatTracklistDto -> szavazatTracklistDto.getZeneId().equals(zene1.getId()))
                .findFirst()
                .orElseThrow()
                .getSzavazatByFelhasznalo());

    }

}
