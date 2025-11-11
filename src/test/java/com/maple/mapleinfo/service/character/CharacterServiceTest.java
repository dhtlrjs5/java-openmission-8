package com.maple.mapleinfo.service.character;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.mapleinfo.client.MapleApiClient;
import com.maple.mapleinfo.dto.CharacterBasicInfoDto;
import com.maple.mapleinfo.exception.CharacterNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @InjectMocks
    private CharacterService characterService;

    @Mock
    private MapleApiClient mapleApiClient;

    @Mock
    private ObjectMapper objectMapper;

    private static final String NICKNAME = "테스트캐릭터";
    private static final String OCID = "ocid_test_12345";
    private static final String OCID_JSON = "{\"ocid\": \"" + OCID + "\"}";

    private CharacterBasicInfoDto mockDto;
    private JsonNode mockJsonNode;

    @BeforeEach
    void setUp() {
        mockDto = Mockito.mock(CharacterBasicInfoDto.class);
        mockJsonNode = Mockito.mock(JsonNode.class);
    }

    @Test
    @DisplayName("성공: 닉네임으로 캐릭터 기본 정보를 조회한다")
    void getCharacterBasicInfo_Success() throws JsonProcessingException {
        // given
        when(mockDto.getName()).thenReturn(NICKNAME);
        when(mockJsonNode.path("ocid")).thenReturn(mockJsonNode);
        when(mockJsonNode.path("ocid").asText()).thenReturn(OCID);

        when(mapleApiClient.getCharacterIdByName(NICKNAME)).thenReturn(OCID_JSON);
        when(objectMapper.readTree(OCID_JSON)).thenReturn(mockJsonNode);
        when(mapleApiClient.getCharacterBasicInfo(OCID)).thenReturn(mockDto);

        // when
        CharacterBasicInfoDto result = characterService.getCharacterBasicInfo(NICKNAME);

        // then
        assertNotNull(result);
        assertEquals(NICKNAME, result.getName());
    }

    @Test
    @DisplayName("실패: 닉네임 조회 API 호출 실패 시 CharacterNotFoundException 발생")
    void getCharacterBasicInfo_Fail_NicknameNotFound() {
        // given
        doThrow(new CharacterNotFoundException("잘못된 캐릭터명 입니다.")).when(mapleApiClient).getCharacterIdByName(NICKNAME);

        // when & then
        assertThrows(CharacterNotFoundException.class, () -> characterService.getCharacterBasicInfo(NICKNAME));
    }

    @Test
    @DisplayName("실패: OCID JSON 파싱 실패 시 CharacterNotFoundException 발생")
    void getCharacterBasicInfo_Fail_JsonProcessing() throws JsonProcessingException {
        // given
        when(mapleApiClient.getCharacterIdByName(NICKNAME)).thenReturn(OCID_JSON);
        when(objectMapper.readTree(OCID_JSON)).thenThrow(new JsonProcessingException("파싱 오류") {});

        // when & then
        assertThrows(CharacterNotFoundException.class, () -> characterService.getCharacterBasicInfo(NICKNAME));
    }
}