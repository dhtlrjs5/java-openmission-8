package com.maple.mapleinfo.dto;

import com.maple.mapleinfo.domain.star_force.Equipment;
import com.maple.mapleinfo.domain.star_force.StarStatistics;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StarForceDto {

    private final StarStatistics statistics;
    private final Equipment equipment;
}
