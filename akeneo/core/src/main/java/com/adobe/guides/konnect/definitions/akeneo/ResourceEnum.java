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
package com.adobe.guides.konnect.definitions.akeneo;

import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.AKENEO_DEFAULT_QUERY;

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
    GET_ALL_PRODUCTS("Get list of products ", "/api/rest/v1/products", AKENEO_DEFAULT_QUERY),

    /**
     * Get a product
     */
    GET_PRODUCT_BY_ID("Get a product", "/api/rest/v1/products/{code}", "10667767"),

    /**
     * Get list of products (UUID)
     */
    GET_ALL_PRODUCTS_UUID("Get list of products (UUID)", "/api/rest/v1/products-uuid", AKENEO_DEFAULT_QUERY),

    /**
     * Get a product (UUID)
     */
    GET_A_PRODUCT_UUID("Get a product (UUID)", "/api/rest/v1/products-uuid/{code}", "25566245-55c3-42ce-86d9-8610ac459fa8"),

    /**
     * Get list of families
     */
    GET_ALL_FAMILIES("Get list of families", "/api/rest/v1/families", "search={\"code\":[{\"operator\":\"IN\",\"value\":[\"family_code1\",\"family_code2\"]}]}"),

    /**
     * Get list of attributes
     */
    GET_ALL_ATTRIBUTES("Get list of attributes", "/api/rest/v1/attributes", "search={\"code\":[{\"operator\":\"IN\",\"value\":[\"code1\",\"code2\"]}]}"),

    /**
     * Get list of categories
     */
    GET_LIST_CATEGORIES("Get list of categories", "/api/rest/v1/categories", "search={\"code\":[{\"operator\":\"IN\",\"value\":[\"category_code1\",\"category_code2\"]}]}"),

    /**
     * Get list of locales
     */
    GET_LIST_LOCALES("Get list of locales", "/api/rest/v1/locales", "search={\"enabled\":[{\"operator\":\"=\",\"value\":true}]}"),

    /**
     * Get system info
     */
    GET_SYSTEM_INFO("Get system info", "/api/rest/v1/system-information", ""),

    /**
     * Refresh auth token
     */
    GET_OAUTH_TOKEN("Refresh auth token", "/api/oauth/v1/token", "");
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
