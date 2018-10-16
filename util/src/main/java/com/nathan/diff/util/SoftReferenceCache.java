package com.nathan.diff.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 缓存使用softreferences
 * @param <K>
 * @param <V>
 */
public class SoftReferenceCache<K, V> {
    private final HashMap<K, SoftReference<V>> mCache;

    public SoftReferenceCache() {
        mCache = new HashMap<K, SoftReference<V>>();
    }

    public void put(K key, V value) {
        mCache.put(key, new SoftReference<V>(value));
    }

    public V get(K key) {
        V value = null;

        SoftReference<V> reference = mCache.get(key);

        if (reference != null) {
            value = reference.get();
        }

        return value;
    }
}