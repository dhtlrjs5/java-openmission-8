package com.maple.mapleinfo.client;

import com.maple.mapleinfo.dto.CharacterBasicInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }

    public CharacterBasicInfoDto getCharacterBasicInfo(String ocid) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/character/basic")
                .queryParam("ocid", ocid)
                .build()
                .toUriString();

        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<CharacterBasicInfoDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                CharacterBasicInfoDto.class
        );

        return response.getBody();
    }
}