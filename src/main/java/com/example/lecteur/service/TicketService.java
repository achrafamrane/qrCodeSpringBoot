package com.example.lecteur.service;

import com.example.lecteur.model.Ticket;
import com.example.lecteur.repository.TicketRepository;
import com.example.lecteur.response.TicketCountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Ticket getTicketByDateTicket(Date timestamp, Integer id) {

        return ticketRepository.findTicketByDateTicketAndEmployee_Id(timestamp, id);

    }

    public void addTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }


    public List<Object[]> countTicketsByMonthAndYear(Integer employeeId) {
        return ticketRepository.countTicketsByMonthAndYear(employeeId);
    }

    public List<TicketCountDTO> countTicketsByCriteria(Integer employeeId, Integer year, Integer month) {
        List<Object[]> results = ticketRepository.countTicketsByCriteria(employeeId, year, month);

        // Convert Object[] to TicketCountDTO
        return results.stream()
                .map(result -> new TicketCountDTO((Integer) result[0], (Integer) result[1], (Long) result[2]))
                .collect(Collectors.toList());
    }
}
