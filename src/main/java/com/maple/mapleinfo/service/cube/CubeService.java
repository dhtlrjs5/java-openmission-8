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
        CubeType type = potential.getType();

        Potential upgradePotential = potential.upgradePotential();
        Grade newGrade = upgradePotential.getGrade();
        Integer newCount = upgradePotential.getCount();

        return roll(grade, newGrade, type, newCount);
    }

    private Potential roll(Grade grade, Grade newGrade, CubeType type, Integer count) {
        JsonNode gradeNode = repository.findOptionGradeNode(type, grade);
        JsonNode upgradeNode = repository.findUpgradeProbabilityNode(type, grade);

        if (newGrade != grade) {
            gradeNode = repository.findOptionGradeNode(type, newGrade);
            List<Option> options = roller.rollOption(gradeNode);
            return new Potential(newGrade, options, 0, type);
        }

        newGrade = upgrade(grade, upgradeNode);
        if (newGrade != grade) {
            gradeNode = repository.findOptionGradeNode(type, newGrade);
            List<Option> options = roller.rollOption(gradeNode);
            return new Potential(newGrade, options, 0, type);
        }

        List<Option> options = roller.rollOption(gradeNode);
        return new Potential(grade, options, count + 1, type);
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
