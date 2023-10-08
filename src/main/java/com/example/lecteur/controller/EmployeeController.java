package com.example.lecteur.controller;

import com.example.lecteur.model.Employee;
import com.example.lecteur.service.EmployeeService;
import com.example.lecteur.utils.QRCodeGenerator;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;


    @GetMapping("/generateQrCode")
    public ResponseEntity<List<Employee>> getEmployee() throws IOException, WriterException {
        List<Employee> employees = employeeService.getEmployees();
        if (employees.size() != 0) {
            for (Employee employee : employees) {
                QRCodeGenerator.generateQRCode(employee);
            }

        }
        return ResponseEntity.ok(employeeService.getEmployees());
    }

    @PostMapping("/addEmployee")
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @GetMapping("/getEmployee/{id}")
    public Employee findById(@PathVariable Integer id) {
        return employeeService.findById(id);
    }


    @DeleteMapping("/deleteEmployee/{id}")
    public void deleteEmplyee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);

    }


}
