/*
 * Copyright (c) 2008-2018, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.spi.discovery;

import com.hazelcast.config.properties.PropertyDefinition;
import com.hazelcast.config.properties.PropertyTypeConverter;
import com.hazelcast.config.properties.SimplePropertyDefinition;

public final class DNSDiscoveryConfiguration {

    /**
     * <tt>site-domain</tt> configures the basic site domain for the lookup, to
     * find other sub-domains of the cluster members and retrieve their assigned
     * IP addresses.
     */
    public static final PropertyDefinition DOMAIN = new SimplePropertyDefinition("site-domain", PropertyTypeConverter.STRING);
    public static final PropertyDefinition PORT = new SimplePropertyDefinition("port", PropertyTypeConverter.INTEGER);

    // prevent instantiation
    private DNSDiscoveryConfiguration() {
    }
}
