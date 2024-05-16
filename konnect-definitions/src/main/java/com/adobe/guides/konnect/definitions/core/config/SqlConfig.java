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

import com.adobe.guides.konnect.definitions.core.annotations.APIDefinition;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_DEFAULT;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_LABEL;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_PLACEHOLDER;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_REQUIRED;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_SQL_AUTHENTICATION;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_TRUE;
import static com.adobe.guides.konnect.definitions.core.constants.LiteralConstants.DRIVER_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.LiteralConstants.SQL_URL_INFO;

/**
 * This class provides a skeletal implementation of the {@link Config}
 * interface to minimize the effort required to implement this interface
 * for the SQL data source config.  For a REST-based config, {@link RestConfig}
 * should be used. For a NoSQL data source config, {@link NoSqlConfig}
 * should be used.
 *
 * <p>To implement a SQL config, the programmer needs only to extend
 * this class and provide implementations for the {@link #getName()}
 * method.
 *
 * <p>The documentation for each non-abstract method in this class describes its
 * implementation in detail.  Each of these methods may be overridden if the
 * Config being implemented admits a more efficient implementation.
 *
 * @author Adobe
 * @since 1.0.0
 */
@APIDefinition(name = ANNOTATION_LABEL, value = ANNOTATION_SQL_AUTHENTICATION)
public abstract class SqlConfig implements Config {

    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Driver"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_TRUE),
            @APIDefinition(name = ANNOTATION_DEFAULT, value = ""),
            @APIDefinition(name = ANNOTATION_INFO, value = DRIVER_INFO),
    })
    private String driver;


    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "URL"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_TRUE),
            @APIDefinition(name = ANNOTATION_PLACEHOLDER, value = ""),
            @APIDefinition(name = ANNOTATION_INFO, value = SQL_URL_INFO),
    })
    private String connectionString;

    /**
     * Sole constructor.
     * Sets the fields of this config.
     *
     * @param driver           the driver of this config
     * @param connectionString the connection string of this config
     */
    public SqlConfig(String driver, String connectionString) {
        this.driver = driver;
        this.connectionString = connectionString;
    }

    /**
     * Returns a {@code String} which is the driver of this config
     *
     * @return {@code String} which is the driver of this config
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Returns a {@code String} which is the connection string of this config
     *
     * @return {@code String} which is the connection string of this config
     */
    public String getConnectionString() {
        return connectionString;
    }
}
