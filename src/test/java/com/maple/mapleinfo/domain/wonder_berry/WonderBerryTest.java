package com.maple.mapleinfo.service.wonder_berry;

import com.maple.mapleinfo.domain.wonder_berry.WonderBerryItems;
import com.maple.mapleinfo.domain.wonder_berry.WonderResult;
import com.maple.mapleinfo.domain.wonder_berry.WonderStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WonderBerryServiceTest {

    @InjectMocks
    private WonderBerryService wonderBerryService; // ⭐ 테스트 대상: WonderBerryService로 변경

    @Mock
    private Random mockRandom;

    @BeforeEach
    void setUp() throws Exception {
        Field itemsField = WonderBerryService.class.getDeclaredField("items");
        itemsField.setAccessible(true);
        WonderBerryItems itemsInstance = (WonderBerryItems) itemsField.get(wonderBerryService);

        Field randomField = WonderBerryItems.class.getDeclaredField("random");
        randomField.setAccessible(true);

        randomField.set(itemsInstance, mockRandom);
    }

    @Test
    @DisplayName("성공: 1회 사용 시 RARE를 뽑고 통계를 업데이트한다")
    void useWonderBerry_Success_RareItem() {
        // given
        when(mockRandom.nextDouble()).thenReturn(0.01);

        WonderStatistics statistics = new WonderStatistics();

        // when
        WonderResult result = wonderBerryService.useBerry(statistics);
        WonderStatistics stats = result.getStatistics();

        // then
        assertEquals("크림", result.getItem().getName(), "크림이 뽑혀야 한다");
        assertEquals(1L, stats.getCount(), "사용 횟수가 1 증가해야 한다");
        assertEquals(5400L, stats.getCost(), "비용이 5400L 증가해야 한다");
        assertEquals(1L, stats.getItemCount().get("크림"), "크림 획득 횟수가 1 증가해야 한다");
    }

    @Test
    @DisplayName("성공: 10회 사용 시 10번의 사용이 누적되며 비용이 54000L 증가한다")
    void useTenTimes_Success() {
        // given
        // 10회 동안 NORMAL 아이템('제나')을 뽑도록 Mock
        when(mockRandom.nextDouble()).thenReturn(0.50);

        final long EXPECTED_TOTAL_COST = 5400L * 10;
        final long EXPECTED_COUNT = 10L;

        WonderStatistics statistics = new WonderStatistics();

        // when
        WonderResult result = wonderBerryService.useTenBerries(statistics);
        WonderStatistics stats = result.getStatistics();

        // then
        assertEquals(EXPECTED_COUNT, stats.getCount(), "사용 횟수가 10 증가해야 한다");
        assertEquals(EXPECTED_TOTAL_COST, stats.getCost(), "비용이 5400L * 10 증가해야 한다");
        assertEquals(EXPECTED_COUNT, stats.getItemCount().get("제나"), "식빵이 획득 횟수가 10 증가해야 한다");
    }

    @Test
    @DisplayName("성공: reset() 호출 시 통계가 초기화된다")
    void reset_Success() {
        // given
        when(mockRandom.nextDouble()).thenReturn(0.01);
        WonderStatistics statistics = new WonderStatistics();
        wonderBerryService.useBerry(statistics); // 1회 사용으로 상태를 만듬

        // when
        WonderResult result = wonderBerryService.reset(statistics);
        WonderStatistics stats = result.getStatistics();

        // then
        assertEquals(0L, stats.getCount(), "사용 횟수가 0으로 초기화되어야 한다");
        assertEquals(0L, stats.getCost(), "비용이 0으로 초기화되어야 한다");
        assertTrue(stats.getItemCount().isEmpty(), "아이템 카운트 맵이 비어야 한다");
    }
}