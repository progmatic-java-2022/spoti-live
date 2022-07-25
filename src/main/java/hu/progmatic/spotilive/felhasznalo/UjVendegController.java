package hu.progmatic.spotilive.felhasznalo;

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

@Controller
public class UjVendegController {

    @Autowired
    MeghivoService meghivoService;
    @Autowired
    private EsemenyService esemenyService;


    @PostMapping("/public/ujmeghivo")
    public String ujMeghivo(Model model, @ModelAttribute KreditCommand kreditCommand) {
        MeghivoDto meghivo = meghivoService.meghivoLetrehozasa(kreditCommand.getKreditekSzama());
        model.addAttribute("meghivoUUID", meghivo.getUuid());
        return "/qrkod";
    }

    @GetMapping("/public/meghivo/{uuid}")
    public String vendeg(
            @PathVariable String uuid,
            Model model,
            @ModelAttribute KreditCommand kreditCommand
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
                return "redirect:/login";
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
    public KreditCommand kreditCommand() {
        return KreditCommand.builder().build();
    }

    @ModelAttribute("meghivoUUID")
    public String getMeghivoLink() {
        return null;
    }

    @ModelAttribute("kreditek")
    public String getKreditek() {
        return esemenyService.getKreditekSzama();}

    @ModelAttribute("meghivoFelhasznalvaError")
    public String meghivoFelhasznalvaError() {
        return null;
    }

}
