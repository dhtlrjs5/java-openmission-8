package com.maple.mapleinfo.service.wonder_berry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.mapleinfo.domain.wonder_berry.Item;
import com.maple.mapleinfo.domain.wonder_berry.WonderBerry;
import com.maple.mapleinfo.domain.wonder_berry.WonderResult;
import com.maple.mapleinfo.utils.Rarity;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class WonderBerryService {

    private static final String FILE_NAME = "wonder-berry.json";
    private static final String RESOURCE_PATH = "/data/" + FILE_NAME;

    private final WonderBerry wonderBerry;

    public WonderBerryService() {

        ObjectMapper mapper = new ObjectMapper();

        JsonNode root;
        try (InputStream inputStream = getClass().getResourceAsStream(RESOURCE_PATH)) {
            if (inputStream == null) {
                throw new FileNotFoundException(RESOURCE_PATH + " not found");
            }
            root = mapper.readTree(inputStream);
        } catch (Exception e) {
            throw new IllegalStateException(FILE_NAME + " 로드 실패");
        }

        List<Item> items = new ArrayList<>();

        for (JsonNode node : root.path("RARE")) {
            items.add(new Item(
                    node.get("name").asText(),
                    node.get("probability").asDouble(),
                    Rarity.RARE
            ));
        }

        for (JsonNode node : root.path("NORMAL")) {
            items.add(new Item(
                    node.get("name").asText(),
                    node.get("probability").asDouble(),
                    Rarity.NORMAL
            ));
        }

        this.wonderBerry = new WonderBerry(items);
    }

    public WonderResult useBerry() {
        return wonderBerry.useWonderBerry();
    }

    public WonderResult useTenBerries() {
        return wonderBerry.useTenTimes();
    }

    public WonderResult reset() {
        return wonderBerry.reset();
    }
}
