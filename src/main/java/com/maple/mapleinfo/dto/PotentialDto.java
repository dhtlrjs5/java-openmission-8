package com.maple.mapleinfo.dto;

import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PotentialDto {

    private Grade grade;
    private Integer count;
    private CubeType type;
}
