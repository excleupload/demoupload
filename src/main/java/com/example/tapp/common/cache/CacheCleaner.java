package com.example.tapp.common.cache;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class CacheCleaner<K, V> {

    int delay;
    Cache<K, V> cache;
    ConcurrentHashMap<K, Timer> timers;

    public CacheCleaner(Cache<K, V> cache, int minute) {
        this.cache = cache;
        this.delay = (minute * (1000 * 60));
        this.timers = new ConcurrentHashMap<>(10);
    }

    public void add(K key) {
        Timer timer = new Timer(true);
       // timer.schedule(new CleanerTask<>(timers, cache, key), delay);
        timers.put(key, timer);
    }

    public void remove(K key) {
        Timer timer = timers.get(key);
        if (timer != null) {
            timer.cancel();
            timers.remove(key);
        }
    }

    @SuppressWarnings("unused")
	private static class CleanerTask<K, V> extends TimerTask {

        K key;
        Cache<K, V> cache;
        ConcurrentHashMap<K, Timer> timers;

        public CleanerTask(ConcurrentHashMap<K, Timer> timers, Cache<K, V> cache, K key) {
            this.cache = cache;
            this.key = key;
            this.timers = timers;
        }

        @Override
        public void run() {
            cache.remove(key);
            this.timers.remove(key);
            System.out.println("Remove : " + key);
        }
    }
}
