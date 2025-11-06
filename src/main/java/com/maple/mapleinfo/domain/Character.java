package com.maple.mapleinfo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class Character {

    private final String ocid;
    private final String name;
    private final String world;
    private final String guild;
    private final String job;
    private final long level;
    private final String expRate;
    private final String image;
}
