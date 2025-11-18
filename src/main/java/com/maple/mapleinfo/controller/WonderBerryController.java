package com.maple.mapleinfo.controller;

import com.maple.mapleinfo.domain.wonder_berry.WonderResult;
import com.maple.mapleinfo.service.wonder_berry.WonderBerryService;
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
    public String showWonderBerry() {

        return "wonder";
    }

    @GetMapping("/use")
    @ResponseBody
    public WonderResult useWonderBerry() {
        return wonderBerryService.useBerry();
    }


    @GetMapping("/use10")
    @ResponseBody
    public WonderResult useTenWonderBerries() {
        return wonderBerryService.useTenBerries();
    }

    @GetMapping("/reset")
    @ResponseBody
    public WonderResult resetWonderBerry() {
        return wonderBerryService.reset();
    }
}
