package com.example.lecteur.controller;

import com.example.lecteur.model.Employee;
import com.example.lecteur.repository.EmployeeRepository;
import com.example.lecteur.response.Message;
import com.example.lecteur.service.EmployeeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.example.lecteur.utils.Util.*;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;


    @GetMapping("/generateQrCode")
    public boolean getEmployee() throws IOException, WriterException {
        List<Employee> employees = employeeService.getEmployeeByCode();

        if (employees.size() != 0) {
            for (Employee employee : employees) {
                generateQRCode(employee);
            }
            return true;
        }
        return false;
    }

    @GetMapping("/all")
    public List<Employee> getAllEmployee() throws IOException, WriterException {
        List<Employee> employees = employeeService.getEmployees();

        return employees;
    }

    @PostMapping(value = "/addEmployee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> addEmployee(@RequestParam(required = false) MultipartFile image, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String matricule) throws IOException {
        Employee employee = new Employee();
        if (image == null) {
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setMatricule(matricule);
            employeeService.addEmployee(employee);
        } else {
            String fileName = matricule + "_" + image.getOriginalFilename();
            String filePath = PATH_TO_PHOTO_PROFILE + fileName;
            File dest = new File(filePath);
            image.transferTo(dest);

            if (employeeService.findEmployeeByPhotoProfileContains(fileName) != null ||
                    employeeService.findEmployeeByMatriculeContains(matricule) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message("Matricule duplicate"));
            }

            employee.setPhotoProfile(fileName);
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setMatricule(matricule);
            employeeService.addEmployee(employee);
        }
        return ResponseEntity.ok().body(new Message("Employee added successfully!"));
    }

    @GetMapping("/getEmployee/{id}")
    public Employee findById(@PathVariable Integer id) {
        return employeeService.findById(id);
    }

    @GetMapping("/getPhoto/{photoPath}")
    public ResponseEntity<?> findById(@PathVariable String photoPath) throws IOException {
        byte[] images = Files.readAllBytes(new File(PATH_TO_PHOTO_PROFILE + photoPath).toPath());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(images);
    }

    @DeleteMapping("/deleteEmployee/{id}")
    public ResponseEntity<Message> deleteEmplyee(@PathVariable Integer id) {
        Employee employee = employeeService.findById(id);

        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("Employee not found"));
        }

        String fileName = employee.getPhotoProfile();
        String filePath = PATH_TO_PHOTO_PROFILE + fileName;
        String QRCODEPath = QR_CODE_PATH + employee.getId() + SUFIX_QR_CODE;
        System.out.println(QRCODEPath);
        File file = new File(filePath);
        File fileQR = new File(QRCODEPath);
        if (file.exists() || fileQR.exists()) {

            fileQR.delete();
            file.delete();
        }

        employeeService.deleteEmployee(employee);
        return ResponseEntity.ok().body(new Message("Employee deleted successfully!"));

    }


    public boolean generateQRCode(Employee employee) throws WriterException, IOException {
        String qrCodePath = QR_CODE_PATH;
        String qrCodeName = qrCodePath + employee.getId() + SUFIX_QR_CODE;
        var qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(employee.getId() + "\n", BarcodeFormat.QR_CODE,
                QR_CODE_WIDTH, QR_CODE_HEIGHT);
        Path path = FileSystems.getDefault().getPath(qrCodeName);

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        employee.setHasCode(true);
        employeeRepository.save(employee);
        return true;

    }

}
