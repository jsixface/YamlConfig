# YamlConfig
[![Build Package](https://github.com/jsixface/YamlConfig/actions/workflows/build-package.yml/badge.svg)](https://github.com/jsixface/YamlConfig/actions/workflows/build-package.yml)

[Yaml](https://en.wikipedia.org/wiki/YAML) is a data serialization format similar to JSON but more human readable. 
It looks better to organize config in a YAML file since it makes sense to maintain some properties in a hierarchical manner.

**YamlConfig** helps read configuration for a java project from a YAML config file and access them via dotted notation.

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
  <version>1.2.0</version>
</dependency>
```
Or if you use Gradle, you can include this using below dependency.

``` 
implementation 'com.github.jsixface:yamlconfig:1.2.0'
```

## Usage - internal Yaml
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

## Usage - externally supplied Yaml
As above, but with you supplying the Yaml instance, allowing Yaml to template environment variables to override yaml supplied values.

```
# externally managed and configured Yaml instance.
Yaml yaml = new Yaml(new EnvScalarConstructor());
yaml.addImplicitResolver(EnvScalarConstructor.ENV_TAG, EnvScalarConstructor.ENV_FORMAT, "$");

# externally managed yml file
InputStream resource = getClass()
                         .getClassLoader()
                         .getResourceAsStream("config.yml");
                         
# pass the lot to YamlConfig
YamlConfig config = YamlConfig.load(yaml, resource);

# and now you can use fully qualified dot notation keys:
config.getString("service.db.someKey");
```
allowing `*.yml` files to contain `key: ${ENV_KEY:-defaultValue}` like this:
```yaml
services:
  db:
    someKey: ${SOME_KEY:-defaultValue}
```
see the test [YamlConfigWithEnvOverridesTest.java](src/test/java/com/github/jsixface/YamlConfigWithEnvOverridesTest.java)
and its config file: [testWithEnvOverrides.yml](src/test/resources/testWithEnvOverrides.yml)