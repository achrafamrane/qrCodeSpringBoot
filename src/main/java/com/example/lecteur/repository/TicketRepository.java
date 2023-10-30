package com.example.lecteur.repository;

import com.example.lecteur.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    //    Ticket findTicketByDateTicketAndEmployee(Date date);

    Ticket findTicketByDateTicketAndEmployee_Id(Date timestamp, Integer id);

    @Query("SELECT YEAR(t.dateTicket) as year, MONTH(t.dateTicket) as month, COUNT(t) as ticketCount " +
            "FROM Ticket t " +
            "WHERE (:employeeId IS NULL OR t.employee.id = :employeeId) " +
            "GROUP BY YEAR(t.dateTicket), MONTH(t.dateTicket)")
    List<Object[]> countTicketsByMonthAndYear(@Param("employeeId") Integer employeeId);


    @Query("SELECT YEAR(t.dateTicket) as year, MONTH(t.dateTicket) as month, COUNT(t) as ticketCount " +
            "FROM Ticket t " +
            "WHERE (:employeeId IS NULL OR t.employee.id = :employeeId) " +
            "  AND (:year IS NULL OR YEAR(t.dateTicket) = :year) " +
            "  AND (:month IS NULL OR MONTH(t.dateTicket) = :month) " +
            "GROUP BY YEAR(t.dateTicket), MONTH(t.dateTicket)")
    List<Object[]> countTicketsByCriteria(
            @Param("employeeId") Integer employeeId,
            @Param("year") Integer year,
            @Param("month") Integer month);


}
