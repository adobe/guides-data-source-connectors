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

/**
 * Enum for Resource Type
 *
 * @author Adobe
 * @since 1.0.0
 */
public enum ResourceEnum {

    /**
     * Work Items by ID
     */
    BY_ID("Work Items by ID"),

    /**
     * Work Items by Wiql Query Id
     */
    BY_QUERY_ID("Work Items by Wiql Query Id"),

    /**
     * Work Items by Wiql Query
     */
    BY_QUERY("Work Items by Wiql Query");
    private String value;

    ResourceEnum(String value) {
        this.value = value;
    }

    /**
     * Returns the value of the enum.
     *
     * @return the {@code String} value of the enum
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    /**
     * Returns the enum value for the given {@code String}. <p>
     * If the value is not found, an {@code IllegalArgumentException} is thrown.
     *
     * @param value the {@code String} value of the enum.
     * @return the {@code ResourceEnum} value of the enum.
     */
    public static ResourceEnum getEnum(String value) {
        for (ResourceEnum v : values()) {
            if (v.getValue().equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
