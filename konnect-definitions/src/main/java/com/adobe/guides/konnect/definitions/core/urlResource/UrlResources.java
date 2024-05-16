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

import com.adobe.guides.konnect.definitions.core.exception.KonnectException;

import java.util.List;

/**
 * An interface for URL Resources of a config.
 * <p>
 * The user of this interface has control over what is the
 * list of resources inside a config.
 * It also has control over which resource is the current resource
 * selected during a query execution.
 *
 * @author Adobe
 * @since 1.0.0
 */
public interface UrlResources {

    /**
     * Returns the list of resources.
     *
     * @return a list of {@link RestResourceDao}
     */
    List<RestResourceDao> getResourceList();

    /**
     * Sets the list of resources.
     *
     * @param resourceList a list of {@link RestResourceDao}
     * @throws KonnectException if the resource list is null.
     */
    void setResourceList(List<RestResourceDao> resourceList) throws KonnectException;

    /**
     * Sets the current resource of the config.
     *
     * @param id the id of the resource to be set as current.
     * @throws KonnectException if the resource id is not found in the resource list.
     */
    void setCurrentResource(String id) throws KonnectException;
}
