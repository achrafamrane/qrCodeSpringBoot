package com.example.lecteur.utils;

import com.example.lecteur.model.Employee;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.var;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static com.example.lecteur.utils.Util.*;

public class QRCodeGenerator {

    public static void generateQRCode(Employee employee) throws WriterException, IOException {
        String qrCodePath = QR_CODE_PATH;
        String qrCodeName = qrCodePath + employee.getId() + "-QR.png";
        var qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode("Id:" + employee.getId() + "\n", BarcodeFormat.QR_CODE,
                QR_CODE_WIDTH, QR_CODE_HEIGHT);
        Path path = FileSystems.getDefault().getPath(qrCodeName);

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

    }

}
