package com.example.lecteur.service;

import com.example.lecteur.model.Employee;
import com.example.lecteur.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;


    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);

    }

    public Employee findById(Integer id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Couldn't find employee with id " + id));
    }

    public void deleteEmployee(Employee employee) {

        employeeRepository.delete(employee);

    }

    public List<Employee> getEmployeeByCode() {
        return employeeRepository.findEmployeeByHasCodeIsNull();
    }

    public void updateEmployeeByCode(Employee employee) {

        employeeRepository.save(employee);
    }

    public Employee findEmployeeByPhotoProfileContains(String photoProfile) {
        return employeeRepository.findEmployeeByPhotoProfileContains(photoProfile);
    }

    public Employee findEmployeeByMatriculeContains(String matricule) {
        return employeeRepository.findEmployeeByMatriculeContains(matricule);
    }
}
