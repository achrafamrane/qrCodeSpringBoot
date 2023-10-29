package com.example.lecteur.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

//    @Column(name = "id_employee")
//    private Integer IdEmployee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private Employee employee;

    //    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "employee_id")
//    @JsonBackReference
//    private Employee employee;
    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", dateTicket=" + dateTicket +
                ", employee=" + (employee != null ? employee.getId() : "null") +
                // Include other fields as needed
                '}';
    }
}
