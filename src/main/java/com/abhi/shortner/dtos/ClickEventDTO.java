package com.abhi.shortner.dtos;

import com.abhi.shortner.models.ClickEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class ClickEventDTO {
    private LocalDate clickDate;
    private Long count;



}
