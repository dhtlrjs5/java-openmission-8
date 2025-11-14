package com.maple.mapleinfo.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.mapleinfo.domain.star_force.StarForceProbability;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static com.maple.mapleinfo.utils.ErrorMessages.*;

@Repository
public class StarForceRepository {

    private static final String PROBABILITY_FILE_NAME = "star_force_probabilities.json";
    private static final String COST_FILE_NAME = "star_force_costs.json";
    private static final String PROBABILITY_PATH = "/data/" + PROBABILITY_FILE_NAME;
    private static final String COST_PATH = "/data/" + COST_FILE_NAME;

    private static final String ERROR_PROBABILITY_FILE_NOT_FOUND = PROBABILITY_FILE_NAME + SUFFIX_NOT_FOUND;
    private static final String ERROR_PROBABILITY_FILE_FAIL_LOADING = PROBABILITY_FILE_NAME + SUFFIX_FAIL_LOADING;
    private static final String ERROR_COST_FILE_NOT_FOUND = COST_FILE_NAME + SUFFIX_NOT_FOUND;
    private static final String ERROR_COST_FILE_FAIL_LOADING = COST_FILE_NAME + SUFFIX_FAIL_LOADING;

    private final JsonNode probabilityRoot;
    private final JsonNode costRoot;

    public StarForceRepository() {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream probabilityStream = getClass().getResourceAsStream(PROBABILITY_PATH)){
            if (probabilityStream == null) {
                throw new FileNotFoundException(ERROR_PROBABILITY_FILE_NOT_FOUND);
            }

            probabilityRoot = mapper.readTree(probabilityStream);
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_PROBABILITY_FILE_FAIL_LOADING);
        }

        try (InputStream costStream = getClass().getResourceAsStream(COST_PATH)){
            if (costStream == null) {
                throw new FileNotFoundException(ERROR_COST_FILE_NOT_FOUND);
            }

            costRoot = mapper.readTree(costStream);
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_COST_FILE_FAIL_LOADING);
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
