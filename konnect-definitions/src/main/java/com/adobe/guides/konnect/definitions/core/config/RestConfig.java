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

import com.adobe.guides.konnect.definitions.core.annotations.APIDefinition;
import com.adobe.guides.konnect.definitions.core.exception.KonnectException;
import com.adobe.guides.konnect.definitions.core.urlResource.RestResourceDao;
import com.adobe.guides.konnect.definitions.core.util.RestResourceUtil;
import com.adobe.guides.konnect.definitions.core.util.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_ENUM;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_FALSE;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_LABEL;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_REQUIRED;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_TRUE;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_TYPE;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_VALUES;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REQUEST_TYPE_ENUM;
import static com.adobe.guides.konnect.definitions.core.constants.LiteralConstants.HEADERS_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.LiteralConstants.REQUEST_BODY_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.LiteralConstants.REQUEST_TYPE_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.LiteralConstants.URL_INFO;

/**
 * This class provides a skeletal implementation of the {@link RestWrapperConfig}
 * interface to minimize the effort required to implement this interface
 * for the REST-based config.  For a SQL data source config, {@link SqlConfig}
 * should be used. For a NoSQL data source config, {@link NoSqlConfig}
 * should be used.
 *
 * <p>To implement a REST config, the programmer needs only to extend
 * this class and provide implementations for the {@link #getName()},
 * and {@link #getAuthenticationDetails()} methods.
 *
 * <p>The documentation for each non-abstract method in this class describes its
 * implementation in detail.  Each of these methods may be overridden if the
 * Config being implemented admits a more efficient implementation.
 *
 * @author Adobe
 * @since 1.0.0
 */
public abstract class RestConfig extends RestWrapperConfig {

