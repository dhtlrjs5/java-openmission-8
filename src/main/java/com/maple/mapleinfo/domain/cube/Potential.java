package com.maple.mapleinfo.domain.cube;

import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Potential {

    private Grade grade;
    private List<Option> options;
    private Integer count;
    private CubeType type;
}
