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
 * This class provides a skeletal implementation of the {@link Config}
 * interface to minimize the effort required to implement this interface
 * for the NoSQL data source config.  For a REST-based config, {@link RestConfig}
 * should be used. For a SQL data source config, {@link SqlConfig}
 * should be used.
 *
 * <p>To implement a NoSQL config, the programmer needs only to extend
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
public abstract class NoSqlConfig implements Config {
    
}
