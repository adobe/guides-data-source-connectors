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

import java.util.HashMap;
import java.util.Map;

/**
 * <tt>QueryInfoDto</tt> is the object that is passed to execute a query.
 * <p>
 * The field <tt>query</tt> is required to be set so that it can be executed.
 * Other information that can be provided is <tt>queryName</tt> which will
 * be used during executions of multiple queries.
 * <tt>AdditionalQueryInfo</tt> can be provided as part of the query info when
 * additional parameters are needed for successful query execution.<p>
 *
 * @author Adobe
 * @since 1.0.0
 */
public class QueryInfoDto {
    private String query;
    private String queryName;
    private AdditionalQueryInfo additionalQueryInfo;
    private AdditionalResourceInfo additionalResourceInfo;

    /**
     * Set the query string.
     *
     * @param query a {@code String} which is the query string.
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Set the query name.
     *
     * @param queryName a {@code String} which is the query name.
     */
    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    /**
     * Set the additional query information.
     *
     * @param additionalQueryInfo an {@code AdditionalQueryInfo} object
     *                            which contains additional query information.
     */
    public void setAdditionalQueryInfo(AdditionalQueryInfo additionalQueryInfo) {
        this.additionalQueryInfo = additionalQueryInfo;
    }

    /**
     * Returns the query string.
     *
     * @return a {@code String} which is the query string.
     */
    public String getQuery() {
        return query;
    }

    /**
     * Returns the query name.
     *
     * @return a {@code String} which is the query name.
     */
    public String getQueryName() {
        return queryName;
    }

    /**
     * Returns the additional query information.
     *
     * @return an {@code AdditionalQueryInfo} object which contains
     * additional query information.
     */
    public Map<String, String> getAdditionalQueryInfo() {
        if (additionalQueryInfo != null) {
            return additionalQueryInfo.getInfoMap();
        }
        return new HashMap<>();
    }

    /**
     * Returns the additional resource information.
     *
     * @return a {@code Map} which is the map of additional resource fields
     * and their values.
     */
    public Map<String, String> getAdditionalResourceInfo() {
        if (additionalResourceInfo != null) {
            return additionalResourceInfo.getResourceInfoMap();
        }
        return new HashMap<>();
    }

    /**
     * Set the additional resource information.
     *
     * @param additionalResourceInfo an {@code AdditionalResourceInfo} object
     *                               which contains additional resource information.
     */
    public void setAdditionalResourceInfo(AdditionalResourceInfo additionalResourceInfo) {
        this.additionalResourceInfo = additionalResourceInfo;
    }
}
