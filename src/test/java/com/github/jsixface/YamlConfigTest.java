/*
 * Copyright 2019 Arumugam Jeganathan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jsixface;

import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class YamlConfigTest {
    private final InputStream resource = getClass().getClassLoader().getResourceAsStream("test.yml");

    @Test
    public void getStringList() {
        final YamlConfig config = YamlConfig.load(resource);
        final String value = config.getString("services.names");
        assertNotNull(value);
        assertEquals("Andrew", value);
    }

    @Test
    public void getStringArray() {
        final YamlConfig config = YamlConfig.load(resource);
        final String value = config.getString("services.names[1].first");
        assertNotNull(value);
        assertEquals("Andrew", value);
    }

    @Test
    public void getStringOutOfIndex() {
        final YamlConfig config = YamlConfig.load(resource);
        final String value = config.getString("services.names[3].first");
        assertNull(value);
    }

    @Test
    public void getStringInvalidKey() {
        final YamlConfig config = YamlConfig.load(resource);
        final String value = config.getString("services.test.first");
        assertNull(value);
    }

    @Test
    public void getStringNumber() {
        final YamlConfig config = YamlConfig.load(resource);
        final String value = config.getString("version");
        assertNotNull(value);
        assertEquals("3", value);
    }

    @Test
    public void getString() {
        final YamlConfig config = YamlConfig.load(resource);
        final String value = config.getString("services.db.image");
        assertNotNull(value);
        assertEquals("mysql", value);
    }

    @Test
    public void getInt() {
        final YamlConfig config = YamlConfig.load(resource);
        final int value = config.getInt("version");
        assertEquals(3, value);
    }
}