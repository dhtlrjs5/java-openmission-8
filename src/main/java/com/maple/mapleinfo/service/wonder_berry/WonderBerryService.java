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

import static com.maple.mapleinfo.utils.ErrorMessages.*;

@Service
public class WonderBerryService {

    private static final String FILE_NAME = "wonder_berry.json";
    private static final String RESOURCE_PATH = "/data/" + FILE_NAME;

    private final WonderBerry wonderBerry;

    public WonderBerryService() {

        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = loadJsonRoot(mapper);

        List<Item> items = createItems(root);

        this.wonderBerry = new WonderBerry(items);
    }

    private List<Item> createItems(JsonNode root) {
        List<Item> items = new ArrayList<>();

        for (JsonNode node : root.path("RARE")) {
            Item item = createItem(node, Rarity.RARE);
            items.add(item);
        }

        for (JsonNode node : root.path("NORMAL")) {
            Item item = createItem(node, Rarity.NORMAL);
            items.add(item);
        }
        return items;
    }

    private Item createItem(JsonNode node, Rarity rarity) {
        JsonNode nameNode = node.get("name");
        JsonNode probabilityNode = node.get("probability");

        return new Item(
                nameNode.asText(),
                probabilityNode.asDouble(),
                rarity
        );
    }

    private JsonNode loadJsonRoot(ObjectMapper mapper) {
        JsonNode root;

        try (InputStream inputStream = getClass().getResourceAsStream(RESOURCE_PATH)) {
            if (inputStream == null) {
                throw new FileNotFoundException(FILE_NAME + SUFFIX_NOT_FOUND);
            }
            root = mapper.readTree(inputStream);
        } catch (Exception e) {
            throw new IllegalStateException(FILE_NAME + SUFFIX_FAIL_LOADING);
        }

        return root;
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
