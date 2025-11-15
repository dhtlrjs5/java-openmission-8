package com.maple.mapleinfo.service.star_force;

import com.maple.mapleinfo.domain.star_force.Equipment;
import com.maple.mapleinfo.domain.star_force.StarForceProbability;
import com.maple.mapleinfo.domain.star_force.StarStatistics;
import com.maple.mapleinfo.repository.StarForceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class StarForceService {

    private static final double PROBABILITY_SCALE = 100.0;

    private final Random random = new Random();
    private final StarForceRepository repository;

    public Equipment enhance(Equipment equipment, StarStatistics statistics) {

        Integer star = updateStatistics(equipment, statistics);
        StarForceProbability probability = repository.findProbability(star);

        return enhancementResult(equipment, statistics,  probability);
    }

    private Equipment enhancementResult(
            Equipment equipment,
            StarStatistics statistics,
            StarForceProbability probability
    ) {
        double randomValue = random.nextDouble() * PROBABILITY_SCALE;

        if (randomValue < probability.getSuccess()) {
            equipment.increaseStar();
            return equipment;
        }

        if (randomValue > PROBABILITY_SCALE - probability.getDestroy()) {
            equipment.destroyedEquipment();
            statistics.addDestruction();
            return equipment;
        }

        return equipment;
    }

    private Integer updateStatistics(Equipment equipment, StarStatistics statistics) {
        Integer star = equipment.getStar();
        Integer level = equipment.getLevel();
        Long cost = repository.findCost(star, level);

        statistics.addCost(cost);
        statistics.addAttempts();

        return star;
    }

    public Equipment repair(Equipment equipment, StarStatistics statistics) {

        if (!equipment.isDestroyed()) {
            return equipment;
        }

        Long price = equipment.getPrice();

        equipment.repair();
        statistics.addCost(price);

        return equipment;
    }

    public StarStatistics reset() {
        return new StarStatistics();
    }
}
