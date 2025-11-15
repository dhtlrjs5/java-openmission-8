package com.maple.mapleinfo.service.star_force;

import com.maple.mapleinfo.domain.star_force.*;
import com.maple.mapleinfo.repository.StarForceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Field;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class StarForceServiceTest {

    private final EquipmentLevel level = new EquipmentLevel(250);

    private StarForceRepository repository;
    private StarForceService service;
    private Random mockRandom;

    @BeforeEach
    void setUp() throws Exception {
        repository = mock(StarForceRepository.class);
        mockRandom = mock(Random.class);
        service = new StarForceService(repository);

        Field randomField = StarForceService.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(service, mockRandom);
    }

    // 통계 객체 초기화 헬퍼 메서드
    private StarStatistics initStats() {
        return new StarStatistics();
    }

    @ParameterizedTest(name = "성공: {0}성 강화 성공 시 별이 1개 증가한다")
    @CsvSource(value = {
            "0, 435000, 95.0",      // 0성: 성공 95.0%, 비용 435000
            "10, 17740600, 50.0",   // 10성: 성공 50.0%, 비용 17740600
            "15, 139289100, 30.0",  // 15성: 성공 30.0%, 비용 139289100
            "17, 255250300, 15.0",  // 17성: 성공 15.0%, 비용 255250300
            "21, 526565100, 15.0"   // 21성: 성공 15.0%, 비용 526565100
    })
    @DisplayName("성공: 강화 성공 시 별이 1개 증가하고 비용과 시도 횟수가 누적된다")
    void enhance_successIncreasesStar(int star, long cost, double successRate) {
        // given
        StarStatus status = new StarStatus(star, 1_000_000L, false);
        Equipment equipment = new Equipment(level, status);
        StarStatistics stats = initStats();

        // 성공 판정을 위해 randomValue < successRate 가 되도록 설정 (1.0%로 고정)
        when(mockRandom.nextDouble()).thenReturn(0.01);
        when(repository.findProbability(star))
                .thenReturn(new StarForceProbability(successRate, 0.0, 0.0));
        when(repository.findCost(star, 250)).thenReturn(cost);

        // when
        Equipment result = service.enhance(equipment, stats);

        // then
        assertThat(result.getStar()).isEqualTo(star + 1);
        assertThat(result.isDestroyed()).isFalse();
        assertThat(stats.getTotalCost()).isEqualTo(cost);
        assertThat(stats.getAttempts()).isEqualTo(1L);
    }

    @ParameterizedTest(name = "실패(유지): {0}성 강화 실패 시 별이 유지된다")
    @CsvSource(value = {
            "0, 435000, 95.0, 0.0",    // 0성: 성공 95.0%
            "10, 17740600, 50.0, 0.0", // 10성: 성공 50.0%
            "15, 139289100, 30.0, 2.1", // 15성: 성공 30.0%
            "20, 290257600, 30.0, 10.5", // 20성: 성공 30.0%
            "16, 164060800, 30.0, 2.1",  // 16성: 성공 30.0%, 별하락 O
            "19, 1130808000, 15.0, 8.5", // 19성: 성공 15.0%, 별하락 O
            "21, 526565100, 15.0, 12.75",// 21성: 성공 15.0%, 별하락 O
            "25, 516676700, 10.0, 18.0"  // 25성: 성공 10.0%, 별하락 O
    })
    @DisplayName("실패(유지): 강화 실패 시 별이 유지되고 비용과 시도 횟수가 누적된다")
    void enhance_failNoDropMaintainsStar(int star, long cost, double successRate, double destroyRate) {
        // given
        StarStatus status = new StarStatus(star, 1_000_000L, false);
        Equipment equipment = new Equipment(level, status);
        StarStatistics stats = initStats();
        double failValue = (successRate + destroyRate / 2.0) / 100;

        // 실패 판정을 위해 randomValue가 successRate 이상, 100-destroyRate 이하가 되도록 설정
        when(mockRandom.nextDouble()).thenReturn(failValue);
        when(repository.findProbability(star))
                .thenReturn(new StarForceProbability(successRate, 100.0 - successRate - destroyRate, destroyRate));
        when(repository.findCost(star, 250)).thenReturn(cost);

        // when
        Equipment result = service.enhance(equipment, stats);

        // then
        assertThat(result.getStar()).isEqualTo(star);
        assertThat(result.isDestroyed()).isFalse();
        assertThat(stats.getTotalCost()).isEqualTo(cost);
        assertThat(stats.getAttempts()).isEqualTo(1L);
    }

    @Test
    @DisplayName("파괴: 강화 파괴 시 12성으로 하락하고 destroyed 플래그가 true가 된다")
    void enhance_destroySetsToTwelveStar() {
        // given 15성: 파괴 2.1%, 비용 139,289,100L
        int star = 15;
        long cost = 139_289_100L;
        double destroyRate = 2.1;
        double destroyThreshold = (100.0 - destroyRate) / 100.0; // 0.979

        StarStatus status = new StarStatus(star, 1_000_000L, false);
        Equipment equipment = new Equipment(level, status);
        StarStatistics stats = initStats();

        // 파괴 판정을 위해 randomValue > (100 - destroyRate) / 100.0 이 되도록 설정
        when(mockRandom.nextDouble()).thenReturn(0.99);
        when(repository.findProbability(star))
                .thenReturn(new StarForceProbability(30.0, 100.0 - 30.0 - destroyRate, destroyRate));
        when(repository.findCost(star, 250)).thenReturn(cost);

        // when
        Equipment result = service.enhance(equipment, stats);

        // then
        assertThat(result.getStar()).isEqualTo(12);
        assertThat(result.isDestroyed()).isTrue();
        assertThat(stats.getTotalCost()).isEqualTo(cost);
        assertThat(stats.getAttempts()).isEqualTo(1L);
        assertThat(stats.getDestruction()).isEqualTo(1L);
    }

    @Test
    @DisplayName("복구: 장비 복구 시 12성으로 복구하고 누적 금액에 장비 가격이 더해진다")
    void enhance_repairAddTotalCost() {
        // given
        StarStatus status = new StarStatus(12, 1_000_000L, true);
        Equipment equipment = new Equipment(level, status);

        StarStatistics statistics = new StarStatistics();

        // when
        Equipment repaired = service.repair(equipment, statistics);

        // then
        assertThat(repaired.isDestroyed()).isFalse();
        assertThat(repaired.getStar()).isEqualTo(12);
        assertThat(statistics.getTotalCost()).isEqualTo(1_000_000L);
    }
}