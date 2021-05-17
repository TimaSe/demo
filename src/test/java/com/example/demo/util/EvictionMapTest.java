package com.example.demo.util;


import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class EvictionMapTest extends AbstractTest {

    @Test
    public void testEvictionMap_general_scenario() throws InterruptedException {
        EvictionMap<Key, String> map = new EvictionMap<>(10000);
        map.put(Key.ONE, Value.TEST_1.name());
        map.put(Key.TWO, Value.TEST_2.name());

        String value = map.get(Key.ONE);
        assertThat(value).isEqualTo(Value.TEST_1.name());

        value = map.get(Key.TWO);
        assertThat(value).isEqualTo(Value.TEST_2.name());

        Thread.sleep(15000);

        value = map.get(Key.ONE);
        assertThat(value).isNull();

        value = map.get(Key.TWO);
        assertThat(value).isNull();
    }

    @Test
    public void testEvictionMap_threadSafety() {
        Map<Integer, Integer> map = new EvictionMap<>(10000);
        List<Integer> list = IntStream.range(0, 10000)
                .boxed()
                .collect(Collectors.toList());

        list.parallelStream()
                .forEach(i -> map.put(i, i));

        assertThat(list.size()).isEqualTo(map.size());
    }

    @Test
    public void testFrequentPUT() {
        EvictionMap<Key, String> map = new EvictionMap<>(10000);
        map.put(Key.ONE, Value.TEST_1.name());
        map.put(Key.TWO, Value.TEST_2.name());
        map.put(Key.THREE, Value.TEST_3.name());
        map.put(Key.FOUR, Value.TEST_4.name());
        map.put(Key.FIVE, Value.TEST_5.name());
        map.put(Key.SIX, Value.TEST_6.name());
        map.put(Key.SEVEN, Value.TEST_7.name());
        map.put(Key.EIGHT, Value.TEST_8.name());
        map.put(Key.NINE, Value.TEST_9.name());
        map.put(Key.TEN, Value.TEST_10.name());

        String value = map.get(Key.SIX);
        assertThat(value).isEqualTo(Value.TEST_6.name());

        map.put(Key.ONE, Value.TEST_10.name());
        map.put(Key.TWO, Value.TEST_9.name());
        map.put(Key.THREE, Value.TEST_8.name());
        map.put(Key.FOUR, Value.TEST_7.name());
        map.put(Key.FIVE, Value.TEST_6.name());
        map.put(Key.SIX, Value.TEST_5.name());
        map.put(Key.SEVEN, Value.TEST_4.name());
        map.put(Key.EIGHT, Value.TEST_3.name());
        map.put(Key.NINE, Value.TEST_2.name());
        map.put(Key.TEN, Value.TEST_1.name());

        value = map.get(Key.THREE);
        assertThat(value).isEqualTo(Value.TEST_8.name());
    }

    @Test
    public void testFrequentGET() {
        EvictionMap<Key, String> map = new EvictionMap<>(10000);
        map.put(Key.ONE, Value.TEST_1.name());

        assertThat(map.get(Key.ONE)).isEqualTo(Value.TEST_1.name());
        assertThat(map.get(Key.ONE)).isEqualTo(Value.TEST_1.name());
        assertThat(map.get(Key.ONE)).isEqualTo(Value.TEST_1.name());

        map.put(Key.TEN, Value.TEST_4.name());

        assertThat(map.get(Key.TEN)).isEqualTo(Value.TEST_4.name());
        assertThat(map.get(Key.TEN)).isEqualTo(Value.TEST_4.name());
        assertThat(map.get(Key.TEN)).isEqualTo(Value.TEST_4.name());
    }

    @Test
    public void testFrequentPUT_GET() {
        Map<Integer, Integer> map = new EvictionMap<>(10000);
        List<Integer> list = IntStream.range(0, 10000000)
                .boxed()
                .collect(Collectors.toList());

        Random random = new Random();

        list.parallelStream()
                .forEach(i -> {
                    if (random.nextBoolean()) map.put(i, i);
                    else map.get(i);
                });
    }

}
