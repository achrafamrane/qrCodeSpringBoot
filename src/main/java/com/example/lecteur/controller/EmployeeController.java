package com.example.lecteur.controller;

import com.example.lecteur.model.Employee;
import com.example.lecteur.repository.EmployeeRepository;
import com.example.lecteur.response.Message;
import com.example.lecteur.service.EmployeeService;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            String fileName = matricule.substring(0, 3) + "_" + image.getOriginalFilename();
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
        String QRCODEPath = BADGE_CODE_PATH + employee.getId() + SUFIX_BADGE;
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
//        String qrCodePath = QR_CODE_PATH;
//        String qrCodeName = qrCodePath + employee.getId() + SUFIX_QR_CODE;
//        var qrCodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix = qrCodeWriter.encode(employee.getId() + "\n", BarcodeFormat.QR_CODE,
//                QR_CODE_WIDTH, QR_CODE_HEIGHT);
//        Path path = FileSystems.getDefault().getPath(qrCodeName);
//
//        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
//        employee.setHasCode(true);
//        employeeRepository.save(employee);
        try {
            // Compile JRXML to JasperReport
            String jrxmlPath = BADGE_CODE_PATH + "badge.jrxml";

            // Load the compiled JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlPath);

            // Set parameters for the report
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("Nom", employee.getFirstName()); // Replace "param1" with your parameter name
            parameters.put("Prenom", employee.getLastName()); // Replace "param1" with your parameter name
            parameters.put("id", String.valueOf(employee.getId())); // Replace "param1" with your parameter name
            parameters.put("Matricule", String.valueOf(employee.getMatricule())); // Replace "param1" with your parameter name


            // Compile and fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            // Export the report to a PDF file (you can change the format as needed)
            String outputFile = BADGE_CODE_PATH + employee.getId() + SUFIX_BADGE;
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile);

            System.out.println("Report generated successfully at: " + outputFile);
            employee.setHasCode(true);
            employeeRepository.save(employee);
        } catch (JRException e) {
            e.printStackTrace();
        }
        return false;
    }
}




