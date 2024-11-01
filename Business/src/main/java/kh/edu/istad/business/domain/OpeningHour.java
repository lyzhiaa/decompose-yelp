package kh.edu.istad.business.domain;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OpeningHour {
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;

}
