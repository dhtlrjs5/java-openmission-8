package com.maple.mapleinfo.service.cube;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.mapleinfo.domain.cube.CubeStatistics;
import com.maple.mapleinfo.domain.cube.Option;
import com.maple.mapleinfo.domain.cube.Potential;
import com.maple.mapleinfo.dto.CubeDto;
import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class CubeService {

    private static final int RARE_TO_EPIC_DEFAULT_LIMIT = 10;
    private static final int EPIC_TO_UNIQUE_DEFAULT_LIMIT = 42;
    private static final int UNIQUE_TO_LEGENDARY_DEFAULT_LIMIT = 107;
    private static final int RARE_TO_EPIC_ADDITIONAL_LIMIT = 31;
    private static final int EPIC_TO_UNIQUE_ADDITIONAL_LIMIT = 76;
    private static final int UNIQUE_TO_LEGENDARY_ADDITIONAL_LIMIT = 214;

    private static final String DEFAULT_PATH = "/data/cube-weapon.json";
    private static final String ADDITIONAL_PATH = "/data/additional-cube-weapon.json";

    private final JsonNode defaultRoot;
    private final JsonNode additionalRoot;
    private final Random random = new Random();

    public CubeService() {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream inputStream = getClass().getResourceAsStream(DEFAULT_PATH)) {
            if (inputStream == null) {
                throw new FileNotFoundException(DEFAULT_PATH + " not found");
            }
            defaultRoot = mapper.readTree(inputStream);
        } catch (Exception e) {
            throw new IllegalStateException("cube-weapon.json 로드 실패", e);
        }

        try (InputStream inputStream = getClass().getResourceAsStream(ADDITIONAL_PATH)) {
            if (inputStream == null) {
                throw new FileNotFoundException(ADDITIONAL_PATH + " not found");
            }
            additionalRoot = mapper.readTree(inputStream);
        } catch (Exception e) {
            throw new IllegalStateException("additional-cube-weapon.json 로드 실패", e);
        }
    }

    public Potential useCube(Grade grade, CubeType type, Integer count) {

        List<Option> options = new ArrayList<>();

        JsonNode cube = defaultRoot.path("cube");

        if (type.equals(CubeType.ADDITIONAL)) {
            cube = additionalRoot.path("cube");
        }

        Potential initPotential = new Potential(grade, options, count, type);
        return rollCube(initPotential, cube);
    }

    private Potential rollCube(Potential potential, JsonNode cube) {
        Grade grade = potential.getGrade();
        List<Option> options = potential.getOptions();
        Integer count = potential.getCount();
        CubeType type = potential.getType();

        Grade newGrade = applyDefaultLimitSystem(grade, count);

        if (type.equals(CubeType.ADDITIONAL)) {
            newGrade = applyAdditionalLimitSystem(grade, count);
        }

        if (newGrade != grade) {
            rollOption(newGrade, options, cube);
            return new Potential(newGrade, options, 0, type);
        }

        newGrade = upgrade(grade, cube);
        if (newGrade != grade) {
            rollOption(newGrade, options, cube);
            return new Potential(newGrade, options, 0, type);
        }

        rollOption(newGrade, options, cube);
        return new Potential(grade, options, count + 1, type);
    }

    private Grade upgrade(Grade grade, JsonNode cube) {

        if (grade.equals(Grade.LEGENDARY)) {
            return grade;
        }

        JsonNode upgrade = cube.path("upgrade");
        JsonNode probability = upgrade.path(grade.name());

        if (probability.isMissingNode() || probability.isNull()) {
            return grade;
        }

        double randomValue = random.nextDouble() * 100.0;

        if (randomValue <= probability.asDouble()) {
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

    private void rollOption(Grade grade, List<Option> options, JsonNode cube) {
        JsonNode gradeNode = loadGradeNode(grade, cube);
        if (gradeNode == null) {
            throw new IllegalArgumentException("grade 오류");
        }

        JsonNode firstNode = gradeNode.path("first");
        JsonNode secondNode = gradeNode.path("second");
        JsonNode thirdNode = gradeNode.path("third");

        Option first = rollFirstOption(firstNode);
        Option second = rollSecondOption(secondNode);
        Option third = rollThirdOption(thirdNode);

        options.add(first);
        options.add(second);
        options.add(third);
    }

    private JsonNode loadGradeNode(Grade grade, JsonNode cube) {

        if (grade == null) {
            return null;
        }

        JsonNode weaponNode = cube.path("weapon");

        JsonNode gradeNode = weaponNode.path(grade.name());

        if (gradeNode.isMissingNode() || gradeNode.isNull()) {
            return null;
        }

        return gradeNode;
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
            throw new IllegalStateException("잘못된 데이터 입니다.");
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
                String name = node.path("option").asText("알 수 없음");
                String probability = node.path("probability").asText("0%");
                return new Option(name, probability + "%");
            }
        }

        JsonNode fallback = optionsNode.get(0);
        return new Option(
                fallback.path("option").asText("알 수 없음"),
                fallback.path("probability").asText("0%") + "%"
        );
    }

    public CubeDto reset() {
        Potential potential = new Potential(Grade.RARE, new ArrayList<>(), 0, CubeType.DEFAULT);
        CubeStatistics statistics = new CubeStatistics();

        return new CubeDto(potential, statistics);
    }
}
