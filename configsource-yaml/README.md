# Yaml Config Source

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.microprofile-ext.config-ext/configsource-yaml/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.microprofile-ext.config-ext/configsource-yaml)
[![Javadocs](https://www.javadoc.io/badge/org.microprofile-ext.config-ext/configsource-yaml.svg)](https://www.javadoc.io/doc/org.microprofile-ext.config-ext/configsource-yaml)

This source gets values from some yaml file(s).

## Usage

```xml

    <dependency>
        <groupId>org.microprofile-ext.config-ext</groupId>
        <artifactId>configsource-yaml</artifactId>
        <version>XXXXX</version>
        <scope>runtime</scope>
    </dependency>

```

## Example:

```yaml

    location:
        protocol: "http"
        host: "localhost"
        port: "8080"
        path: "/some/path"
        jedis:
            - Yoda
            - Qui-Gon Jinn
            - Obi-Wan Kenobi
            - Luke Skywalker
```

will create the following properties:

```property
    
    "location.protocol": "http"
    "location.host": "localhost"
    "location.port": "8080"
    "location.path": "/some/path"
    "location.jedis": "[Yoda, Qui-Gon Jinn, Obi-Wan Kenobi, Luke Skywalker]"

```

## Configure options

### Url(s)

By default the config source will look for a file called `application.yaml`. You can set the location(s) of the files:

    configsource.yaml.url=<here the url(s)>

example:

    configsource.yaml.url=file:/tmp/myconfig.yml

You can also add more than one location by comma-separating the location:

    configsource.yaml.url=file:/tmp/myconfig.yml,http://localhost/myconfig.yml

The latest files will override properties in previous files. As example, if using above configuration, property `foo=bar` in `file:/tmp/myconfig.yml` will be override if it's added to `http://localhost/myconfig.yml`.

### Key separator

By default the separator used in the key is a DOT (.) example:

```property
    
    "location.protocol": "http"
```

You can change this by setting `configsource.yaml.keyseparator` to the desired separator, example:

    configsource.yaml.keyseparator=_

will create:

```property
    
    "location_protocol": "http"
```