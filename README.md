# YamlConfig
[![Build Status](https://travis-ci.com/jsixface/YamlConfig.svg?branch=master)](https://travis-ci.com/jsixface/YamlConfig)

[Yaml](https://en.wikipedia.org/wiki/YAML) is a data serialization format similar to JSON but more human readable. It is looks better to organize config in a YAML file since it makes sense to maintian some properties in a hierarchial manner.

**YamlConfig** helps in read configuration for a java project from a YAML config file and access them via dotted notation.

## Features
  - Uses SnakeYAML for reading YAML, so it can handle any data recognizable by SnakeYAML.
  - Ease of access using dotted notation to read properties

## Getting Started
The latest jar can be downloaded from the [releases](https://github.com/jsixface/YamlConfig/releases) page.

If you use Maven for Dependency management, you can include this using below dependency.

```
<dependency>
  <groupId>com.github.jsixface</groupId>
  <artifactId>yamlconfig</artifactId>
  <version>1.0</version>
</dependency>
```

## Usage
Get an instance of the YamlConfig by passing in a reader or an inputstream.

```
InputStream resource = getClass()
                         .getClassLoader()
                         .getResourceAsStream("config.yml");

YamlConfig config = YamlConfig.load(resource);
```

Assume the contents of `config.yml` is as below:

```
services:
  db:
    image: mysql
    container_name: mysql_db
  endpoint:
    - host: example.com
      port: 2976
    - host: example.com
      port: 2978
```

You can access the value of db image by:

```
String imgName = config.getString("services.db.image");
```
It can also be used to get through arrays like below:

```
String value = config.getString("services.endpoint[1].host");
```
