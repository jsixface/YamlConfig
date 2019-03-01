package com.github.jsixface;

import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class YamlConfigTest {
    private InputStream resource = getClass().getClassLoader().getResourceAsStream("test.yml");

    @Test
    public void load() {
    }

    @Test
    public void getStringArray() {
        YamlConfig config = YamlConfig.load(resource);
        String value = config.getString("services.names[1].first");
        assertNotNull(value);
        assertEquals("Andrew", value);
    }

    @Test
    public void getStringOutOfIndex() {
        YamlConfig config = YamlConfig.load(resource);
        String value = config.getString("services.names[3].first");
        assertNull(value);
    }

    @Test
    public void getStringInvalidKey() {
        YamlConfig config = YamlConfig.load(resource);
        String value = config.getString("services.test.first");
        assertNull(value);
    }

    @Test
    public void getStringNumber() {
        YamlConfig config = YamlConfig.load(resource);
        String value = config.getString("version");
        assertNotNull(value);
        assertEquals("3", value);
    }

    @Test
    public void getString() {
        YamlConfig config = YamlConfig.load(resource);
        String value = config.getString("services.db.image");
        assertNotNull(value);
        assertEquals("mysql", value);
    }

    @Test
    public void getInt() {
        YamlConfig config = YamlConfig.load(resource);
        Integer value = config.getInt("version");
        assertNotNull(value);
        assertEquals(Integer.valueOf(3), value);
    }
}