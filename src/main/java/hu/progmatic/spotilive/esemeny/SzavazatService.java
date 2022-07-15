package hu.progmatic.spotilive.esemeny;

import hu.progmatic.spotilive.felhasznalo.FelhasznaloService;
import hu.progmatic.spotilive.zene.ZeneService;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SzavazatService {

    @Autowired
    private ZenekarService zenekarService;
    @Autowired
    private ZeneService zeneService;
    @Autowired
    private FelhasznaloService felhasznaloService;
}
