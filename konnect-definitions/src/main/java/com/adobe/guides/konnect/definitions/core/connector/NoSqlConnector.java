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
package com.adobe.guides.konnect.definitions.core.connector;

import com.adobe.guides.konnect.definitions.core.config.ConfigDto;
import com.adobe.guides.konnect.definitions.core.query.QueryInfoDto;

import java.util.List;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.ADOBE_SYSTEMS;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.NOSQL_DB;

/**
 * This class provides a skeletal implementation of the {@link Connector}
 * interface to minimize the effort required to implement this interface
 * for the NoSQL data source connector.  For a SQL data source connector,
 * {@link SqlConnector} should be used. For a REST-based connector,
 * {@link RestConnector} should be used.
 *
 * <p>To implement a NoSQL connector, the programmer needs only to extend
 * this class and provide implementations for the {@link #validateConnection(ConfigDto)},
 * {@link #execute(ConfigDto, QueryInfoDto)}, {@link #execute(ConfigDto, List)},
 * {@link #getName()} and {@link #getConfigClass()} methods.
 *
 * <p>The documentation for each non-abstract method in this class describes its
 * implementation in detail.  Each of these methods may be overridden if the
 * Connector being implemented admits a more efficient implementation.
 *
 * @author Adobe
 * @since 1.0.0
 */
public abstract class NoSqlConnector implements Connector {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthor() {
        return ADOBE_SYSTEMS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGroup() {
        return NOSQL_DB;
    }
}
