package hu.progmatic.spotilive.appconfig;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Log4j2
@Controller
public class HttpErrorController implements ErrorController {

  @RequestMapping("/error")
  public String handleError(HttpServletRequest request, Model model) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    if (status != null) {
      int statusCode = Integer.parseInt(status.toString());

      if (statusCode == HttpStatus.NOT_FOUND.value()) {
        setTitle(model, "A kért oldal nem található");
      } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
        setTitle(model, "A kért oldalhoz nincsen jogosultsága");
      } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
        setTitle(model, "A kérés feldolgozása közben váratlan hiba történt");
      }
    }
    return "hibaOldal";
  }

  @ModelAttribute("errorPageTitle")
  String errorPageTitle() {
    return "Hiba történt a kérés feldolgozása közben";
  }

  @ModelAttribute("errorPageDetails")
  String errorPageDetails() {
    return "";
  }

  private void setDetails(Model model, String details) {
    model.addAttribute("errorPageDetails", details);
  }

  private void setTitle(Model model, String title) {
    model.addAttribute("errorPageTitle", title);
  }
}
