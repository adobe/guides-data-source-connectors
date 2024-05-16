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

import java.util.List;

/**
 * This class represents a GraphQL response.
 * <p>
 * It contains the data and errors.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class GraphQLResponse {

    protected Object data;
    protected List<Object> errors;

    /**
     * Returns the data.
     *
     * @return {@code Object} which is the data.
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data {@code Object} which is the data.
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Returns the errors.
     *
     * @return {@code List} of errors.
     */
    public List<Object> getErrors() {
        return errors;
    }

    /**
     * Sets the errors.
     *
     * @param errors {@code List} of errors.
     */
    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }
}
