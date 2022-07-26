package hu.progmatic.spotilive.kezdolap;

import hu.progmatic.spotilive.felhasznalo.FelhasznaloService;
import hu.progmatic.spotilive.felhasznalo.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    @ModelAttribute("guestFelhasznalo")
    public boolean guestFelhasznalo(){
        return felhasznaloService.hasRole(UserType.Roles.GUEST_READ_ROLE);
    }
    @ModelAttribute("zenekarFelhasznalo")
    public boolean zenekarFelhasznalo(){
        return felhasznaloService.hasRole(UserType.Roles.ZENE_KEZELES_ROLE);
    }
    @ModelAttribute("zenekarUjFelhasznalo")
    public boolean zenekarUjFelhasznalo(){
        return felhasznaloService.hasRole(UserType.Roles.NEWGUEST_CREATE_ROLE);
    }
    @ModelAttribute("adminFelhasznalo")
    public boolean adminFelhasznalo(){
        return felhasznaloService.hasRole(UserType.Roles.ADMIN_ROLE);
    }
}
