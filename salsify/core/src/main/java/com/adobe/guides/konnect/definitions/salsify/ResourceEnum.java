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
package com.adobe.guides.konnect.definitions.salsify;

import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_DEFAULT_QUERY;

/**
 * Enum for Resource Type
 *
 * @author Adobe
 * @since 1.0.0
 */
public enum ResourceEnum {

    /**
     * Get list of products
     */
    GET_ALL_PRODUCTS("Get list of products ", "/products", SALSIFY_DEFAULT_QUERY),

    /**
     * Get list of records
     */
    GET_ALL_RECORDS("Get list of records", "/records", SALSIFY_DEFAULT_QUERY),

    /**
     * Get digital assets
     */
    GET_ALL_ATTRIBUTES("Get digital assets", "/digital_assets", SALSIFY_DEFAULT_QUERY);
    private String stringValue;
    private String url;
    private String sampleQuery;

    ResourceEnum(String value, String url, String sampleQuery) {
        this.stringValue = value;
        this.url = url;
        this.sampleQuery = sampleQuery;
    }

    /**
     * Returns the value of the enum.
     *
     * @return the {@code String} value of the enum
     */
    public String getValue() {
        return stringValue;
    }

    /**
     * Returns the sample query of the enum.
     *
     * @return the {@code String} sample query of the enum
     */
    public String getSampleQuery() {
        return sampleQuery;
    }

    /**
     * Returns the URL of the enum.
     *
     * @return the {@code String} URL of the enum
     */
    public String getUrl() {
        return url.replaceAll("\\{code\\}", "").replaceAll("\\{id\\}", "");
    }

    /**
     * Returns the enum value for the given URL. <p>
     * If the URL is not found, an {@code IllegalArgumentException} is thrown.
     *
     * @param value the {@code String} URL of the enum.
     * @return the {@code ResourceEnum} value of the enum.
     */
    public static ResourceEnum getEnumByUrl(String value) {
        for (ResourceEnum v : values()) {
            if (v.getUrl().equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns the enum value for the given {@code String}. <p>
     * If the value is not found, an {@code IllegalArgumentException} is thrown.
     *
     * @param value the {@code String} value of the enum.
     * @return the {@code ResourceEnum} value of the enum.
     */
    public static ResourceEnum getEnumByValue(String value) {
        for (ResourceEnum v : values()) {
            if (v.getValue().equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
