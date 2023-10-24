package com.example.lecteur.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_ticket_count")
public class EmployeeTicketCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private Employee employee;

    private Integer year;

    private Integer month;

    @Column(name = "ticket_count")
    private Integer ticketCount;
}
