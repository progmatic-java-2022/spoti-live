package hu.progmatic.spotilive;

import hu.progmatic.spotilive.demo.DemoService;
import hu.progmatic.spotilive.tag.TagDto;
import hu.progmatic.spotilive.tag.TagService;
import hu.progmatic.spotilive.zene.ZeneDto;
import hu.progmatic.spotilive.zene.ZeneService;
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

    public Integer getdemoZeneKarId() {
        return zenekarService.getByName(DemoService.DEMO_ZENEKAR).getId();
    }

    public ZeneDto getDemoZeneDto() {
        return zeneService.getZeneByNev(DemoService.DEMO_ZENE);
    }

    public TagDto getDemoTagDto(){
        return tagservice.getTagDtoByNev(DemoService.DEMO_TAG);
    }
}
