package com.maple.mapleinfo.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.mapleinfo.domain.star_force.StarForceProbability;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Repository
public class StarForceRepository {

    private static final String PROBABILITY_FILE_NAME = "star_force_probabilities.json";
    private static final String COST_FILE_NAME = "star_force_costs.json";
    private static final String PROBABILITY_PATH = "/data/" + PROBABILITY_FILE_NAME;
    private static final String COST_PATH = "/data/" + COST_FILE_NAME;

    private final Map<Integer, StarForceProbability> probabilities;
    private final Map<Integer, Long> costs;

    public StarForceRepository() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            InputStream probabilityStream =
                    getClass().getClassLoader().getResourceAsStream(PROBABILITY_PATH);
            probabilities = mapper.readValue(probabilityStream, new TypeReference<>() {});
        } catch (IOException e) {
            throw new IllegalStateException(PROBABILITY_FILE_NAME + " not found");
        }

        try {
            InputStream probabilityStream =
                    getClass().getClassLoader().getResourceAsStream(COST_PATH);
            costs = mapper.readValue(probabilityStream, new TypeReference<>() {});
        } catch (IOException e) {
            throw new IllegalStateException(COST_FILE_NAME + " not found");
        }
    }

    public StarForceProbability findProbability(int star) {
        return probabilities.get(star);
    }

    public Long findCost(int star) {
        return costs.get(star);
    }
}
