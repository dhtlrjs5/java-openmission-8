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
@RequiredArgsConstructor
public class StarForceController {

    private final StarForceService service;

    @GetMapping("/star")
    public String showStar() {
        return "star";
    }

    @PostMapping("/star/enhance")
    @ResponseBody
    public StarForceDto enhance(@RequestBody Equipment equipment, HttpSession session) {
        StarStatistics statistics = getStatistics(session);
        Equipment updatedEquipment = service.enhance(equipment, statistics);

        return new StarForceDto(statistics, updatedEquipment);
    }

    @PostMapping("/star/repair")
    @ResponseBody
    public StarForceDto repair(@RequestBody Equipment equipment, HttpSession session) {
        StarStatistics statistics = getStatistics(session);
        Equipment repaired = service.repair(equipment, statistics);

        return new StarForceDto(statistics, repaired);
    }

    @PostMapping("/star/set-price")
    @ResponseBody
    public StarForceDto setPrice(@RequestBody Equipment equipment, HttpSession session) {
        StarStatistics statistics = getStatistics(session);
        //임시용 레벨 250 고정
        Equipment newEquipment = new Equipment(250, equipment.getStar(), equipment.getPrice(), equipment.isDestroyed());

        session.setAttribute("equipment", newEquipment);

        return new StarForceDto(statistics, newEquipment);
    }

    @GetMapping("/reset")
    public StarForceDto reset() {
        StarStatistics statistics = service.reset(); // 통계 초기화
        Equipment resetEquipment = new Equipment(250, 0, 0L, false); // 장비 초기화

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
