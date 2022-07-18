package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.felhasznalo.FelhasznaloService;
import hu.progmatic.spotilive.zene.Zene;
import hu.progmatic.spotilive.zene.ZeneService;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class SzavazatService {

    @Autowired
    private ZenekarService zenekarService;
    @Autowired
    private ZeneService zeneService;
    @Autowired
    private FelhasznaloService felhasznaloService;
    @Autowired
    private EsemenyService esemenyService;
    @Autowired
    private SzavazatRepository szavazatRepository;



    public List<SzavazatTracklistDto> getEsemenyTrackList(Integer esemenyId) {
        var esemeny = esemenyService.getEsemenyById(esemenyId);
        var zenekar = esemeny.getZenekar();
        var felhasznaloId = felhasznaloService.getFelhasznaloId();
        var felhasznalo = felhasznaloService.getById(felhasznaloId);
        List<Zene> zeneList = zenekar.getZeneLista();

        return zeneList.stream()
                .map(zene -> SzavazatTracklistDto.factory(zene,esemeny,felhasznalo))
                .sorted(Comparator.comparing(SzavazatTracklistDto::getSzamCim))
                .sorted((o1, o2) -> o2.getOsszSzavazat() - o1.getOsszSzavazat())
                .toList();
    }
}
