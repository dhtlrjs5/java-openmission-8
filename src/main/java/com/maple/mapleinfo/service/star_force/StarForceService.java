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

    private final StarForceRepository repository;
    private final Random random = new Random();

    public Equipment enhance(Equipment equipment, StarStatistics statistics) {

        int currentStar = equipment.getStar();
        StarForceProbability probability = repository.findProbability(currentStar);
        Long cost = repository.findCost(currentStar);
        statistics.addCost(cost);
        statistics.addAttempts();

        double randomValue = random.nextDouble() * 100.0;

        if (randomValue < probability.getSuccess()) {
            equipment.increaseStar();
            return equipment;
        }

        if (randomValue > 100.0 - probability.getDestroy()) {
            equipment.destroyedEquipment();
            statistics.addDestruction();
            return equipment;
        }

        equipment.failEnhancement();
        return equipment;
    }
}
