package com.maple.mapleinfo.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.mapleinfo.domain.star_force.StarForceProbability;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Repository
public class StarForceRepository {

    private static final String PROBABILITY_FILE_NAME = "star_force_probabilities.json";
    private static final String COST_FILE_NAME = "star_force_costs.json";
    private static final String PROBABILITY_PATH = "/data/" + PROBABILITY_FILE_NAME;
    private static final String COST_PATH = "/data/" + COST_FILE_NAME;

    private final JsonNode probabilityRoot;
    private final JsonNode costRoot;

    public StarForceRepository() {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream probabilityStream = getClass().getResourceAsStream(PROBABILITY_PATH)){
            if (probabilityStream == null) {
                throw new FileNotFoundException(PROBABILITY_FILE_NAME + " not found");
            }

            probabilityRoot = mapper.readTree(probabilityStream);
        } catch (IOException e) {
            throw new IllegalStateException(PROBABILITY_FILE_NAME + " 로드 실패");
        }

        try (InputStream costStream = getClass().getResourceAsStream(COST_PATH)){
            if (costStream == null) {
                throw new FileNotFoundException(COST_FILE_NAME + " not found");
            }

            costRoot = mapper.readTree(costStream);
        } catch (IOException e) {
            throw new IllegalStateException(COST_FILE_NAME + " 로드 실패");
        }
    }

    public StarForceProbability findProbability(int star) {
        String stars = String.valueOf(star);
        JsonNode node = probabilityRoot.path(stars);
        if (node.isMissingNode()) {
            return null;
        }

        JsonNode successNode = node.path("success");
        JsonNode failNode = node.path("fail");
        JsonNode destroyNode = node.path("destroy");

        double success = successNode.asDouble();
        double fail = failNode.asDouble();
        double destroy = destroyNode.asDouble();

        return new StarForceProbability(success, fail, destroy);
    }

    public Long findCost(int findStar, int findLevel) {
        String star = String.valueOf(findStar);
        String level = String.valueOf(findLevel);

        JsonNode levelNode = costRoot.path("level");
        JsonNode node = levelNode.path(level);
        JsonNode costNode = node.path(star);

        return costNode.asLong();
    }
}
