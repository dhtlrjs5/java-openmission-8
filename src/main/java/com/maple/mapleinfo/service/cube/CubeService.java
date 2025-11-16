package com.maple.mapleinfo.service.cube;

import com.fasterxml.jackson.databind.JsonNode;
import com.maple.mapleinfo.domain.cube.Option;
import com.maple.mapleinfo.domain.cube.Potential;
import com.maple.mapleinfo.dto.PotentialDto;
import com.maple.mapleinfo.repository.CubeRepository;
import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CubeService {

    private static final double PROBABILITY_SCALE = 100.0;

    private final Random random = new Random();
    private final CubeRepository repository;
    private final CubeRoller roller;

    public Potential useCube(PotentialDto potentialDto) {

        Grade grade = potentialDto.getGrade();
        CubeType type = potentialDto.getType();
        Integer count = potentialDto.getCount();

        List<Option> options = new ArrayList<>();

        Potential initPotential = new Potential(grade, options, count, type);
        return rollCube(initPotential);
    }

    private Potential rollCube(Potential potential) {
        Grade grade = potential.getGrade();
        Integer count = potential.getCount();
        CubeType type = potential.getType();

        Potential newPotential = potential.upgradePotential();
        Grade newGrade = newPotential.getGrade();

        List<Option> options = getOption(grade, newGrade, type);

        return new Potential(grade, options, count + 1, type);
    }

    private List<Option> getOption(Grade grade, Grade newGrade, CubeType type) {
        JsonNode gradeNode = repository.findOptionGradeNode(type, grade);
        JsonNode upgradeNode = repository.findUpgradeProbabilityNode(type, grade);

        if (newGrade != grade) {
            return roller.rollOption(gradeNode);
        }

        newGrade = upgrade(grade, upgradeNode);
        if (newGrade != grade) {
            return roller.rollOption(gradeNode);
        }

        return roller.rollOption(gradeNode);
    }

    private Grade upgrade(Grade grade, JsonNode cube) {

        if (grade.equals(Grade.LEGENDARY)) {
            return grade;
        }

        double randomValue = random.nextDouble() * PROBABILITY_SCALE;

        if (randomValue <= cube.asDouble()) {
            grade = grade.next();
        }

        return grade;
    }

    public Potential reset() {
        return new Potential(Grade.RARE, new ArrayList<>(), 0, CubeType.DEFAULT);
    }
}
