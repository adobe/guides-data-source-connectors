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
package com.adobe.guides.konnect.definitions.core.urlResource;

import com.adobe.guides.konnect.definitions.core.annotations.APIDefinition;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_ENUM;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_FALSE;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_IGNORE;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_LABEL;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_REQUIRED;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_TRUE;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_TYPE;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_VALUES;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REQUEST_TYPE_ENUM;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_ID;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_NAME;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_REQUEST_BODY;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_REQUEST_HEADERS;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_REQUEST_TYPE;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_RESOURCE_IS_DEFAULT;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_RESOURCE_IS_ENABLED;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_SAMPLE_QUERY;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_URL;
import static com.adobe.guides.konnect.definitions.core.constants.LiteralConstants.HEADERS_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.LiteralConstants.REQUEST_BODY_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.LiteralConstants.REQUEST_TYPE_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.LiteralConstants.RESOURCE_URL;
import static com.adobe.guides.konnect.definitions.core.constants.LiteralConstants.SAMPLE_QUERY;
import static com.adobe.guides.konnect.definitions.core.constants.LiteralConstants.URL_RESOURCE_NAME;

/**
 * This class represents a REST resource.
 * <p>
 * It contains the id, name, URL, request type, body, headers,
 * sample query, and whether the resource is default or enabled.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class RestResourceDao {

    @SerializedName(REST_CONFIG_ID)
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_IGNORE, value = ANNOTATION_TRUE)
    })
    private String id;


    @SerializedName(REST_CONFIG_NAME)
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Name"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_TRUE),
            @APIDefinition(name = ANNOTATION_INFO, value = URL_RESOURCE_NAME),
    })
    private String name;

    @SerializedName(REST_CONFIG_URL)
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "URL"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_TRUE),
            @APIDefinition(name = ANNOTATION_INFO, value = RESOURCE_URL),
    })
    private String url;

    @SerializedName(REST_CONFIG_REQUEST_TYPE)
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "HTTP method"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_FALSE),
            @APIDefinition(name = ANNOTATION_INFO, value = REQUEST_TYPE_INFO),
            @APIDefinition(name = ANNOTATION_TYPE, value = ANNOTATION_ENUM),
            @APIDefinition(name = ANNOTATION_VALUES, value = REQUEST_TYPE_ENUM),
    })
    private String requestType;

    @SerializedName(REST_CONFIG_REQUEST_BODY)
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Body"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_FALSE),
            @APIDefinition(name = ANNOTATION_INFO, value = REQUEST_BODY_INFO),
    })
    private String body;

    @SerializedName(REST_CONFIG_RESOURCE_IS_DEFAULT)
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_IGNORE, value = ANNOTATION_TRUE)
    })
    private boolean isDefault;

    @SerializedName(REST_CONFIG_RESOURCE_IS_ENABLED)
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_IGNORE, value = ANNOTATION_TRUE)
    })
    private boolean isEnabled;

    @SerializedName(REST_CONFIG_REQUEST_HEADERS)
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Headers"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_FALSE),
            @APIDefinition(name = ANNOTATION_INFO, value = HEADERS_INFO),
    })
    private Map<String, String> headers;

    @SerializedName(REST_CONFIG_SAMPLE_QUERY)
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Sample query"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_FALSE),
            @APIDefinition(name = ANNOTATION_INFO, value = SAMPLE_QUERY),
    })
    private String sampleQuery;

    /**
     * Default constructor.
     */
    public RestResourceDao() {
    }

    /**
     * Set the id of the resource.
     *
     * @param id the id of the resource
     * @return {@link RestResourceDao} which is the resource with the id set.
     */
    public RestResourceDao setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Returns the id of the resource.
     *
     * @return a {@code String} which is the id of the resource.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the resource.
     *
     * @return a {@code String} which is the name of the resource.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the resource.
     *
     * @param name the name of the resource
     * @return {@link RestResourceDao} which is the resource with the name set.
     */
    public RestResourceDao setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Returns the URL of the resource.
     *
     * @return a {@code String} which is the URL of the resource.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the URL of the resource.
     *
     * @param url the URL of the resource
     * @return {@link RestResourceDao} which is the resource with the URL set.
     */
    public RestResourceDao setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Returns the request type of the resource.
     *
     * @return a {@code String} which is the request type of the resource.
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Set the request type of the resource.
     *
     * @param requestType the request type of the resource
     * @return {@link RestResourceDao} which is the resource with the request type set.
     */
    public RestResourceDao setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    /**
     * Returns the body of the resource.
     *
     * @return a {@code String} which is the body of the resource.
     */
    public String getBody() {
        return body;
    }

    /**
     * Set the body of the resource.
     *
     * @param body the body of the resource
     * @return {@link RestResourceDao} which is the resource with the body set.
     */
    public RestResourceDao setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * Returns the headers of the resource.
     *
     * @return a {@code Map} of {@code String} which is the headers of the resource.
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Set the headers of the resource.
     *
     * @param headers the headers of the resource
     * @return {@link RestResourceDao} which is the resource with the headers set.
     */
    public RestResourceDao setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * Returns if the resource is default.
     *
     * @return a {@code boolean} which is {@code true} if the resource is default.
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * Set if the resource is default.
     *
     * @param aDefault {@code true} if the resource is default
     * @return {@link RestResourceDao} which is the resource with the isDefault
     * value set.
     */
    public RestResourceDao setDefault(boolean aDefault) {
        isDefault = aDefault;
        return this;
    }

    /**
     * Returns if the resource is enabled.
     *
     * @return a {@code boolean} which is {@code true} if the resource is enabled.
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Set if the resource is enabled.
     *
     * @param enabled {@code true} if the resource is enabled
     * @return {@link RestResourceDao} which is the resource with the isEnabled
     * value set.
     */
    public RestResourceDao setEnabled(boolean enabled) {
        isEnabled = enabled;
        return this;
    }

    /**
     * Returns the sample query of the resource.
     *
     * @return a {@code String} which is the sample query of the resource.
     */
    public String getSampleQuery() {
        return sampleQuery;
    }

    /**
     * Set the sample query of the resource.
     *
     * @param sampleQuery the sample query of the resource
     * @return {@link RestResourceDao} which is the resource with the sample query set.
     */
    public RestResourceDao setSampleQuery(String sampleQuery) {
        this.sampleQuery = sampleQuery;
        return this;
    }
}
