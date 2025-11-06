package com.maple.mapleinfo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 *   "date": "2023-12-21T00:00+09:00",
 *   "character_name": "string",
 *   "world_name": "string",
 *   "character_gender": "string",
 *   "character_class": "string",
 *   "character_class_level": "string",
 *   "character_level": 0,
 *   "character_exp": 0,
 *   "character_exp_rate": "string",
 *   "character_guild_name": "string",
 *   "character_image": "string",
 *   "character_date_create": "2023-12-21T00:00+09:00",
 *   "access_flag": "string",
 *   "liberation_quest_clear": "string"
 */
@Getter
public class CharacterBasicInfoDto {

    @JsonProperty("character_name")
    private String name;

    @JsonProperty("world_name")
    private String world;

    @JsonProperty("character_guild_name")
    private String guild;

    @JsonProperty("character_class")
    private String job;

    @JsonProperty("character_level")
    private long level;

    @JsonProperty("character_exp_rate")
    private String expRate;

    @JsonProperty("character_image")
    private String image;

    @Override
    public String toString() {
        return String.format("%s (%s@%s) [%s] Lv.%d (%s%%)", name, world, guild, job, level, expRate);
    }
}
