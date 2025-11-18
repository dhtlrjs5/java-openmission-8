package com.maple.mapleinfo.service.star_force;

import com.maple.mapleinfo.domain.star_force.Equipment;
import com.maple.mapleinfo.domain.star_force.StarForceProbability;
import com.maple.mapleinfo.domain.star_force.StarStatistics;
import com.maple.mapleinfo.dto.StarForceDto;
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

    public StarForceDto enhance(Equipment equipment, StarStatistics statistics) {
        Integer star = equipment.getStar();
        StarStatistics newStatistics = updateEnhancedStatistics(equipment, statistics);
        StarForceProbability probability = repository.findProbability(star);

        Equipment newEquipment = enhancementResult(equipment, newStatistics, probability);
        if (newEquipment.isDestroyed() && !equipment.isDestroyed()) {
            newStatistics =  newStatistics.addDestruction();
        }

        return new StarForceDto(newStatistics, newEquipment);
    }

    private Equipment enhancementResult(
            Equipment equipment,
            StarStatistics statistics,
            StarForceProbability probability
    ) {
        double randomValue = random.nextDouble() * PROBABILITY_SCALE;

        if (randomValue < probability.getSuccess()) {
            return equipment.increaseStar();
        }

        if (randomValue > PROBABILITY_SCALE - probability.getDestroy()) {
            statistics.addDestruction();
            return equipment.destroyed();
        }

        return equipment;
    }

    private StarStatistics updateEnhancedStatistics(Equipment equipment, StarStatistics statistics) {
        Integer star = equipment.getStar();
        Integer level = equipment.getLevel();
        Long cost = repository.findCost(star, level);

        return statistics.addAttempts(cost);
    }

    public Equipment repair(Equipment equipment) {

        if (!equipment.isDestroyed()) {
            return equipment;
        }

        return equipment.repair();
    }

    public StarStatistics updateRepairStatistics(Equipment equipment, StarStatistics statistics) {
        Long price = equipment.getPrice();

        return statistics.addCost(price);
    }

    public StarStatistics reset() {
        return new StarStatistics();
    }
}
