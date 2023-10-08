package com.example.lecteur.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ticket {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "date_ticket")
    @Temporal(TemporalType.DATE)
    private Date dateTicket;

    @Column(name = "id_employee")
    private Integer IdEmployee;
    //    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "employee_id")
//    @JsonBackReference
//    private Employee employee;

}
