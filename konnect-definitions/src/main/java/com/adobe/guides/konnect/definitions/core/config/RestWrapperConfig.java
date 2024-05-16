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
import com.adobe.guides.konnect.definitions.core.urlResource.RestResourceDao;
import com.adobe.guides.konnect.definitions.core.urlResource.UrlResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_FALSE;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_IGNORE;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_INCLUDE_SUBTYPE;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_LABEL;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_REQUIRED;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_TRUE;
import static com.adobe.guides.konnect.definitions.core.constants.LiteralConstants.URL_RESOURCE_MAPPING_INFO;

/**
 * This class provides a skeletal implementation of the {@link Config}
 * and {@link UrlResources} interface to minimize the effort required to
 * implement this interface for the REST-based config.  For a SQL data
 * source config, {@link SqlConfig} should be used. For a NoSQL data
 * source config, {@link NoSqlConfig} should be used.
 *
 * <p>To implement a RestWrapperConfig config, the programmer needs only to extend
 * this class and provide implementations for the {@link #getName()} method.
 *
 * <p>The documentation for each non-abstract method in this class describes its
 * implementation in detail.  Each of these methods may be overridden if the
 * Config being implemented admits a more efficient implementation.
 *
 * @author Adobe
 * @since 1.0.0
 */
public abstract class RestWrapperConfig implements Config, UrlResources {

    private static final Logger log = LoggerFactory.getLogger(RestWrapperConfig.class);

    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_IGNORE, value = ANNOTATION_TRUE)
    })
    private RestResourceDao currentResource;

    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Url resource list"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_FALSE),
            @APIDefinition(name = ANNOTATION_INFO, value = URL_RESOURCE_MAPPING_INFO),
            @APIDefinition(name = ANNOTATION_INCLUDE_SUBTYPE, value = ANNOTATION_TRUE),
    })
    private List<RestResourceDao> urlResourceList;

    /**
     * Sole constructor.
     * Sets the fields of this config.
     */
    public RestWrapperConfig() {
        this.urlResourceList = new ArrayList<>();
        this.currentResource = null;
    }

    /**
     * Constructs a new RestWrapperConfig with the given list of RestResourceDaos.
     *
     * @param restResourceDaos the list of RestResourceDaos.
     */
    public RestWrapperConfig(List<RestResourceDao> restResourceDaos) {
        this.urlResourceList = restResourceDaos;
        this.currentResource = null;
    }

    /**
     * Returns the name of the config.
     *
     * @return the name of the config.
     */
    @Override
    public List<RestResourceDao> getResourceList() {
        return this.urlResourceList;
    }

    /**
     * Returns the current resource.
     *
     * @return the current resource.
     */
    public RestResourceDao getCurrentResource() {
        return currentResource;
    }

    /**
     * Sets the list of RestResourceDaos.
     *
     * @param urlResourceList the list of RestResourceDaos.
     */
    public void setUrlResourceList(List<RestResourceDao> urlResourceList) {
        this.urlResourceList = urlResourceList;
    }

    /**
     * Sets the current resource.
     *
     * @param currentResource the current resource.
     */
    public void setCurrentResource(RestResourceDao currentResource) {
        this.currentResource = currentResource;
    }
}
