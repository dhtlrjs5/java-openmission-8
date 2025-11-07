package com.maple.mapleinfo.domain.cube;

import com.maple.mapleinfo.utils.Grade;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Potential {

    private Grade grade;
    private List<Option> options;
}
