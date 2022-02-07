# DNSDiscovery
Hazelcast Discovery SPI



# Hazelcast Discovery Plugin for DNS

This repository contains a plugin which provides the automatic Hazelcast member discovery using DNS resolve..

## Requirements

* Hazelcast 3.12
* Linux Kernel 3.19+ (TCP connections may get stuck when used with older Kernel versions, resulting in undefined timeouts)
* Versions compatibility:
  * hazelcast-dns 2.4 is compatible with hazelcast 3.12.x

## Embedded mode

To use Hazelcast embedded in your application, you need to add the plugin dependency into your Maven/Gradle file. Then, when you provide `hazelcast.xml`/`hazelcast.yaml` as presented below or an equivalent Java-based configuration, your Hazelcast instances discover themselves automatically.

#### Maven

```xml
<dependency>
  <groupId>com.hazelcast</groupId>
  <artifactId>hazelcast-dns</artifactId>
  <version>${hazelcast-dns.version}</version>
</dependency>
```

#### Gradle

```groovy
compile group: "com.hazelcast", name: "hazelcast-dns", version: "${hazelcast-dns.version}"
```

## Understanding DNS Discovery Strategy


Make sure that:

* you have the `hazelcast-dns.jar` (or `hazelcast-all.jar`) dependency in your classpath

## Configure the DNSDiscoveryStrategy
```xml
<network>
        <join>
            <!-- deactivating other discoveries -->
            <multicast enabled="false"/>
            <tcp-ip enabled="false"/>
            <aws enabled="false"/>
            <!-- activate our discovery strategy -->
            <discovery-strategies>
                <!-- class equals to the DiscoveryStrategy not the factory! -->
                <discovery-strategy enabled="true" class="com.hazelcast.spi.discovery.DNSDiscoveryStrategy">
                    <properties>
                        <property name="port">5801</property>
                        <property name="site-domain">cluster.local</property>
                    </properties>
                </discovery-strategy>
            </discovery-strategies>
        </join>
    </network>
```
