package com.maple.mapleinfo.controller;

import com.maple.mapleinfo.domain.wonder_berry.WonderResult;
import com.maple.mapleinfo.domain.wonder_berry.WonderStatistics;
import com.maple.mapleinfo.service.wonder_berry.WonderBerryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/wonder")
@RequiredArgsConstructor
public class WonderBerryController {

    private final WonderBerryService wonderBerryService;

    @GetMapping()
    public String showWonderBerry(HttpSession session) {
        getWonderStatistics(session);

        return "wonder";
    }

    @GetMapping("/use")
    @ResponseBody
    public WonderResult useWonderBerry(HttpSession session) {
        WonderStatistics statistics = getWonderStatistics(session);
        WonderResult result = wonderBerryService.useBerry(statistics);

        session.setAttribute("wonderStatistics", statistics);

        return result;
    }


    @GetMapping("/use10")
    @ResponseBody
    public WonderResult useTenWonderBerries(HttpSession session) {
        WonderStatistics statistics = getWonderStatistics(session);
        WonderResult result = wonderBerryService.useTenBerries(statistics);

        session.setAttribute("wonderStatistics", statistics);

        return result;
    }

    @GetMapping("/reset")
    @ResponseBody
    public WonderResult resetWonderBerry(HttpSession session) {
        WonderStatistics statistics = getWonderStatistics(session);
        WonderResult result = wonderBerryService.reset(statistics);

        session.setAttribute("wonderStatistics", statistics);

        return result;
    }

    private WonderStatistics getWonderStatistics(HttpSession session) {
        WonderStatistics statistics = (WonderStatistics) session.getAttribute("wonderStatistics");
        if (statistics == null) {
            statistics = new WonderStatistics();
            session.setAttribute("wonderStatistics", statistics);
        }
        return statistics;
    }
}
