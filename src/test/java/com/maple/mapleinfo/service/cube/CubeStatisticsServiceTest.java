package com.maple.mapleinfo.service.cube;

import com.maple.mapleinfo.domain.cube.CubeStatistics;
import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
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

    @ParameterizedTest(name = "성공: 기본 큐브 사용 시 {0} 등급 통계 및 비용이 올바르게 업데이트된다")
    @CsvSource({
            "RARE, 4500000",
            "EPIC, 18000000",
            "UNIQUE, 38250000",
            "LEGENDARY, 45000000"
    })
    void updateStatistics_Default(Grade grade, long expectedCost) {
        // when
        CubeStatistics statistics =
                cubeStatisticsService.updateStatistics(CubeType.DEFAULT, grade);

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
        // when
        CubeStatistics statistics =
                cubeStatisticsService.updateStatistics(CubeType.ADDITIONAL, grade);

        // then
        assertEquals(1L, statistics.getAdditionalCount(), "큐브 횟수가 1 증가해야 한다");
        assertEquals(expectedCost, statistics.getAdditionalCost(), "에디셔널 큐브 비용이 추가되어야 한다");
        assertEquals(expectedCost, statistics.getTotalCost(), "총 비용이 추가되어야 한다");
    }
}