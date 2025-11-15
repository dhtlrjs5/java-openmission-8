package com.maple.mapleinfo.controller;

import com.maple.mapleinfo.domain.cube.CubeStatistics;
import com.maple.mapleinfo.domain.cube.Potential;
import com.maple.mapleinfo.dto.CubeDto;
import com.maple.mapleinfo.dto.PotentialDto;
import com.maple.mapleinfo.service.cube.CubeService;
import com.maple.mapleinfo.service.cube.CubeSessionManager;
import com.maple.mapleinfo.service.cube.CubeStatisticsService;
import com.maple.mapleinfo.utils.CubeType;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.maple.mapleinfo.utils.SessionKeys.*;

@Controller
@RequestMapping("/cube")
@RequiredArgsConstructor
public class CubeController {

    private final CubeService cubeService;
    private final CubeStatisticsService cubeStatisticsService;
    private final CubeSessionManager cubeSessionManager;

    @GetMapping
    public String showCubePage(Model model, HttpSession session) {
        // 초기화
        if (session.getAttribute(DEFAULT_GRADE) == null) {
            cubeSessionManager.initializeSession(session);
        }

        model.addAttribute(DEFAULT_GRADE, session.getAttribute(DEFAULT_COUNT));
        model.addAttribute(ADDITIONAL_GRADE, session.getAttribute(ADDITIONAL_GRADE));

        return "cube";
    }

    @GetMapping("/use")
    @ResponseBody
    public CubeDto useDefaultCube(HttpSession session) {
        PotentialDto state = cubeSessionManager.getPotentialFromSession(session, CubeType.DEFAULT);
        Potential potential = cubeService.useCube(state);
        CubeStatistics statistics = cubeStatisticsService.updateStatistics(CubeType.DEFAULT, state. getGrade());

        cubeSessionManager.updateSession(session, potential);

        return new CubeDto(potential, statistics);
    }

    @GetMapping("/use/additional")
    @ResponseBody
    public CubeDto useAdditionalCube(HttpSession session) {
        PotentialDto state = cubeSessionManager.getPotentialFromSession(session, CubeType.ADDITIONAL);
        Potential potential = cubeService.useCube(state);
        CubeStatistics statistics = cubeStatisticsService.updateStatistics(CubeType.ADDITIONAL, state. getGrade());

        cubeSessionManager.updateSession(session, potential);

        return new CubeDto(potential, statistics);
    }

    @GetMapping("/reset")
    @ResponseBody
    public CubeDto resetCube(HttpSession session) {
        cubeSessionManager.initializeSession(session);

        return cubeService.reset();
    }
}