package com.example.lecteur.controller;


import com.example.lecteur.model.Employee;
import com.example.lecteur.model.Ticket;
import com.example.lecteur.response.TicketCountDTO;
import com.example.lecteur.service.EmployeeService;
import com.example.lecteur.service.EmployeeTicketCountService;
import com.example.lecteur.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/checkTicket")
public class CheckController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeTicketCountService employeeTicketCountService;


    @GetMapping("/{id}")
    public ResponseEntity<Object> checkTicket(@PathVariable Integer id) {

        //
        Ticket ticket = ticketService.getTicketByDateTicket(TicketService.removeTime(new Date()), id);
        Employee employee = employeeService.findById(id);
        if (ticket == null || (ticket.getEmployee().getId() != null && !ticket.getEmployee().getId().equals(id))) {
            addNewTicket(employee);

            return ResponseEntity.ok(true);
        } else {

            return ResponseEntity.ok(false);
        }

    }


    // Other endpoints...

    @GetMapping("/count/{employeeId}")
    public ResponseEntity<Object> countTicketsForEmployee(@PathVariable Integer employeeId) {
        try {
            List<TicketCountDTO> ticketCounts = ticketService.countTicketsByCriteria(employeeId, null, null);
            return ResponseEntity.ok(ticketCounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error counting tickets");
        }
    }

    @GetMapping("/count/all")
    public ResponseEntity<Object> countTicketsForAll() {
        try {
            List<TicketCountDTO> ticketCounts = ticketService.countTicketsByCriteria(null, null, null);
            return ResponseEntity.ok(ticketCounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error counting tickets");
        }
    }

    @GetMapping("/count/year/{year}")
    public ResponseEntity<Object> countTicketsForYear(@PathVariable Integer year) {
        try {
            List<TicketCountDTO> ticketCounts = ticketService.countTicketsByCriteria(null, year, null);
            return ResponseEntity.ok(ticketCounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error counting tickets");
        }
    }

    @GetMapping("/count/month")
    public ResponseEntity<Object> countTicketsForMonth(
            @RequestParam(name = "year") Integer year,
            @RequestParam(name = "month") Integer month) {
        try {
            List<TicketCountDTO> ticketCounts = ticketService.countTicketsByCriteria(null, year, month);
            return ResponseEntity.ok(ticketCounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error counting tickets");
        }
    }

    private void addNewTicket(Employee employee) {
        Ticket ticket = new Ticket();
        ticket.setDateTicket(TicketService.removeTime(new Date()));
        ticket.setEmployee(employee);
        ticketService.addTicket(ticket);
        employeeTicketCountService.addTicketAndUpdateCounts(ticket);

    }
}

