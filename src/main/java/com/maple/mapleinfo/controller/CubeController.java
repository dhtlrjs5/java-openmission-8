package com.maple.mapleinfo.controller;

import com.maple.mapleinfo.domain.cube.Option;
import com.maple.mapleinfo.domain.cube.Potential;
import com.maple.mapleinfo.service.CubeService;
import com.maple.mapleinfo.utils.Grade;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CubeController {

    private final CubeService cubeService;

    @GetMapping("/cube")
    public String showCubePage(Model model, HttpSession session) {
        // 처음 접속 시 RARE로 초기화
        if (session.getAttribute("grade") == null) {
            session.setAttribute("grade", Grade.RARE);
        }

        model.addAttribute("grade", session.getAttribute("grade"));
        return "cube";
    }

    @GetMapping("/cube/use")
    public String useCube(Model model, HttpSession session) {
        // 현재 등급 불러오기
        Grade currentGrade = (Grade) session.getAttribute("grade");
        if (currentGrade == null) currentGrade = Grade.RARE;

        // 큐브 사용
        Potential potential = cubeService.useCube(currentGrade);
        List<Option> options = potential.getOptions();

        Grade grade = potential.getGrade();

        // 세션에 새 등급 저장
        session.setAttribute("grade", grade);

        // 뷰에 데이터 전달
        model.addAttribute("options", options);
        model.addAttribute("grade", grade);

        return "cube";
    }
}