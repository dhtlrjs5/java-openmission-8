package com.maple.mapleinfo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.mapleinfo.client.MapleApiClient;
import com.maple.mapleinfo.dto.CharacterBasicInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CharacterService {

    private final MapleApiClient mapleApiClient;
    private final ObjectMapper objectMapper;

    public CharacterService(MapleApiClient mapleApiClient, ObjectMapper objectMapper) {
        this.mapleApiClient = mapleApiClient;
        this.objectMapper = objectMapper;
    }

    public CharacterBasicInfoDto getCharacterBasicInfo(String nickname) {
        String ocidJson = mapleApiClient.getCharacterIdByName(nickname);
        String ocid;

        try {
            JsonNode rootNode = objectMapper.readTree(ocidJson);

            ocid = rootNode.path("ocid").asText();
        } catch (JsonProcessingException e) {
            ocid = null;
        }

        return mapleApiClient.getCharacterBasicInfo(ocid);
    }
}

