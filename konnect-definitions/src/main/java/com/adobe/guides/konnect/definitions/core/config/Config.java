/*
Copyright 2024 Adobe. All rights reserved.
This file is licensed to you under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy
of the License at http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under
the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
OF ANY KIND, either express or implied. See the License for the specific language
governing permissions and limitations under the License.
*/
package com.adobe.guides.konnect.definitions.core.config;

/**
 * An external data source configuration. The user of this interface
 * has precise control over what authentication method will be
 * used to connect to an external data source.
 * <p>
 * The <tt>Config</tt> interface provides just one method <tt>getName</tt>
 * that needs to be implemented.
 * <p>
 * The <tt>Config</tt> interface provides flexibility in how the
 * implementations of this interface provide their authentication.
 * <p>
 * The <tt>Config</tt> implementation along with its corresponding
 * {@link com.adobe.guides.konnect.definitions.core.connector.Connector}
 * implementation is a complete workflow for any external data source
 * connector to work.
 * <p>
 * The {@code Connector} takes a {@code Config} to be able to execute and
 * validate queries.
 *
 * @author Adobe
 * @since 1.0.0
 */
public interface Config {

    /**
     * Returns the unique name for this config.
     *
     * @return a {@code String} which is the name of this config.
     */
    String getName();
}
