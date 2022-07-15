package hu.progmatic.spotilive;

import hu.progmatic.spotilive.demo.DemoService;
import hu.progmatic.spotilive.esemeny.EsemenyDto;
import hu.progmatic.spotilive.esemeny.EsemenyService;
import hu.progmatic.spotilive.tag.TagDto;
import hu.progmatic.spotilive.tag.TagService;
import hu.progmatic.spotilive.zene.ZeneDto;
import hu.progmatic.spotilive.zene.ZeneService;
import hu.progmatic.spotilive.zenekar.ZenekarDto;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceTestHelper {

    @Autowired
    ZeneService zeneService;

    @Autowired
    ZenekarService zenekarService;

    @Autowired
    TagService tagservice;
    @Autowired
    private EsemenyService esemenyService;


    public Integer getdemoZeneKar1Id() {
        return getDemoZenekar1().getId();
    }

    private ZenekarDto getDemoZenekar1() {
        return zenekarService.getByName(DemoService.PREFIX1 + DemoService.DEMO_ZENEKAR);
    }

    public ZeneDto getDemoZenekar1ZeneDto() {
        return zeneService.getZeneByNev(DemoService.PREFIX1 + DemoService.DEMO_ZENE);
    }
    public ZeneDto getDemoZenekar2ZeneDto() {
        return zeneService.getZeneByNev(DemoService.PREFIX2 + DemoService.DEMO_ZENE);
    }

    public TagDto getDemoTagDto(){
        return tagservice.getTagDtoByNev(DemoService.DEMO_TAG);
    }

    public String getdemoZeneKarNev() {
        return getDemoZenekar1().getNev();
    }

    public Integer getZenekar1demoEsemenyId() {
        return getZenekar1DemoEsemeny().getId();
    }

    public EsemenyDto getZenekar1DemoEsemeny() {
        return esemenyService.getByName(DemoService.PREFIX1 + DemoService.DEMO_ESEMENY);
    }
    public EsemenyDto getZenekar2DemoEsemeny() {
        return esemenyService.getByName(DemoService.PREFIX2 + DemoService.DEMO_ESEMENY);
    }
}