    private static final Logger log = LoggerFactory.getLogger(RestConfig.class);

    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "URL"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_TRUE),
            @APIDefinition(name = ANNOTATION_INFO, value = URL_INFO),
    })
    private String url;

    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "HTTP method"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_FALSE),
            @APIDefinition(name = ANNOTATION_INFO, value = REQUEST_TYPE_INFO),
            @APIDefinition(name = ANNOTATION_TYPE, value = ANNOTATION_ENUM),
            @APIDefinition(name = ANNOTATION_VALUES, value = REQUEST_TYPE_ENUM),
    })
    private String requestType;


    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Body"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_FALSE),
            @APIDefinition(name = ANNOTATION_INFO, value = REQUEST_BODY_INFO),
    })
    private String body;

    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Headers"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_FALSE),
            @APIDefinition(name = ANNOTATION_INFO, value = HEADERS_INFO),
    })
    private Map<String, String> headers;

    /**
     * Sole constructor.
     * Sets the fields of this config.
     *
     * @param url         {@code String} URL used to send the request
     * @param requestType {@code String} which is the type of request like GET or POST
     * @param body        {@code String} which is the body of request
     * @param headers     {@code Map} of the headers to be used in request
     */
    public RestConfig(String url, String requestType, String body, Map<String, String> headers) {
        super();
        this.url = url;
        this.requestType = requestType;
        this.body = body;
        this.headers = headers;
    }

    /**
     * Sole constructor.
     * Sets the fields of this config.
     *
     * @param url              {@code String} URL used to send the request
     * @param requestType      {@code String} which is the type of request like GET or POST
     * @param body             {@code String} which is the body of request
     * @param headers          {@code Map} of the headers to be used in request
     * @param restResourceDaos {@code List} of {@link RestResourceDao} which is the list of resources
     */
    public RestConfig(String url, String requestType, String body, Map<String, String> headers, List<RestResourceDao> restResourceDaos) {
        super(restResourceDaos);
        this.url = url;
        this.requestType = requestType;
        this.body = body;
        this.headers = headers;
    }

    /**
     * Returns a {@code String} URL used to send the request
     *
     * @return {@code String} URL used to send the request
     */
    public String getUrl() {
        return getUrl(false);
    }

    /**
     * Returns a {@code String} URL used to send the request
     *
     * @param isValidate {@code boolean} if true, then the base URL is returned.
     * @return {@code String} URL used to send the request
     */
    public String getUrl(boolean isValidate) {
        if (this.getCurrentResource() != null && isValidate != true) {
            return UrlUtils.getAbsoluteURLFromBaseAndRelativeUrl(this.url, this.getCurrentResource().getUrl(), this.url);
        }
        return this.url;
    }

    /**
     * Returns a {@code String} which is the type of request like GET or POST
     *
     * @return {@code String} which is the type of request
     */
    public String getRequestType() {
        return getRequestType(false);
    }

    /**
     * Returns a {@code String} which is the type of request like GET or POST
     *
     * @param isValidate {@code boolean} if true, then the base request type
     *                   is returned.
     * @return {@code String} which is the type of request
     */
    public String getRequestType(boolean isValidate) {
        if (this.getCurrentResource() != null && isValidate != true) {
            return this.getCurrentResource().getRequestType();
        }
        return this.requestType;
    }

    /**
     * Returns a {@code String} which is the body of request
     *
     * @return {@code String} which is the body of request
     */
    public String getBody() {
        return getBody(false);
    }

    /**
     * Returns a {@code String} which is the body of request
     *
     * @param isValidate {@code boolean} if true, then the base body
     *                   is returned.
     * @return {@code String} which is the body of request
     */
    public String getBody(boolean isValidate) {
        if (this.getCurrentResource() != null && isValidate != true) {
            return this.getCurrentResource().getBody();
        }
        return this.body;
    }

    /**
     * Returns a {@code Map} of the headers to be used in request.<p>
     * This returns a key-value pair where key is the header name
     * and value is the header value
     *
     * @return {@code Map} of the headers to be used in request
     */
    public Map<String, String> getHeaders() {
        return getHeaders(false);
    }

    /**
     * Returns a {@code Map} of the headers to be used in request.<p>
     * This returns a key-value pair where key is the header name
     * and value is the header value
     *
     * @param isValidate {@code boolean} if true, then the base headers
     *                   are returned.
     * @return {@code Map} of the headers to be used in request
     */
    public Map<String, String> getHeaders(boolean isValidate) {
        if (this.getCurrentResource() != null && isValidate != true) {
            return this.getCurrentResource().getHeaders();
        }
        return this.headers;
    }

    /**
     * Returns a {@link AuthenticationDetails} object to be used to execute a
     * query.
     *
     * @return {@link AuthenticationDetails} object to be used to execute a query
     */
    public abstract AuthenticationDetails getAuthenticationDetails();

    /**
     * Sets the resources to be used for this config.
     *
     * @param resourceList a {@code List} of {@link RestResourceDao} objects.
     *                     Each object represents a resource to be used.
     */
    @Override
    public void setResourceList(List<RestResourceDao> resourceList) throws KonnectException {
        this.setUrlResourceList(resourceList);
        try {
            this.setUrlResourceList(RestResourceUtil.validateDefaultArrayOfUrlList(this.getResourceList()));
        } catch (Exception e) {
            log.warn("Failed to validate url mapping ", e);
            throw new KonnectException("Failed to validate url mapping " + e.getMessage());
        }
    }

    /**
     * Sets the current resource to be used for this config.
     *
     * @param id a {@code String} which is the id of the resource to be used.
     */
    @Override
    public void setCurrentResource(String id) throws KonnectException {
        boolean found = false;
        boolean shouldthrowError = false;
        if (!this.getResourceList().isEmpty() && StringUtils.isNotBlank(id)) {
            shouldthrowError = true;
            for (int i = 0; i < this.getResourceList().size(); i++) {
                if (this.getResourceList().get(i).getId().equalsIgnoreCase(id)) {
                    this.setCurrentResource(this.getResourceList().get(i));
                    found = true;
                    break;
                }
            }
        }
        if (found == false) {
            this.setCurrentResource(new RestResourceDao());
            if (shouldthrowError == true) {
                throw new KonnectException("URL Resource selected for query is invalid");
            }
        }
    }
}
