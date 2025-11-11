package com.maple.mapleinfo.controller;

import com.maple.mapleinfo.dto.CharacterBasicInfoDto;
import com.maple.mapleinfo.service.character.CharacterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/maplestory/v1")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping("/id")
    public String getCharacter(@RequestParam String name, Model model) {
        CharacterBasicInfoDto info = characterService.getCharacterBasicInfo(name);
        model.addAttribute("info", info);
        return "characterBasicInfo";
    }
}