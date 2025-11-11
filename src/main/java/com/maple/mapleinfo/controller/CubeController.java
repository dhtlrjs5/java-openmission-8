package com.maple.mapleinfo.controller;

import com.maple.mapleinfo.domain.cube.CubeStatistics;
import com.maple.mapleinfo.domain.cube.Potential;
import com.maple.mapleinfo.dto.CubeDto;
import com.maple.mapleinfo.service.CubeService;
import com.maple.mapleinfo.service.CubeStatisticsService;
import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class CubeController {

    private static final String DEFAULT_COUNT = "defaultCount";
    private static final String ADDITIONAL_COUNT = "additionalCount";
    private static final String GRADE = "grade";
    private static final String ADDITIONAL_GRADE = "additionalGrade";

    private final CubeService cubeService;
    private final CubeStatisticsService cubeStatisticsService;

    @GetMapping("/cube")
    public String showCubePage(Model model, HttpSession session) {
        // 기본 큐브 등급 초기화
        if (session.getAttribute(GRADE) == null) {
            session.setAttribute(GRADE, Grade.RARE);
        }
        // 추가 큐브 등급 초기화
        if (session.getAttribute(ADDITIONAL_GRADE) == null) {
            session.setAttribute(ADDITIONAL_GRADE, Grade.RARE);
        }

        model.addAttribute(GRADE, session.getAttribute(GRADE));
        model.addAttribute(ADDITIONAL_GRADE, session.getAttribute(ADDITIONAL_GRADE));

        return "cube";
    }

    @GetMapping("/cube/use")
    @ResponseBody
    public CubeDto useDefaultCube(HttpSession session) {
        Grade currentGrade = (Grade) session.getAttribute(GRADE);
        Integer count = (Integer) session.getAttribute(DEFAULT_COUNT);
        if (currentGrade == null) currentGrade = Grade.RARE;
        if (count == null) count = 0;

        Potential potential = cubeService.useCube(currentGrade, CubeType.DEFAULT, count);
        CubeStatistics statistics = cubeStatisticsService.updateStatistics(CubeType.DEFAULT, currentGrade);

        session.setAttribute(GRADE, potential.getGrade());
        session.setAttribute(DEFAULT_COUNT, potential.getCount());

        return new CubeDto(potential, statistics);
    }

    @GetMapping("/cube/use/additional")
    @ResponseBody
    public CubeDto useAdditionalCube(HttpSession session) {
        Grade currentGrade = (Grade) session.getAttribute(ADDITIONAL_GRADE);
        Integer count = (Integer) session.getAttribute(ADDITIONAL_COUNT);
        if (currentGrade == null) currentGrade = Grade.RARE;
        if (count == null) count = 0;

        Potential potential = cubeService.useCube(currentGrade, CubeType.ADDITIONAL, count);
        CubeStatistics statistics = cubeStatisticsService.updateStatistics(CubeType.ADDITIONAL, currentGrade);

        session.setAttribute(ADDITIONAL_GRADE, potential.getGrade());
        session.setAttribute(ADDITIONAL_COUNT, potential.getCount());

        return new CubeDto(potential, statistics);
    }

    @GetMapping("/cube/reset")
    @ResponseBody
    public CubeDto resetCube(HttpSession session) {
        session.setAttribute(GRADE, Grade.RARE);
        session.setAttribute(ADDITIONAL_GRADE, Grade.RARE);
        session.setAttribute(DEFAULT_COUNT, 0);
        session.setAttribute(ADDITIONAL_COUNT, 0);

        return cubeService.reset();
    }
}