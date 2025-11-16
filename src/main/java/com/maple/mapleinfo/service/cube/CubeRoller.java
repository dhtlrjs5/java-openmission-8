package com.maple.mapleinfo.service.cube;

import com.fasterxml.jackson.databind.JsonNode;
import com.maple.mapleinfo.domain.cube.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.maple.mapleinfo.utils.ErrorMessages.ERROR_NOT_FOUND_DATA;

@Component
@RequiredArgsConstructor
public class CubeRoller {

    private final Random random = new Random();

    public List<Option> rollOption(JsonNode gradeNode) {
        List<Option> rollOptions = new ArrayList<>();

        JsonNode firstNode = gradeNode.path("first");
        JsonNode secondNode = gradeNode.path("second");
        JsonNode thirdNode = gradeNode.path("third");

        Option firstOption = getRandomOption(firstNode);
        Option secondOption = getRandomOption(secondNode);
        Option thirdOption = getRandomOption(thirdNode);

        rollOptions.add(firstOption);
        rollOptions.add(secondOption);
        rollOptions.add(thirdOption);

        return rollOptions;
    }

    private Option getRandomOption(JsonNode optionsNode) {
        double cumulativeProbability = calculateCumulativeProbability(optionsNode);
        double randomValue = random.nextDouble() * cumulativeProbability;

        return selectOption(optionsNode, randomValue);
    }

    private double calculateCumulativeProbability(JsonNode optionsNode) {
        double cumulativeProbability = 0.0;

        for (JsonNode node : optionsNode) {
            JsonNode probabilityNode = node.path("probability");
            cumulativeProbability += probabilityNode.asDouble();
        }

        return cumulativeProbability;
    }

    private Option selectOption(JsonNode optionsNode, double randomValue) {
        double cumulativeProbability = 0.0;

        for (JsonNode node : optionsNode) {
            JsonNode probabilityNode = node.path("probability");
            cumulativeProbability += probabilityNode.asDouble();

            if (randomValue <= cumulativeProbability) {
                return createOption(node);
            }
        }

        return fallback(optionsNode);
    }

    private static Option createOption(JsonNode node) {
        JsonNode optionNode = node.path("option");
        JsonNode probabilityNode = node.path("probability");

        String name = optionNode.asText(ERROR_NOT_FOUND_DATA);
        String probability = probabilityNode.asText(ERROR_NOT_FOUND_DATA);

        return new Option(name, probability + "%");
    }

    private Option fallback(JsonNode optionsNode) {
        JsonNode fallback = optionsNode.get(0);
        JsonNode optionNode = fallback.path("option");
        JsonNode probabilityNode = fallback.path("probability");

        return new Option(
                optionNode.asText(ERROR_NOT_FOUND_DATA),
                probabilityNode.asText(ERROR_NOT_FOUND_DATA)
        );
    }
}