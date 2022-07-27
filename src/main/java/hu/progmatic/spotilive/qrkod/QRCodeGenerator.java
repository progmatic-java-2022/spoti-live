package hu.progmatic.spotilive.qrkod;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRCodeGenerator {
    public static byte[] getQRCodeImage(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageConfig con = new MatrixToImageConfig(
                    Color.BLACK.getRGB(),
                    Color.GREEN.getRGB()
            );

            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, con);
            return pngOutputStream.toByteArray();
        } catch (WriterException|IOException|IllegalArgumentException e) {
            throw new RuntimeException("Hiba a QR kód generálás közben!");
        }
    }
}
