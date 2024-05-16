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
package com.adobe.guides.konnect.definitions.core.models.graphql;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

/**
 * This class represents a GraphQL request.
 * <p>
 * It contains the query, operation name, and variables.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class GraphQLRequest {
    protected String query;
    protected String operationName;
    protected Object variables;
    private Integer hash;

    /**
     * Constructor for the GraphQL request.
     *
     * @param query {@code String} which is the query string.
     */
    public GraphQLRequest(String query) {
        this.query = query;
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
     * Sets the query string.
     *
     * @param query a {@code String} which is the query string.
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Returns the operation name.
     *
     * @return a {@code String} which is the operation name.
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * Sets the operation name.
     *
     * @param operationName a {@code String} which is the operation name.
     */
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
     * Returns the variables.
     *
     * @return an {@code Object} which represents the variables.
     */
    public Object getVariables() {
        return variables;
    }

    /**
     * Sets the variables.
     *
     * @param variables an {@code Object} which represents the variables.
     */
    public void setVariables(Object variables) {
        this.variables = variables;
    }

    /**
     * Returns if the current object and the given object are equal.
     *
     * @param o the object to compare
     * @return a {@code boolean} which is {@code true} if the objects
     * are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GraphQLRequest that = (GraphQLRequest) o;
        if (!StringUtils.equals(query, that.query)) {
            return false;
        }
        if (!StringUtils.equals(operationName, that.operationName)) {
            return false;
        }
        return Objects.equals(variables, that.variables);
    }

    /**
     * Returns the hash code for this object.
     *
     * @return an {@code int} which is the hash code.
     */
    @Override
    public int hashCode() {
        if (hash != null) {
            return hash.intValue();
        }
        hash = new HashCodeBuilder()
                .append(query)
                .append(operationName)
                .append(variables)
                .toHashCode();
        return hash.intValue();
    }
}
