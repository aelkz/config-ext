# Etcd Config Source

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.microprofile-ext.config-ext/configsource-etcd/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.microprofile-ext.config-ext/configsource-etcd)
[![Javadocs](https://www.javadoc.io/badge/org.microprofile-ext.config-ext/configsource-etcd.svg)](https://www.javadoc.io/doc/org.microprofile-ext.config-ext/configsource-etcd)

Use [etcd](https://coreos.com/etcd/) to get config values. You can define the server details of the etcd server using MicroProfile Config:

## Usage

```xml

    <dependency>
        <groupId>org.microprofile-ext.config-ext</groupId>
        <artifactId>configsource-etcd</artifactId>
        <version>XXXXXX</version>
        <scope>runtime</scope>
    </dependency>

```

## Configure options

    configsource.etcd.scheme=http (default)
    configsource.etcd.host=localhost (default)
    configsource.etcd.port2379 (default)  

You can disable the config source by setting this config:
    
    EtcdConfigSource.enabled=false  