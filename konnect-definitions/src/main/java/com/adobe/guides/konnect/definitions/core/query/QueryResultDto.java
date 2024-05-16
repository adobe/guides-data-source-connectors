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
package com.adobe.guides.konnect.definitions.core.query;

/**
 * <tt>QueryResultDto</tt> is the object that is returned after a query
 * is executed.
 * <p>
 * It contains the query that was executed and the response
 * that was received.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class QueryResultDto {
    private String query;
    private String response;

    /**
     * Returns the query string.
     *
     * @return a {@code String} which is the query string.
     */
    public String getQuery() {
        return query;
    }

    /**
     * Set the query string.
     *
     * @param query a {@code String} which is the query string.
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Returns the response string.
     *
     * @return a {@code String} which is the response string.
     */
    public String getResponse() {
        return response;
    }

    /**
     * Set the response string.
     *
     * @param response a {@code String} which is the response string.
     */
    public void setResponse(String response) {
        this.response = response;
    }
}
