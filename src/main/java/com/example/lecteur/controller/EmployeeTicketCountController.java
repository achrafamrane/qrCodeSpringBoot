package com.example.lecteur.controller;

import com.example.lecteur.model.EmployeeTicketCount;
import com.example.lecteur.service.EmployeeTicketCountService;
import com.example.lecteur.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/countTicket")
public class EmployeeTicketCountController {
    @Autowired
    private EmployeeTicketCountService employeeTicketCountService;

    @Autowired
    private TicketService ticketService;

    @GetMapping("/{id}")
    public EmployeeTicketCount findById(@PathVariable Integer id) {
        return employeeTicketCountService.getEmployeeTicketCountByidEmployee(id);
    }


}
