package com.maple.mapleinfo.client;

import com.maple.mapleinfo.dto.CharacterBasicInfoDto;
import com.maple.mapleinfo.exception.CharacterNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.maple.mapleinfo.utils.ErrorMessages.*;

@Slf4j
@Component
public class MapleApiClient {

    @Value("${nexon.api.key}")
    private String apiKey;

    @Value("${nexon.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public MapleApiClient() {
        this.restTemplate = new RestTemplate();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-nxopen-api-key", apiKey);
        return headers;
    }

    public String getCharacterIdByName(String name) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/id")
                .queryParam("character_name", name)
                .build()
                .toUriString();

        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new CharacterNotFoundException(ERROR_INVALID_CHARACTER_NAME);
        }
    }

    public CharacterBasicInfoDto getCharacterBasicInfo(String ocid) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/character/basic")
                .queryParam("ocid", ocid)
                .build()
                .toUriString();

        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

        try {
            ResponseEntity<CharacterBasicInfoDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    CharacterBasicInfoDto.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new CharacterNotFoundException(ERROR_CHARACTER_INFO_NOT_FOUND);
        }
    }
}