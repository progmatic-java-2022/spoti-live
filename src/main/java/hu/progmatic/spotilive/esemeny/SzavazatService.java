package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.felhasznalo.FelhasznaloService;
import hu.progmatic.spotilive.zene.Zene;
import hu.progmatic.spotilive.zene.ZeneService;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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


    public SzavazatDto createSzavazat(SzavazatDto dto){
        var zene = zeneService.getZeneById(dto.getZeneId());
        var esemeny = esemenyService.getEsemenyById(dto.getEsemenyId());
        Szavazat szavazat = Szavazat.builder()
                .esemeny(esemeny)
                .zene(zene)
                .build();
        zene.getSzavazatok().add(szavazat);
        esemeny.getZenek().add(szavazat);
       return SzavazatDto.factory(szavazatRepository.save(szavazat));
    }


    public List<SzavazatTracklistDto> getEsemenyTrackList(Integer esemenyId) {
        var esemeny = esemenyService.getEsemenyById(esemenyId);
        var zenekar = esemeny.getZenekar();
        List<Zene> zeneList = zenekar.getZeneLista();

        return zeneList.stream()
                .map(zene -> SzavazatTracklistDto.factory(zene,esemeny))
                .toList();
    }
}
