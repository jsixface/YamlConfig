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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YamlConfig {
    private final YamlConfig instance;
    private final Yaml yaml;
    private final Pattern arrayKeyPattern = Pattern.compile("^([a-zA-Z][a-zA-Z0-9]+)\\[([0-9]+)]$");
    private Object content;

    /**
     * Create configuration from Reader
     *
     * @param reader the reader to read config from
     */
    public YamlConfig(Reader reader) {
        this.yaml = new Yaml();
        this.content = yaml.load(reader);
    }


    /**
     * Create configuration from input stream
     *
     * @param inputStream the Input stream to read from
     */
    public YamlConfig(InputStream inputStream) {
        this.yaml = new Yaml();
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
        Object foundNode = getNode(key, content);
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
        Object foundNode = getNode(key, content);
        if (foundNode instanceof Integer) {
            return (Integer) foundNode;
        }
        return null;
    }

    private Object getNode(String key, Object foundNode) {
        String[] parts = splitByDot(key);
        for (String part : parts) {
            int arrayNum = -1;
            Matcher matcher = arrayKeyPattern.matcher(part);
            if (matcher.matches()) {
                part = matcher.group(1);
                arrayNum = Integer.parseInt(matcher.group(2));
            }
            if (foundNode instanceof Map) {
                if (((Map) foundNode).containsKey(part)) {
                    foundNode = ((Map) foundNode).get(part);
                    if (arrayNum >= 0) {
                        if (foundNode instanceof ArrayList
                            && ((ArrayList) foundNode).size() > arrayNum) {
                            foundNode = ((ArrayList) foundNode).get(arrayNum);
                        } else
                            return null;
                    }
                } else
                    return null;
            }
        }
        return foundNode;
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
