package com.maple.mapleinfo.controller;

import com.maple.mapleinfo.domain.cube.Potential;
import com.maple.mapleinfo.service.CubeService;
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

    private final CubeService cubeService;

    @GetMapping("/cube")
    public String showCubePage(Model model, HttpSession session) {
        // 기본 큐브 등급 초기화
        if (session.getAttribute("grade") == null) {
            session.setAttribute("grade", Grade.RARE);
        }
        // 추가 큐브 등급 초기화
        if (session.getAttribute("additionalGrade") == null) {
            session.setAttribute("additionalGrade", Grade.RARE);
        }

        model.addAttribute("grade", session.getAttribute("grade"));
        model.addAttribute("additionalGrade", session.getAttribute("additionalGrade"));

        return "cube";
    }

    @GetMapping("/cube/use")
    @ResponseBody
    public Potential useDefaultCubeJson(HttpSession session) {
        Grade currentGrade = (Grade) session.getAttribute("grade");
        if (currentGrade == null) currentGrade = Grade.RARE;

        Potential potential = cubeService.useCube(currentGrade, CubeType.NORMAL);
        session.setAttribute("grade", potential.getGrade());

        return potential;
    }

    @GetMapping("/cube/use/additional")
    @ResponseBody
    public Potential useAdditionalCubeJson(HttpSession session) {
        Grade currentGrade = (Grade) session.getAttribute("additionalGrade");
        if (currentGrade == null) currentGrade = Grade.RARE;

        Potential potential = cubeService.useCube(currentGrade, CubeType.ADDITIONAL);
        session.setAttribute("additionalGrade", potential.getGrade());

        return potential;
    }


}