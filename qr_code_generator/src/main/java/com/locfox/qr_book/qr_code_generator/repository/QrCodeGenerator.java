package com.locfox.qr_book.qr_code_generator.repository;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QrCodeGenerator {

    @Value("${qr_code.host}")
    private String host;

    /// Standard width of qr-code result image
    public static final int WIDTH = 300;

    /// Standard height of qr-code result image
    public static final int HEIGHT = 300;

    /// Generates a qr code from containing the content: `host name` + `uuid`.
    ///
    /// For example:
    ///
    /// ```
    /// http://example.org/71724c72-a2c2-4d3f-ac26-085d408b82a4
    /// ```
    ///
    /// The host name can be configured in the `application.yml` configuration file
    ///
    /// @param uuid represents the link id
    /// @return png image byte array
    /// @throws WriterException if contents cannot be encoded legally in a format
    /// @throws IOException if contents cannot be encoded legally in a format
    @Cacheable(value = "qr", key = "#uuid")
    public byte[] generate(String uuid) throws WriterException, IOException {
        var qrCodeWriter = new QRCodeWriter();
        var bitMatrix = qrCodeWriter.encode(host + uuid, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);

        try (var byteArray = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArray);
            return byteArray.toByteArray();
        }
    }

}
