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
        if (
                felhasznaloService.hasRole(UserType.Roles.USER_WRITE_ROLE) ||
                felhasznaloService.hasRole(UserType.Roles.USER_READ_ROLE) ||
                felhasznaloService.hasRole(UserType.Roles.ZENEKAR_KEZELES_ROLE) ||
                felhasznaloService.hasRole(UserType.Roles.ESEMENY_KEZELES_ROLE) ||
                felhasznaloService.hasRole(UserType.Roles.ADMIN_ROLE)) {

            return "index";
        } else {
            return "guestindex";
        }
    }
}
