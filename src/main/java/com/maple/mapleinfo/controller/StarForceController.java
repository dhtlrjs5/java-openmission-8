package com.maple.mapleinfo.controller;

import com.maple.mapleinfo.domain.star_force.Equipment;
import com.maple.mapleinfo.domain.star_force.StarStatistics;
import com.maple.mapleinfo.domain.star_force.StarStatus;
import com.maple.mapleinfo.dto.StarForceDto;
import com.maple.mapleinfo.service.star_force.StarForceService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/star")
@RequiredArgsConstructor
public class StarForceController {

    private final StarForceService service;

    @GetMapping()
    public String showStar() {
        return "star";
    }

    @PostMapping("/enhance")
    @ResponseBody
    public StarForceDto enhance(HttpSession session) {
        StarStatistics statistics = getStatistics(session);
        Equipment equipment = getEquipment(session);

        StarForceDto enhanced = service.enhance(equipment, statistics);
        Equipment updatedEquipment = enhanced.getEquipment();
        StarStatistics updatedStatistics = enhanced.getStatistics();

        session.setAttribute("equipment", updatedEquipment);
        session.setAttribute("starStatistics", updatedStatistics);

        return new StarForceDto(updatedStatistics, updatedEquipment);
    }

    @PostMapping("/repair")
    @ResponseBody
    public StarForceDto repair(HttpSession session) {
        StarStatistics statistics = getStatistics(session);
        Equipment equipment = getEquipment(session);

        Equipment repaired = service.repair(equipment);
        StarStatistics newStatistics = service.updateRepairStatistics(repaired, statistics);

        session.setAttribute("equipment", repaired);
        session.setAttribute("starStatistics", statistics);

        return new StarForceDto(newStatistics, repaired);
    }

    @PostMapping("/set-price")
    @ResponseBody
    public StarForceDto setPrice(@RequestBody StarStatus status, HttpSession session) {
        StarStatistics statistics = getStatistics(session);
        Equipment equipment = getEquipment(session);
        Long price = status.getPrice();

        Equipment newEquipment = equipment.newPrice(price);
        session.setAttribute("equipment", newEquipment);

        return new StarForceDto(statistics, newEquipment);
    }

    @GetMapping("/reset")
    @ResponseBody
    public StarForceDto reset(HttpSession session) {
        StarStatistics statistics = service.reset();

        Equipment resetEquipment = new Equipment();

        session.setAttribute("starStatistics", statistics);
        session.setAttribute("equipment", resetEquipment);

        return new StarForceDto(statistics, resetEquipment);
    }

    private StarStatistics getStatistics(HttpSession session) {
        StarStatistics statistics = (StarStatistics) session.getAttribute("starStatistics");

        if (statistics == null) {
            statistics = new StarStatistics();
            session.setAttribute("starStatistics", statistics);
        }
        
        return statistics;
    }

    private Equipment getEquipment(HttpSession session) {
        Equipment equipment = (Equipment) session.getAttribute("equipment");

        if (equipment == null) {
            equipment = new Equipment();
            session.setAttribute("equipment", equipment);
        }

        return equipment;
    }
}
