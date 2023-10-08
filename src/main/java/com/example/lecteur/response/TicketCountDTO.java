package com.example.lecteur.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketCountDTO {
    private Integer year;
    private Integer month;
    private Long ticketCount;

}
