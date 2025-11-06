package com.maple.mapleinfo.controller;

import com.maple.mapleinfo.service.CharacterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/maplestory/v1")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping("/id")
    public String getCharacter(@RequestParam String name) {
        return characterService.getCharacterBasicInfo(name);
    }
}
