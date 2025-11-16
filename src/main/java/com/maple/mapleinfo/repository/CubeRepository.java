package com.maple.mapleinfo.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.maple.mapleinfo.utils.ErrorMessages.*;

@Repository
public class CubeRepository {

    private static final String DEFAULT_FILE_NAME = "cube_weapon.json";
    private static final String ADDITIONAL_FILE_NAME = "additional_cube_weapon.json";
    private static final String DEFAULT_PATH = "/data/" + DEFAULT_FILE_NAME;
    private static final String ADDITIONAL_PATH = "/data/" + ADDITIONAL_FILE_NAME;

    private final JsonNode defaultRoot;
    private final JsonNode additionalRoot;

    public CubeRepository() {
        ObjectMapper mapper = new ObjectMapper();

        defaultRoot = loadJsonFile(mapper, DEFAULT_PATH, DEFAULT_FILE_NAME);
        additionalRoot = loadJsonFile(mapper, ADDITIONAL_PATH, ADDITIONAL_FILE_NAME);
    }

    private JsonNode loadJsonFile(ObjectMapper mapper, String path, String fileName) {
        try (InputStream inputStream = getClass().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new FileNotFoundException(fileName + SUFFIX_NOT_FOUND);
            }

            return mapper.readTree(inputStream);
        } catch (Exception e) {
            throw new IllegalStateException(DEFAULT_FILE_NAME + SUFFIX_FAIL_LOADING);
        }
    }

    public JsonNode findUpgradeProbabilityNode(CubeType type, Grade grade) {
        String gradeName = grade.name();

        JsonNode cubeNode = getRootNode(type);
        JsonNode upgradeNode = cubeNode.path("upgrade");

        if (upgradeNode.isMissingNode() || upgradeNode.isNull()) {
            throw new IllegalStateException(ERROR_NOT_FOUND_DATA);
        }

        return upgradeNode.path(gradeName);
    }

    public JsonNode findOptionGradeNode(CubeType type, Grade grade) {
        String gradeName = grade.name();

        JsonNode cube = getRootNode(type);
        JsonNode weaponNode = cube.path("weapon");
        JsonNode gradeNode = weaponNode.path(gradeName);

        if (gradeNode.isMissingNode() || gradeNode.isNull()) {
            throw new IllegalStateException(ERROR_NOT_FOUND_DATA);
        }

        return gradeNode;
    }

    private JsonNode getRootNode(CubeType type) {
        JsonNode cube = defaultRoot.path("cube");

        if (type.equals(CubeType.ADDITIONAL)) {
            cube = additionalRoot.path("cube");
        }

        return cube;
    }
}