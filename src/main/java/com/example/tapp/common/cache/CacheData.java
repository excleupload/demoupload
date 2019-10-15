package com.example.tapp.common.cache;
public class CacheData<V> {

    private V value;
    private long time;

    public CacheData(V value, long time) {
        this.value = value;
        this.time = time;
    }

    public V getValue() {
        return value;
    }

    public long getTime() {
        return time;
    }
}