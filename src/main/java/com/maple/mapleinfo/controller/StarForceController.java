package com.maple.mapleinfo.controller;

import com.maple.mapleinfo.domain.star_force.Equipment;
import com.maple.mapleinfo.domain.star_force.StarStatistics;
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
    public StarForceDto enhance(@RequestBody Equipment equipment, HttpSession session) {
        StarStatistics statistics = getStatistics(session);
        Equipment updatedEquipment = service.enhance(equipment, statistics);

        return new StarForceDto(statistics, updatedEquipment);
    }

    @PostMapping("/repair")
    @ResponseBody
    public StarForceDto repair(@RequestBody Equipment equipment, HttpSession session) {
        StarStatistics statistics = getStatistics(session);
        Equipment repaired = service.repair(equipment, statistics);

        return new StarForceDto(statistics, repaired);
    }

    @PostMapping("/set-price")
    @ResponseBody
    public StarForceDto setPrice(@RequestBody Equipment equipment, HttpSession session) {
        StarStatistics statistics = getStatistics(session);
        Long price = equipment.getPrice();

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
}
