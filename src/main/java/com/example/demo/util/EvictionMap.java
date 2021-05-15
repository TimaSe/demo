package com.example.demo.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class EvictionMap<K, V> extends ConcurrentHashMap<K, V> {

    private final long evictionMilli;
    private final Map<K, Long> timeMap = new ConcurrentHashMap<>();

    public EvictionMap(long evictionMilli) {
        this.evictionMilli = evictionMilli;
        initialize();
    }

    void initialize() {
        new ScheduledCleaner().start();
    }

    @Override
    public V get(Object key) {
        return super.get(key);
    }

    @Override
    public V put(K key, V value) {
        long currentTime = Instant.now().toEpochMilli();
        timeMap.put(key, currentTime);
        return super.put(key, value);
    }


    class ScheduledCleaner extends Thread {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                clean();
                Thread.sleep(evictionMilli / 3);
            }
        }

        private void clean() {
            long currentTime = Instant.now().toEpochMilli();
            timeMap.keySet().forEach(key -> {
                if (currentTime > (timeMap.get(key) + evictionMilli)) {
                    remove(key);
                    timeMap.remove(key);
                }
            });
        }
    }
}
