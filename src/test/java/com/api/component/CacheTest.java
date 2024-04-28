package com.api.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CacheTest {

    private Cache cache;

    @BeforeEach
    void setUp() {
        cache = new Cache();
    }

    @Test
    void testPutAndGet() {
        // Помещаем значение в кэш
        cache.put("key1", "value1");

        // Получаем значение из кэша
        Object value = cache.get("key1");

        // Проверяем, что значение получено корректно
        assertEquals("value1", value);
    }

    @Test
    void testRemove() {
        // Помещаем значение в кэш
        cache.put("key1", "value1");

        // Удаляем значение из кэша
        cache.remove("key1");

        // Проверяем, что значение удалено
        assertNull(cache.get("key1"));
    }

    @Test
    void testClear() {
        // Помещаем несколько значений в кэш
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        // Очищаем кэш
        cache.clear();

        // Проверяем, что кэш пустой
        assertTrue(cache.get("key1") == null && cache.get("key2") == null && cache.get("key3") == null);
    }

    @Test
    void testEviction() {
        // Помещаем 10 значений в кэш
        for (int i = 1; i <= 10; i++) {
            cache.put("key" + i, "value" + i);
        }

        // Помещаем еще одно значение
        cache.put("newKey", "newValue");

        // Проверяем, что самое старое значение было вытеснено
        assertNull(cache.get("key1"));
    }
}
