package com.maple.mapleinfo.service.cube;

import com.fasterxml.jackson.databind.JsonNode;
import com.maple.mapleinfo.domain.cube.CubeStatistics;
import com.maple.mapleinfo.domain.cube.Option;
import com.maple.mapleinfo.domain.cube.Potential;
import com.maple.mapleinfo.dto.CubeDto;
import com.maple.mapleinfo.dto.PotentialDto;
import com.maple.mapleinfo.repository.CubeRepository;
import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.maple.mapleinfo.utils.ErrorMessages.*;

@Service
@RequiredArgsConstructor
public class CubeService {

    private static final int RARE_TO_EPIC_DEFAULT_LIMIT = 10;
    private static final int EPIC_TO_UNIQUE_DEFAULT_LIMIT = 42;
    private static final int UNIQUE_TO_LEGENDARY_DEFAULT_LIMIT = 107;
    private static final int RARE_TO_EPIC_ADDITIONAL_LIMIT = 31;
    private static final int EPIC_TO_UNIQUE_ADDITIONAL_LIMIT = 76;
    private static final int UNIQUE_TO_LEGENDARY_ADDITIONAL_LIMIT = 214;

    private static final int RESET_COUNT = 0;
    private static final double PROBABILITY_SCALE = 100.0;
    private static final String DEFAULT_PROBABILITY_VALUE = "0%";

    private final Random random = new Random();
    private final CubeRepository repository;

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

        JsonNode gradeNode = repository.findOptionGradeNode(type, grade);
        JsonNode upgradeNode = repository.findUpgradeProbabilityNode(type, grade);
        Grade newGrade = applyDefaultLimitSystem(grade, count);

        if (type.equals(CubeType.ADDITIONAL)) {
            newGrade = applyAdditionalLimitSystem(grade, count);
        }

        if (newGrade != grade) {
            List<Option> options = rollOption(gradeNode);
            return new Potential(newGrade, options, RESET_COUNT, type);
        }

        newGrade = upgrade(grade, upgradeNode);
        if (newGrade != grade) {
            List<Option> options = rollOption(gradeNode);
            return new Potential(newGrade, options, RESET_COUNT, type);
        }

        List<Option> options = rollOption(gradeNode);
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

    private Grade applyDefaultLimitSystem(Grade grade, int count) {
        switch (grade) {
            case RARE:
                if (count >= RARE_TO_EPIC_DEFAULT_LIMIT) return Grade.EPIC;
                break;
            case EPIC:
                if (count >= EPIC_TO_UNIQUE_DEFAULT_LIMIT) return Grade.UNIQUE;
                break;
            case UNIQUE:
                if (count >= UNIQUE_TO_LEGENDARY_DEFAULT_LIMIT) return Grade.LEGENDARY;
                break;
        }
        return grade;
    }

    private Grade applyAdditionalLimitSystem(Grade grade, int count) {
        switch (grade) {
            case RARE:
                if (count >= RARE_TO_EPIC_ADDITIONAL_LIMIT) return Grade.EPIC;
                break;
            case EPIC:
                if (count >= EPIC_TO_UNIQUE_ADDITIONAL_LIMIT) return Grade.UNIQUE;
                break;
            case UNIQUE:
                if (count >= UNIQUE_TO_LEGENDARY_ADDITIONAL_LIMIT) return Grade.LEGENDARY;
                break;
        }
        return grade;
    }

    private List<Option> rollOption(JsonNode gradeNode) {
        List<Option> rollOptions = new ArrayList<>();

        JsonNode firstNode = gradeNode.path("first");
        JsonNode secondNode = gradeNode.path("second");
        JsonNode thirdNode = gradeNode.path("third");

        Option first = rollFirstOption(firstNode);
        Option second = rollSecondOption(secondNode);
        Option third = rollThirdOption(thirdNode);

        rollOptions.add(first);
        rollOptions.add(second);
        rollOptions.add(third);

        return rollOptions;
    }

    private Option rollFirstOption(JsonNode node) {
        return getRandomOption(node);
    }

    private Option rollSecondOption(JsonNode node) {
        return getRandomOption(node);
    }

    private Option rollThirdOption(JsonNode node) {
        return getRandomOption(node);
    }

    private Option getRandomOption(JsonNode optionsNode) {
        if (optionsNode.isMissingNode() || optionsNode.isNull()) {
            throw new IllegalStateException(ERROR_NOT_FOUND_DATA);
        }

        double totalProbability = 0.0;
        for (JsonNode node : optionsNode) {
            totalProbability += node.path("probability").asDouble();
        }

        double randomValue = random.nextDouble() * totalProbability;

        double cumulativeProbability = 0.0;
        for (JsonNode node : optionsNode) {
            cumulativeProbability += node.path("probability").asDouble();
            if (randomValue <= cumulativeProbability) {
                String name = node.path("option").asText(ERROR_NOT_FOUND_DATA);
                String probability = node.path("probability").asText(DEFAULT_PROBABILITY_VALUE);
                return new Option(name, probability + "%");
            }
        }

        JsonNode fallback = optionsNode.get(0);
        return new Option(
                fallback.path("option").asText(ERROR_NOT_FOUND_DATA),
                fallback.path("probability").asText(DEFAULT_PROBABILITY_VALUE) + "%"
        );
    }

    public Potential reset() {
        return new Potential(Grade.RARE, new ArrayList<>(), 0, CubeType.DEFAULT);
    }
}
