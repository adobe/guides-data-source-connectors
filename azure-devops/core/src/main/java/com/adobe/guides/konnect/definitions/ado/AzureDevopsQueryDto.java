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
package com.adobe.guides.konnect.definitions.ado;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class provides the query object for the Azure DevOps connector. <p>
 * It is used to store the project and query string.
 *
 * @author Adobe
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureDevopsQueryDto {
    private String project;
    private String query;

    /**
     * Returns the project name
     *
     * @return a {@code String} which is the project name
     */
    public String getProject() {
        return project;
    }

    /**
     * Returns the query string
     *
     * @return a {@code String} which is the query string
     */
    public String getQuery() {
        return query;
    }
}
