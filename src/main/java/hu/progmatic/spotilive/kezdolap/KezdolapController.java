package hu.progmatic.spotilive.kezdolap;

import hu.progmatic.spotilive.felhasznalo.FelhasznaloService;
import hu.progmatic.spotilive.felhasznalo.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;


@Controller
public class KezdolapController {

    @Autowired
    private FelhasznaloService felhasznaloService;

    @RequestMapping("/")
    public String kezdolap() {
        if (felhasznaloService.isGuest()){
            return "redirect:/guestindex";
        } else
        {
            return "index";
        }
    }
}
