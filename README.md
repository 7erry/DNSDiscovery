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

To use Hazelcast embedded in your application, you need to add the plugin dependency into your Maven/Gradle file (or use [hazelcast-all](https://mvnrepository.com/artifact/com.hazelcast/hazelcast-all) which already includes the plugin). Then, when you provide `hazelcast.xml`/`hazelcast.yaml` as presented below or an equivalent Java-based configuration, your Hazelcast instances discover themselves automatically.

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

## High Availability

By default, Hazelcast distributes partition replicas (backups) randomly and equally among cluster members. However, this is not safe in terms of high availability when a partition and its replicas are stored on the same rack, using the same network, or power source. To deal with that, Hazelcast offers logical partition grouping, so that a partition
itself and its backup(s) would not be stored within the same group. This way Hazelcast guarantees that a possible failure
affecting more than one member at a time will not cause data loss. The details of partition groups can be found in the
documentation: 
[Partition Group Configuration](https://docs.hazelcast.org/docs/latest/manual/html-single/#partition-group-configuration)

In addition to two built-in grouping options `ZONE_AWARE` and `PLACEMENT_AWARE`, you can customize the formation of
these groups based on the network interfaces of members. See more details on custom groups in the documentation:
[Custom Partition Groups](https://docs.hazelcast.org/docs/latest/manual/html-single/#custom).


### Multi-Zone Deployments

If `ZONE_AWARE` partition group is enabled, the backup(s) of a partition is always stored in a different availability
zone. Hazelcast AWS Discovery plugin supports ZONE_AWARE feature for both EC2 and ECS.

***NOTE:*** *When using the `ZONE_AWARE` partition grouping, a cluster spanning multiple Availability Zones (AZ)
should have an equal number of members in each AZ. Otherwise, it will result in uneven partition distribution among
the members.*

#### XML Configuration

```xml
<partition-group enabled="true" group-type="ZONE_AWARE" />
```

#### YAML Configuration

```yaml
hazelcast:
  partition-group:
    enabled: true
    group-type: ZONE_AWARE
```

#### Java-based Configuration

```java
config.getPartitionGroupConfig()
    .setEnabled(true)
    .setGroupType(MemberGroupType.ZONE_AWARE);
```

### Partition Placement Group Deployments

[AWS Partition Placement Group](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/placement-groups.html#placement-groups-partition)
(PPG) ensures low latency between the instances in the same partition of a placement group
and also provides availability since no two partitions share the same underlying hardware. As long as the partitions of a 
PPG contain an equal number of instances, it will be good practice for Hazelcast clusters formed within a single zone.

If EC2 instances belong to a PPG and `PLACEMENT_AWARE` partition group is enabled, then Hazelcast members will be grouped
by the partitions of the PPG. For instance, the Hazelcast members in the first partition of a PPG named `ppg` will belong
to the partition group of `ppg-1`, and those in the second partition will belong to `ppg-2` and so on. Furthermore, these
groups will be specific to each availability zone. That is, they are formed with zone names as well: `us-east-1-ppg-1`,
`us-east-2-ppg-1`, and the like. However, if a Hazelcast cluster spans multiple availability zones then you should
consider using `ZONE_AWARE`.

### Cluster Placement Group Deployments

[AWS Cluster Placement Group](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/placement-groups.html#placement-groups-cluster)
(CPG) ensures low latency by packing instances close together inside an availability zone.
If you favor latency over availability, then CPG will serve your purpose.

***NOTE:*** *In the case of CPG, using `PLACEMENT_AWARE` has no effect, so can use the default Hazelcast partition group
strategy.*

### Spread Placement Group Deployments

[AWS Spread Placement Groups](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/placement-groups.html#placement-groups-spread)
(SPG) ensures high availability in a single zone by placing each instance in a group on a
distinct rack. It provides better latency than multi-zone deployment, but worse than Cluster Placement Group. SPG is
limited to 7 instances, so if you need a larger Hazelcast cluster within a single zone, you should use PPG instead.

***NOTE:*** *In the case of SPG, using `PLACEMENT_AWARE` has no effect, so can use the default Hazelcast partition group
strategy.*

#### XML Configuration

```xml
<partition-group enabled="true" group-type="PLACEMENT_AWARE" />
```

#### YAML Configuration

```yaml
hazelcast:
  partition-group:
    enabled: true
    group-type: PLACEMENT_AWARE
```

#### Java-based Configuration

```java
config.getPartitionGroupConfig()
    .setEnabled(true)
    .setGroupType(MemberGroupType.PLACEMENT_AWARE);
```

## Autoscaling

Hazelcast is prepared to work correctly within the autoscaling environments. Note that there are two specific requirements to prevent Hazelcast from losing data:
* the number of members must change by 1 at the time
* when a member is launched or terminated, the cluster must be in the safe state

Read about details in the blog post: [AWS Auto Scaling with Hazelcast](https://hazelcast.com/blog/aws-auto-scaling-with-hazelcast/).

## AWS EC2 Deployment Guide

You can download the white paper "Amazon EC2 Deployment Guide for Hazelcast IMDG" [here](https://hazelcast.com/resources/amazon-ec2-deployment-guide/).

## How to find us?

In case of any question or issue, please raise a GH issue, send an email to [Hazelcast Google Groups](https://groups.google.com/forum/#!forum/hazelcast) or contact as directly via [Hazelcast Gitter](https://gitter.im/hazelcast/hazelcast).
