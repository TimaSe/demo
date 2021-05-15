package com.example.demo.util;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class EvictionMapTest {

    enum Key {
        ONE,
        TWO
    }

    @Test
    public void testEvictionMap() throws InterruptedException {
        EvictionMap<Key, String> map = new EvictionMap<>(10000);
        map.put(Key.ONE, "test");
        String value = map.get(Key.ONE);
        assertThat(value).isEqualTo("test");
        Thread.sleep(10000);
        value = map.get(Key.ONE);
        assertThat(value).isNull();
    }
}
