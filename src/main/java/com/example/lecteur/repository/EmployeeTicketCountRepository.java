package com.example.lecteur.repository;

import com.example.lecteur.model.Employee;
import com.example.lecteur.model.EmployeeTicketCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeTicketCountRepository extends JpaRepository<EmployeeTicketCount, Long> {

    // Add custom query methods if needed

    // Example custom query method to find counts for a specific employee, year, and month
    EmployeeTicketCount findByEmployeeAndYearAndMonth(Employee employee, Integer year, Integer month);

    EmployeeTicketCount findEmployeeTicketCountByEmployee_IdOrderByIdDesc(Integer Employee_Id);
}