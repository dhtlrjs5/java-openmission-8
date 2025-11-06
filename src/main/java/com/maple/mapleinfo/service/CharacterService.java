package com.maple.mapleinfo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.mapleinfo.client.MapleApiClient;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

    private final MapleApiClient mapleApiClient;
    private final ObjectMapper objectMapper;

    public CharacterService(MapleApiClient mapleApiClient, ObjectMapper objectMapper) {
        this.mapleApiClient = mapleApiClient;
        this.objectMapper = objectMapper;
    }

    // 테스트용 임시 추출 로직
    public String getCharacterBasicInfo(String nickname) {
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

