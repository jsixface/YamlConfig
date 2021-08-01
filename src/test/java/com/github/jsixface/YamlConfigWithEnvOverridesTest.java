/*
 * Copyright 2021 https://github.com/alfanse
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.env.EnvScalarConstructor;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test supplying YamlConfig with a Yaml instance,
 * preconfigured to use Environment variables and defaults.
 * <p>
 * i.e.
 * key: ${ENV_KEY:-someDefault}
 */
public class YamlConfigWithEnvOverridesTest {

    public static final String ENV_OVERRIDE_VALUE = "my db image env value";
    private final InputStream resource = getClass().getClassLoader().getResourceAsStream("testWithEnvOverrides.yml");

    private YamlConfig config;

    @Before
    public void setUp() {
        Yaml yaml = givenYamlInstanceWithEnvScalar();

        config = YamlConfig.load(yaml, resource);
    }

    private Yaml givenYamlInstanceWithEnvScalar() {
        Yaml yaml = new Yaml(new EnvScalarConstructor());
        yaml.addImplicitResolver(EnvScalarConstructor.ENV_TAG, EnvScalarConstructor.ENV_FORMAT, "$");
        return yaml;
    }

    @After
    public void tearDown() throws Exception {
        if (resource != null)
            resource.close();
    }

    @Test
    public void singleKeyAsInt() {
        assertEquals(Integer.valueOf(3), config.getInt("version"));
    }

    @Test
    public void singleKeyAsString() {
        assertEquals("3", config.getString("version"));
    }

    @Test
    public void singleKeyWithQuotedStringValueAsString() {
        assertEquals("Quoted String", config.getString("key"));
    }

    @Test
    public void chainedPropertyKeyAsString() {
        assertEquals("someString",
                config.getString("services.db.chained"));
    }

    @Test
    public void chainedPropertyKeyOverriddenByEnvironment() {
        //env set correctly (see pom)
        assertTrue(System.getenv().containsKey("DB_IMAGE"));
        assertEquals(ENV_OVERRIDE_VALUE, System.getenv().get("DB_IMAGE"));

        assertEquals(ENV_OVERRIDE_VALUE,
                config.getString("services.db.env_override"));
    }

    @Test
    public void chainedPropertyKeyUsesDefaultWhenEnvKeyNotPresent() {
        //env not set.
        assertFalse(System.getenv().containsKey("DB_CONTAINER_NAME"));

        //default used
        assertEquals("mysql_db",
                config.getString("services.db.env_unset_with_default"));
    }

    @Test
    public void chainedPropertyKeyWhenMissingEnvValueAndNoDefaultReturnsEmptyString() {
        //env not set.
        assertFalse(System.getenv().containsKey("MISSING_ENV"));

        assertEquals("",
                config.getString("services.db.env_unset_noDefault"));
    }

    @Test
    public void chainedPropertyKeyInArray() {
        assertEquals("James",
                config.getString("services.names[0].first"));
    }

    @Test
    public void chainedPropertyKeyInArrayEnvUnsetUsesDefault() {
        assertEquals("John",
                config.getString("services.names[1].first"));
    }

    @Test
    public void chainedPropertyKeyInArrayOutOfIndex() {
        assertNull(config.getString("services.names[3].first"));
    }

    @Test
    public void invalidChainedPropertyKeyReturnsNull() {
        assertNull(config.getString("services.test.first"));
    }
}