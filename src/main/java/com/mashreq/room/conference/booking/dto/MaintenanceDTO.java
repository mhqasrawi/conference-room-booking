package com.mashreq.room.conference.booking.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceDTO {

    private Long id;
    @NotEmpty(message = "Start time cannot be empty.")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "The time must be in HH:MM format.")

    private String startTime;
    @NotEmpty(message = "End time cannot be empty.")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "The time must be in HH:MM format.")

    private String endTime;

}
