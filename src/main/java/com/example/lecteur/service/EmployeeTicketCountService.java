package com.example.lecteur.service;

import com.example.lecteur.model.Employee;
import com.example.lecteur.model.EmployeeTicketCount;
import com.example.lecteur.model.Ticket;
import com.example.lecteur.repository.EmployeeTicketCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class EmployeeTicketCountService {

    private final EmployeeTicketCountRepository employeeTicketCountRepository;

    public static int Month(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int Year(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public void addTicketAndUpdateCounts(Ticket newTicket) {

        int Month = Month(newTicket.getDateTicket());
        int Year = Year(newTicket.getDateTicket());
        Employee employee = newTicket.getEmployee();
        EmployeeTicketCount existingEntry = employeeTicketCountRepository.findByEmployeeAndYearAndMonth(employee, Year, Month);
        if (existingEntry != null) {
            existingEntry.setTicketCount(existingEntry.getTicketCount() + 1);
            employeeTicketCountRepository.save(existingEntry);
        } else {
            EmployeeTicketCount newEntry = new EmployeeTicketCount();
            newEntry.setEmployee(employee);
            newEntry.setYear(Year);
            newEntry.setMonth(Month);
            newEntry.setTicketCount(1);
            employeeTicketCountRepository.save(newEntry);
        }
    }

    public EmployeeTicketCount getEmployeeTicketCount(Employee employee, int Years, int Months) {
        return employeeTicketCountRepository.findByEmployeeAndYearAndMonth(employee, Years, Months);

    }

    public EmployeeTicketCount getEmployeeTicketCountByidEmployee(Integer idEmployee) {
        return employeeTicketCountRepository.findEmployeeTicketCountByEmployee_IdOrderByIdDesc(idEmployee);
    }
}
