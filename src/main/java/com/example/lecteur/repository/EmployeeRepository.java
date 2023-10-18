package com.example.lecteur.repository;

import com.example.lecteur.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findEmployeeByHasCodeIsNull();

    Employee findEmployeeByPhotoProfileContains(String photoProfileContainsString);

    Employee findEmployeeByMatriculeContains(String maleContainsString);
}
