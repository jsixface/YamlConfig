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