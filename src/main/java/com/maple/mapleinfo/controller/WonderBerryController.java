package com.maple.mapleinfo.controller;

import com.maple.mapleinfo.domain.wonder_berry.WonderBerry;
import com.maple.mapleinfo.domain.wonder_berry.WonderResult;
import com.maple.mapleinfo.service.wonder_berry.WonderBerryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class WonderBerryController {

    private final WonderBerryService wonderBerryService;

    @GetMapping("/wonder")
    public String showWonderBerry() {

        return "wonder";
    }

    @GetMapping("/wonder/use")
    @ResponseBody
    public WonderResult useWonderBerry() {
        return wonderBerryService.useBerry();
    }


    @GetMapping("/wonder/use10")
    @ResponseBody
    public WonderResult useTenWonderBerries() {
        return wonderBerryService.useTenBerries();
    }

    @GetMapping("/wonder/reset")
    @ResponseBody
    public WonderResult resetWonderBerry() {
        return wonderBerryService.reset();
    }
}
