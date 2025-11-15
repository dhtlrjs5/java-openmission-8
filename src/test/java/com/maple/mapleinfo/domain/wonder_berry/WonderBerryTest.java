package com.maple.mapleinfo.domain.wonder_berry;

import com.maple.mapleinfo.utils.Rarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WonderBerryTest {

    private WonderBerry wonderBerry;
    private Random mockRandom;
    private List<Item> testItems;

    @BeforeEach
    void setUp() throws Exception {
        testItems = Arrays.asList(
                new Item("RARE", 10.0, Rarity.RARE),
                new Item("NORMAL", 90.0, Rarity.NORMAL)
        );

        mockRandom = mock(Random.class);
        wonderBerry = new WonderBerry(testItems);

        Field itemsField = WonderBerry.class.getDeclaredField("items");
        itemsField.setAccessible(true);
        WonderBerryItems itemsInstance = (WonderBerryItems) itemsField.get(wonderBerry);

        Field randomField = WonderBerryItems.class.getDeclaredField("random");
        randomField.setAccessible(true);

        randomField.set(itemsInstance, mockRandom);
    }

    @Test
    @DisplayName("성공: 1회 사용 시 RARE를 뽑고 통계를 업데이트한다")
    void useWonderBerry_Success_RareItem() {
        // given
        // random.nextDouble() * 100이 첫 번째 아이템의 누적 확률 (10.0) 이하가 되도록
        when(mockRandom.nextDouble()).thenReturn(0.09);

        // when
        WonderResult result = wonderBerry.useWonderBerry();
        WonderStatistics stats = result.getStatistics();

        // then
        assertEquals("RARE", result.getItem().getName(), "RARE가 뽑혀야 한다");
        assertEquals(1L, stats.getCount(), "사용 횟수가 1 증가해야 한다");
        assertEquals(5500L, stats.getCost(), "비용이 5500L 증가해야 한다"); // 상수 사용이 불가능하여 매직넘버 유지
        assertEquals(1L, stats.getItemCount().get("RARE"), "RARE 획득 횟수가 1 증가해야 한다");
    }

    @Test
    @DisplayName("성공: 10회 사용 시 10번의 사용이 누적되며 비용이 55000L 증가한다")
    void useTenTimes_Success() {
        // given
        // 10회 동안 항상 NORMAL을 뽑도록 Mock
        when(mockRandom.nextDouble()).thenReturn(0.50);

        // 예상되는 총 비용: 5500L * 10
        final long EXPECTED_TOTAL_COST = 55000L;
        final long EXPECTED_COUNT = 10L;

        // when
        WonderResult result = wonderBerry.useTenTimes();
        WonderStatistics stats = result.getStatistics();

        // then
        assertEquals(EXPECTED_COUNT, stats.getCount(), "사용 횟수가 10 증가해야 한다");
        assertEquals(EXPECTED_TOTAL_COST, stats.getCost(), "비용이 5500L * 10 증가해야 한다");
        assertEquals(EXPECTED_COUNT, stats.getItemCount().get("NORMAL"), "아이템 획득 횟수가 10 증가해야 한다");
    }

    @Test
    @DisplayName("성공: reset() 호출 시 통계가 초기화된다")
    void reset_Success() {
        // given
        when(mockRandom.nextDouble()).thenReturn(0.09);
        wonderBerry.useWonderBerry();

        // when
        WonderResult result = wonderBerry.reset();
        WonderStatistics stats = result.getStatistics();

        // then
        assertEquals(0L, stats.getCount(), "사용 횟수가 0으로 초기화되어야 한다");
        assertEquals(0L, stats.getCost(), "비용이 0으로 초기화되어야 한다");
        assertTrue(stats.getItemCount().isEmpty(), "아이템 카운트 맵이 비어야 한다");
    }
}