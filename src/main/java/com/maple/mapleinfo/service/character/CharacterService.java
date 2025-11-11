package com.maple.mapleinfo.service.character;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.mapleinfo.client.MapleApiClient;
import com.maple.mapleinfo.dto.CharacterBasicInfoDto;
import com.maple.mapleinfo.exception.CharacterNotFoundException;
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

        CharacterBasicInfoDto info = mapleApiClient.getCharacterBasicInfo(ocid);

        if (ocid == null || ocid.isEmpty() || info == null) {
            throw new CharacterNotFoundException("캐릭터의 정보를 불러올 수 없습니다.");
        }

        return info;
    }
}

