package com.example.tapp.common.cache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Cache<K, V> {

    private int time = 1;
    private ConcurrentHashMap<K, CacheData<V>> cache;
    private CacheCleaner<K, V> cleaner;

    public Cache(int validityInMinute) {
        this.time = validityInMinute;
        cache = new ConcurrentHashMap<>(10);
        cleaner = new CacheCleaner<>(this, time);
    }

    public synchronized void put(K key, V value) {
        cache.put(key, new CacheData<V>(value, getValidity()));
        cleaner.remove(key);
        cleaner.add(key);
    }

    public synchronized V get(K key) throws CacheException {
        CacheData<V> cacheData = cache.get(key);
        if (cacheData == null)
            throw new CacheException("Key does not exist.");

        if (System.currentTimeMillis() > cacheData.getTime())
            throw new CacheException("Key  has been expired.");

        return cacheData.getValue();
    }

    public synchronized void remove(K key) {
        cache.remove(key);
        cleaner.remove(key);
    }

    public synchronized void cleanUp() {
        Set<K> keys = cache.keySet();
        keys.stream().forEach(key -> {
            CacheData<V> cacheData = cache.get(key);
            if (System.currentTimeMillis() > cacheData.getTime())
                cache.remove(key);
        });
    }

    long getValidity() {
        return System.currentTimeMillis() + (time * (1000 * 60));
    }
}
