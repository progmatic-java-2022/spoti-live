package hu.progmatic.spotilive.felhasznalo;

import hu.progmatic.spotilive.email.EmailCommand;
import hu.progmatic.spotilive.esemeny.EsemenyService;
import hu.progmatic.spotilive.qrkod.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UjVendegController {

    @Autowired
    MeghivoService meghivoService;
    @Autowired
    private EsemenyService esemenyService;


    @PostMapping("/public/ujmeghivo")
    public String ujMeghivo(@ModelAttribute @Valid MeghivoKredittelCommand meghivoKredittelCommand,
                            BindingResult bindingResult,
                            Model model) {
        if (meghivoKredittelCommand.getEmailCim().isBlank()) {
            MeghivoDto meghivo = meghivoService.meghivoLetrehozasa(meghivoKredittelCommand.getKreditekSzama());
            model.addAttribute("meghivoUUID", meghivo.getUuid());
            if (!bindingResult.hasErrors()) {
                return "/qrkod";
            }
        } else {
            var meghivoKikuldeseEredmenyDto = meghivoService.meghivokKikuldeseEmailben(meghivoKredittelCommand);
            model.addAttribute("kikuldottMeghivokLista", meghivoKikuldeseEredmenyDto);
//            model.addAttribute("kikuldottMeghivokLista", meghivoKikuldeseEredmenyDto.getKikuldottMeghivok());
//            meghivoService.meghivokKikuldeseEmailben(meghivoKredittelCommand);
            return "/sikeresemail";
        }
        return "/felhasznalo";
    }

    @GetMapping("/sikeresemail")
    public String sikeresEmail(
            Model model
    ) {
        return "sikeresemail";
    }

    @GetMapping("/public/meghivo/{uuid}")
    public String vendeg(
            @PathVariable String uuid,
            Model model,
            @ModelAttribute MeghivoKredittelCommand kreditCommand
    ) {
        MeghivoDto meghivo = meghivoService.meghivoLetrehozasa(kreditCommand.getKreditekSzama());
        model.addAttribute("meghivofelhasznalasacommand",
                MeghivoFelhasznalasaCommand.builder().uuid(uuid).build());
        model.addAttribute("meghivoUUID", meghivo.getUuid());
        model.addAttribute("meghivofelhasznalva", meghivoService.meghivoFelVanHasznalva(uuid));
        return "ujvendegletrehozasa";
    }

    @GetMapping("/qrkod")
    public String qrCodePage() {
        return "qrkod";
    }

    @GetMapping("/qrcode/image/{uuid}")
    public void getQrCodeImage(@PathVariable String uuid, HttpServletResponse response) throws IOException {
        var url = "http://167.71.36.154/public/meghivo/" + uuid;
        response.setContentType("image/png");
        byte[] qrCodeImage = QRCodeGenerator.getQRCodeImage(url, 600, 600);
        response.getOutputStream().write(qrCodeImage);
    }

    @PostMapping("/public/meghivo")
    public String add(
            @ModelAttribute("meghivofelhasznalasacommand") @Valid MeghivoFelhasznalasaCommand command,
            BindingResult bindingResult,
            Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                meghivoService.meghivoFelhasznalasa(command);
                return "redirect:/";
            } catch (FelhasznaloLetrehozasException e) {
                bindingResult.addError(
                        new FieldError(
                                "meghivofelhasznalasacommand",
                                e.getMezoNev(),
                                e.getMessage()
                        ));
                return "ujvendegletrehozasa";
            }
        }
        return "ujvendegletrehozasa";
    }

    @ModelAttribute("kreditCommand")
    public MeghivoKredittelCommand kreditCommand() {
        return MeghivoKredittelCommand.builder().build();
    }

    @ModelAttribute("meghivoUUID")
    public String getMeghivoLink() {
        return null;
    }

    @ModelAttribute("kreditek")
    public String getKreditek() {
        return esemenyService.getKreditekSzama();
    }

    @ModelAttribute("kikuldottMeghivokLista")
    public List<MeghivoKikuldeseEredmenyDto> emailEredmenyList() {
        return new ArrayList<>();
    }

    @ModelAttribute("meghivofelhasznalva")
    public boolean meghivofelhasznalva(){
        return false;
    }


}
