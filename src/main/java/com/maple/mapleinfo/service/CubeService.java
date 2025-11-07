package com.maple.mapleinfo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.mapleinfo.domain.cube.Option;
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

    private static final String RESOURCE_PATH = "/data/cube-weapon.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonNode root;
    private final Random random = new Random();

    public CubeService() {
        try (InputStream inputStream = getClass().getResourceAsStream(RESOURCE_PATH)) {
            if (inputStream == null) {
                throw new FileNotFoundException(RESOURCE_PATH);
            }
            root = mapper.readTree(inputStream);
        } catch (Exception e) {
            throw new IllegalStateException("cube-weapon.json 로드 실패", e);
        }
    }

    public List<Option> useCube(Grade grade) {

        List<Option> options = new ArrayList<>();

        JsonNode gradeNode = loadGradeNode(grade);
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

        return options;
    }

    private JsonNode loadGradeNode(Grade grade) {

        if (grade == null) {
            return null;
        }

        JsonNode weaponNode = root.path("cube").path("weapon");

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
}
