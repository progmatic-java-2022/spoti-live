package hu.progmatic.spotilive.qrkod;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import javassist.bytecode.ByteArray;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class QRCodeGeneratorTest {

    @Test
    void qrkodGeneratorMukodik() {

        assertThat(QRCodeGenerator.getQRCodeImage("valami", 500, 500)).isInstanceOf(byte[].class);
    }

    @Test
    @Disabled
    void qrkodExeptionTest() {

        String generalasHiba = "";

        try {
            QRCodeGenerator.getQRCodeImage("", 500, 500);
        } catch (RuntimeException e) {
            generalasHiba = e.getMessage();
        }

        assertEquals("Hiba a QR kód generálás közben!", generalasHiba);
    }
}