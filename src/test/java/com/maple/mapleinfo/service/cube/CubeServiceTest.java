package com.maple.mapleinfo.service.cube;

import com.fasterxml.jackson.databind.JsonNode;
import com.maple.mapleinfo.domain.cube.Option;
import com.maple.mapleinfo.domain.cube.Potential;
import com.maple.mapleinfo.dto.PotentialDto;
import com.maple.mapleinfo.repository.CubeRepository;
import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CubeServiceTest {

    @InjectMocks
    private CubeService cubeService;

    @Mock
    private Random random;

    @Mock
    private CubeRepository repository;

    @Mock
    private CubeRoller roller;

    private final List<Option> mockOptions = createMockOptions();

    @BeforeEach
    void setUp() throws Exception {
        Field randomField = CubeService.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(cubeService, random);

        // 💡 1. Roller Mocking: 모든 옵션 롤링 호출에 대해 Mock 리스트를 반환합니다.
        when(roller.rollOption(any(JsonNode.class))).thenReturn(mockOptions);

        // 💡 2. Repository Mocking: findOptionGradeNode 호출 Mocking (NPE 방지)
        when(repository.findOptionGradeNode(any(CubeType.class), any(Grade.class)))
                .thenReturn(mock(JsonNode.class));

        // 💡 3. Upgrade Node Mocking: RARE -> EPIC (기본 15.0% 가정)
        // 등급 유지/확률 승급 테스트를 위해 기본 확률을 Mocking합니다.
        JsonNode upgradeNodeMock = mock(JsonNode.class);
        when(upgradeNodeMock.asDouble()).thenReturn(15.0); // RARE -> EPIC 확률 15%
        when(repository.findUpgradeProbabilityNode(any(CubeType.class), any(Grade.class)))
                .thenReturn(upgradeNodeMock);
    }

    private List<Option> createMockOptions() {
        List<Option> options = new ArrayList<>();

        options.add(new Option("Mock Option 1", "1%"));
        options.add(new Option("Mock Option 2", "1%"));
        options.add(new Option("Mock Option 3", "1%"));

        return options;
    }

    @Test
    @DisplayName("성공: 윗잠은 RARE 등급에서 천장 횟수(10회)에 도달하면 EPIC으로 강제 승급 및 카운트 초기화")
    void useCube_Default_RareToEpic_LimitSystem() {
        // given
        // RARE_TO_EPIC_DEFAULT_LIMIT = 10 이므로, count가 9인 상태에서 사용하면 10이 되어 천장 발동
        int initialCount = 9;
        Grade initialGrade = Grade.RARE;
        PotentialDto potentialDto = new PotentialDto(initialGrade, initialCount, CubeType.DEFAULT);

        // when
        Potential potentialAfterLimit = cubeService.useCube(potentialDto);

        // then
        assertEquals(Grade.EPIC, potentialAfterLimit.getGrade(), "천장 횟수에 도달하여 EPIC으로 승급해야 한다");
        assertEquals(0, potentialAfterLimit.getCount(), "승급 후 카운트는 0으로 초기화되어야 한다");
    }

    @Test
    @DisplayName("성공: 윗잠은 EPIC 등급에서 천장 횟수에 도달하면 UNIQUE로 강제 승급 및 카운트 초기화")
    void useCube_Default_EpicToUnique_LimitSystem() {
        // given
        int initialCount = 41;
        Grade initialGrade = Grade.EPIC;
        PotentialDto potentialDto = new PotentialDto(initialGrade, initialCount, CubeType.DEFAULT);

        // when
        Potential potentialAfterLimit = cubeService.useCube(potentialDto);

        // then
        assertEquals(Grade.UNIQUE, potentialAfterLimit.getGrade(), "천장 횟수에 도달하여 UNIQUE로 승급해야 한다");
        assertEquals(0, potentialAfterLimit.getCount(), "승급 후 카운트는 0으로 초기화되어야 한다");
    }

    @Test
    @DisplayName("성공: 윗잠은 UNIQUE 등급에서 천장 횟수에 도달하면 LEGENDARY로 강제 승급 및 카운트 초기화")
    void useCube_Default_UniqueToLEGENDARY_LimitSystem() {
        // given
        int initialCount = 106;
        Grade initialGrade = Grade.UNIQUE;
        PotentialDto potentialDto = new PotentialDto(initialGrade, initialCount, CubeType.DEFAULT);

        // when
        Potential potentialAfterLimit = cubeService.useCube(potentialDto);

        // then
        assertEquals(Grade.LEGENDARY, potentialAfterLimit.getGrade(), "천장 횟수에 도달하여 LEGENDARY로 승급해야 한다");
        assertEquals(0, potentialAfterLimit.getCount(), "승급 후 카운트는 0으로 초기화되어야 한다");
    }

    @Test
    @DisplayName("성공: 등급 유지 시 count가 1 증가하고 옵션이 3개 롤링된다")
    void useCube_Default_GradeMaintained_CountIncreases_OptionsRolled() {
        // given RARE_TO_EPIC 확률은 15.0%
        int initialCount = 0;
        Grade initialGrade = Grade.RARE;
        PotentialDto potentialDto = new PotentialDto(initialGrade, initialCount, CubeType.DEFAULT);

        // random.nextDouble() * 100.0 이 15.0보다 크도록 Mock
        when(random.nextDouble()).thenReturn(0.50); // 50.0% (승급 실패)

        // when
        Potential result = cubeService.useCube(potentialDto);

        // then
        assertEquals(Grade.RARE, result.getGrade(), "확률 승급에 실패하여 RARE 등급이 유지되어야 한다");
        assertEquals(1, result.getCount(), "등급 유지 시 카운트가 1 증가해야 한다");
        assertEquals(3, result.getOptions().size(), "옵션이 3개 롤링되어야 한다");
    }

    @Test
    @DisplayName("성공: 아랫잠은 RARE 등급에서 천장 횟수(31회)에 도달하면 EPIC으로 강제 승급 및 카운트 초기화")
    void useCube_Additional_RareToEpic_LimitSystem() {
        // given
        int initialCount = 30;
        Grade initialGrade = Grade.RARE;
        PotentialDto potentialDto = new PotentialDto(initialGrade, initialCount, CubeType.ADDITIONAL);

        // when
        Potential potentialAfterLimit = cubeService.useCube(potentialDto);

        // then
        assertEquals(Grade.EPIC, potentialAfterLimit.getGrade(), "천장 횟수에 도달하여 EPIC으로 승급해야 한다");
        assertEquals(0, potentialAfterLimit.getCount(), "승급 후 카운트는 0으로 초기화되어야 한다");
    }

    @Test
    @DisplayName("성공: 아랫잠은 EPIC 등급에서 천장 횟수에 도달하면 UNIQUE로 강제 승급 및 카운트 초기화")
    void useCube_Additional_EpicToUnique_LimitSystem() {
        // given
        int initialCount = 75;
        Grade initialGrade = Grade.EPIC;
        PotentialDto potentialDto = new PotentialDto(initialGrade, initialCount, CubeType.ADDITIONAL);

        // when
        Potential potentialAfterLimit = cubeService.useCube(potentialDto);

        // then
        assertEquals(Grade.UNIQUE, potentialAfterLimit.getGrade(), "천장 횟수에 도달하여 UNIQUE로 승급해야 한다");
        assertEquals(0, potentialAfterLimit.getCount(), "승급 후 카운트는 0으로 초기화되어야 한다");
    }

    @Test
    @DisplayName("성공: 아랫잠은 UNIQUE 등급에서 천장 횟수에 도달하면 LEGENDARY로 강제 승급 및 카운트 초기화")
    void useCube_Additional_UniqueToLegendary_LimitSystem() {
        // given
        int initialCount = 213;
        Grade initialGrade = Grade.UNIQUE;
        PotentialDto potentialDto = new PotentialDto(initialGrade, initialCount, CubeType.ADDITIONAL);

        // when
        Potential potentialAfterLimit = cubeService.useCube(potentialDto);

        // then
        assertEquals(Grade.LEGENDARY, potentialAfterLimit.getGrade(), "천장 횟수에 도달하여 LEGENDARY로 승급해야 한다");
        assertEquals(0, potentialAfterLimit.getCount(), "승급 후 카운트는 0으로 초기화되어야 한다");
    }
}