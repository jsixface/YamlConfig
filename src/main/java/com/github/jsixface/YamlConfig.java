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

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YamlConfig {
    private final Pattern arrayKeyPattern = Pattern.compile("^([a-zA-Z][a-zA-Z0-9]+)\\[([0-9]+)]$");
    private final Pattern keyPattern = Pattern.compile("^([a-zA-Z][a-zA-Z0-9]+)\\[([0-9]+)]$");
    private final Object content;

    /**
     * Create configuration from Reader
     *
     * @param reader the reader to read config from
     */
    public YamlConfig(Reader reader) {
        final Yaml yaml = new Yaml();
        this.content = yaml.load(reader);
    }

    /**
     * Create configuration from input stream
     *
     * @param inputStream the Input stream to read from
     */
    public YamlConfig(InputStream inputStream) {
        final Yaml yaml = new Yaml();
        this.content = yaml.load(inputStream);
    }

    /**
     * Create configuration from input stream, using your yaml instance
     *
     * @param yaml        the Yaml instance to use
     * @param inputStream the Input stream to read from
     */
    public YamlConfig(Yaml yaml, InputStream inputStream) {
        this.content = yaml.load(inputStream);
    }

    /**
     * Gets the String value for the specified key from the config.
     *
     * @param key Key in dotted notation like <code>first.second[2].third</code>
     * @return The String value of property.
     * <p>
     * <code>null</code> if the key is not present
     * or not a leaf node.
     * <p>
     * <code>Boolean</code> or <code>Integer</code> or another format is converted to String.
     */
    public String getString(String key) {
        Object foundNode = getNode(key);
        if (foundNode != null && !(foundNode instanceof Collection)) {
            return foundNode.toString();
        }
        return null;
    }

    /**
     * Gets the Integer value for the specified key from the config.
     *
     * @param key Key in dotted notation like <code>first.second[2].third</code>
     * @return The Integer value of property.
     * <p>
     * <code>null</code> if the key is not present or not a leaf node.
     */
    public Integer getInt(String key) {
        Object node = getNode(key);
        if (node instanceof Integer) {
            return (Integer) node;
        }
        return null;
    }

    /**
     * Gets a type list value for the specified key from the config.
     *
     * @param key Key in dotted notation like <code>first.second</code>
     * @return The type list value of property.
     * <p>
     * <code>null</code> if the key is not present or not a leaf node.
     */
    public <T> ArrayList<T> getList(String key, Class<T> type) {
        final ArrayList<?> node = (ArrayList<?>) getNode(key);
        final ArrayList<T> typeList = new ArrayList<>();
        if (node != null) {
            try {
                node.forEach(o -> typeList.add(type.cast(o)));
                return typeList;
            } catch (ClassCastException exception) {
                return null;
            }
        }
        return null;
    }

    /**
     * Gets a node at the specific index.
     * The key follows this pattern: my.key[index].entry
     *
     * @param key the key to find
     * @return the found node or <code>null</code> if not found
     */
    private Object getNode(String key) {
        final String[] parts = splitByDot(key);

        Object node = content;
        for (String part : parts) {
            int arrayNum = -1;
            final Matcher arrayKeyPatternMatcher = arrayKeyPattern.matcher(part);
            final Matcher keyPatternMatcher = keyPattern.matcher(part);
            if (arrayKeyPatternMatcher.matches()) {
                part = arrayKeyPatternMatcher.group(1);
                arrayNum = Integer.parseInt(arrayKeyPatternMatcher.group(2));
            } else if (keyPatternMatcher.matches()) {
                part = keyPatternMatcher.group(1);
            }
            if (node instanceof Map) {
                if (((Map<?, ?>) node).containsKey(part)) {
                    node = ((Map<?, ?>) node).get(part);
                    if (arrayNum >= 0) {
                        if (node instanceof ArrayList<?> && ((ArrayList<?>) node).size() > arrayNum) {
                            node = ((ArrayList<?>) node).get(arrayNum);
                        }
                    }
                } else {
                    return null;
                }
            }
        }
        return node;
    }


    /**
     * Splits a key by the dot character.
     *
     * @param key the key to split
     * @return the split key path.
     */
    private String[] splitByDot(String key) {
        return key.split("\\.");
    }
}
