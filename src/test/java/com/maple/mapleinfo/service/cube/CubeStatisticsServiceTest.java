package com.maple.mapleinfo.service.cube;

import com.maple.mapleinfo.domain.cube.CubeStatistics;
import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CubeStatisticsServiceTest {

    @InjectMocks
    private CubeStatisticsService cubeStatisticsService;

    private CubeStatistics initStats() {
        return new CubeStatistics();
    }

    @ParameterizedTest(name = "성공: 기본 큐브 사용 시 {0} 등급 통계 및 비용이 올바르게 업데이트된다")
    @CsvSource({
            "RARE, 4500000",
            "EPIC, 18000000",
            "UNIQUE, 38250000",
            "LEGENDARY, 45000000"
    })
    void updateStatistics_Default(Grade grade, long expectedCost) {
        //given
        CubeStatistics initStatistics = initStats();

        // when
        CubeStatistics statistics =
                cubeStatisticsService.updateStatistics(initStatistics, CubeType.DEFAULT, grade);

        // then
        assertEquals(1L, statistics.getDefaultCount(), "큐브 횟수가 1 증가해야 한다");
        assertEquals(expectedCost, statistics.getDefaultCost(), "기본 큐브 비용이 추가되어야 한다");
        assertEquals(expectedCost, statistics.getTotalCost(), "총 비용이 추가되어야 한다");
    }

    @ParameterizedTest(name = "성공: 에디셔널 큐브 사용 시 {0} 등급 통계 및 비용이 올바르게 업데이트된다")
    @CsvSource({
            "RARE, 22000000",
            "EPIC, 61600000",
            "UNIQUE, 74800000",
            "LEGENDARY, 88000000"
    })
    void updateStatistics_Additional(Grade grade, long expectedCost) {
        //given
        CubeStatistics initStatistics = initStats();

        // when
        CubeStatistics statistics =
                cubeStatisticsService.updateStatistics(initStatistics, CubeType.ADDITIONAL, grade);

        // then
        assertEquals(1L, statistics.getAdditionalCount(), "큐브 횟수가 1 증가해야 한다");
        assertEquals(expectedCost, statistics.getAdditionalCost(), "에디셔널 큐브 비용이 추가되어야 한다");
        assertEquals(expectedCost, statistics.getTotalCost(), "총 비용이 추가되어야 한다");
    }

    @Test
    @DisplayName("성공: 기본 및 에디셔널 큐브 사용 시 통계가 분리되어 누적되고 총 비용이 합산된다")
    void updateStatistics_Mixed_Cumulative() {
        // given
        CubeStatistics initialStats = initStats();
        Grade defaultGrade = Grade.EPIC;
        long defaultCost = 18_000_000L; // CubeCost.DEFAULT_EPIC
        Grade additionalGrade = Grade.RARE;
        long additionalCost = 22_000_000L; // CubeCost.ADDITIONAL_RARE

        // when 1: 기본 큐브 사용
        CubeStatistics firstStats =
                cubeStatisticsService.updateStatistics(initialStats, CubeType.DEFAULT, defaultGrade);

        // when 2: 에디셔널 큐브 사용 (1회 결과를 인수로 전달)
        CubeStatistics finalStats =
                cubeStatisticsService.updateStatistics(firstStats, CubeType.ADDITIONAL, additionalGrade);


        // then
        // 기본 큐브 통계 확인
        assertEquals(1L, finalStats.getDefaultCount(), "기본 큐브 횟수는 1회여야 한다");
        assertEquals(defaultCost, finalStats.getDefaultCost(), "기본 큐브 비용은 1회분이어야 한다");

        // 에디셔널 큐브 통계 확인
        assertEquals(1L, finalStats.getAdditionalCount(), "에디셔널 큐브 횟수는 1회여야 한다");
        assertEquals(additionalCost, finalStats.getAdditionalCost(), "에디셔널 큐브 비용은 1회분이어야 한다");

        // 총 비용 확인
        assertEquals(defaultCost + additionalCost, finalStats.getTotalCost(), "총 비용은 두 큐브의 합산이어야 한다");
    }
}