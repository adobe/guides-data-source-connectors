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

import java.util.Map;

/**
 * The authentication details for a REST configuration. This class provides
 * the header and query maps used to send a REST request.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class AuthenticationDetails {

    private Map<String, String> header;
    private Map<String, String> query;

    /**
     * Returns a {@code Map} which is a map of header names
     * and header values to be used in REST request
     *
     * @return {@code Map} which is a map of header names
     * and header values
     */
    public Map<String, String> getHeader() {
        return header;
    }

    /**
     * Sets a map with header names and header values
     *
     * @param header a map with header names and header values
     */
    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    /**
     * Returns a {@code Map} which is a map of query field names
     * and query values to be used in REST request
     *
     * @return {@code Map} which is a map of query field names
     * and query values
     */
    public Map<String, String> getQuery() {
        return query;
    }

    /**
     * Sets a map with query field names and query values
     *
     * @param query a map with query field names and query values
     */
    public void setQuery(Map<String, String> query) {
        this.query = query;
    }
}

