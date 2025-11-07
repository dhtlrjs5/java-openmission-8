package com.maple.mapleinfo.domain.cube;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Item {

    private final String name;
    private final long level;
    private Potential potential;
    private Potential additionalPotential;
}
