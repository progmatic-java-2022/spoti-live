package hu.progmatic.spotilive.felhasznalo;

import hu.progmatic.spotilive.zene.ZeneService;
import hu.progmatic.spotilive.zenekar.ZenekarDto;
import hu.progmatic.spotilive.zenekar.ZenekarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class FelhasznaloController {
  private final FelhasznaloService felhasznaloService;

  @Autowired
  ZenekarService zenekarService;

  public FelhasznaloController(FelhasznaloService felhasznaloService) {
    this.felhasznaloService = felhasznaloService;
  }

  @GetMapping("/felhasznalo")
  public String lista() {
    return "felhasznalo";
  }

  @PostMapping("/felhasznalo")
  public String add(@ModelAttribute UjFelhasznaloCommand command, Model model) {
    model.addAttribute("ujFelhasznaloError", null);
    command.setRole(UserType.ZENEKAR);
    try {
      felhasznaloService.add(command);
    } catch (FelhasznaloLetrehozasException e) {
      model.addAttribute("ujFelhasznaloError", e.getMessage());
      return "felhasznalo";
    }
    frissit(model);
    return "felhasznalo";
  }

  private void frissit(Model model) {
    model.addAttribute("allFelhasznalo", populateTypes());
  }

  @PostMapping("/felhasznalo/delete/{id}")
  public String delete(@PathVariable Long id, Model model) {
    felhasznaloService.delete(id);
    frissit(model);
    return "redirect:/felhasznalo";
  }

  @ModelAttribute("allFelhasznalo")
  public List<Felhasznalo> populateTypes() {
    if (felhasznaloService.hasRole(UserType.Roles.USER_READ_ROLE)) {
      return felhasznaloService.findAll();
    }
    return List.of();
  }

  @ModelAttribute("ujFelhasznaloCommand")
  public UjFelhasznaloCommand ujFelhasznaloCommand() {
    return new UjFelhasznaloCommand();
  }


  @ModelAttribute("ujFelhasznaloError")
  public String ujFelhasznaloError() {
    return null;
  }

  @ModelAttribute("hasUserWriteRole")
  public boolean userWriteRole() {
    return felhasznaloService.hasRole(UserType.Roles.USER_WRITE_ROLE);
  }

  @ModelAttribute("hasUserReadRole")
  public boolean userReadRole() {
    return felhasznaloService.hasRole(UserType.Roles.USER_READ_ROLE);
  }

  @ModelAttribute("userId")
  public Long userId() {
    return felhasznaloService.getFelhasznaloId();
  }

  @ModelAttribute("allRole")
  public List<String> allRole() {
    return Arrays.stream(UserType.values())
        .map(UserType::name).toList();
  }
  @ModelAttribute("isAdmin")
  public boolean isAdmin() {
    return felhasznaloService.isAdmin();
  }

  @ModelAttribute("zenekarUjFelhasznaloMeghivas")
  public boolean zenekarUjFelhasznaloMeghivas(){
    return felhasznaloService.hasRole(UserType.Roles.NEWGUEST_INVITE_ROLE);
  }

  @ModelAttribute("meghivoKredittelCommand")
  public MeghivoKredittelCommand kreditCommand() {
    return MeghivoKredittelCommand.builder().build();
  }

  @ModelAttribute("osszesZenekar")
  public List<ZenekarDto> osszesZenekar(){
    return zenekarService.findAllDto().stream().toList();
  }



}
