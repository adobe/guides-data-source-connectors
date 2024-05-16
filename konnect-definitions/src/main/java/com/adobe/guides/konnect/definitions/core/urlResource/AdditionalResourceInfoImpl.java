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

import com.adobe.guides.konnect.definitions.core.query.AdditionalResourceInfo;

import java.util.Map;

/**
 * This class is an implementation of the AdditionalResourceInfo
 * interface.
 * <p>
 * It is used to store additional information about a resource.
 * This information is stored in a map.
 * It overrides the getResourceInfoMap method to return the map.
 *
 * @author Adobe
 * @see AdditionalResourceInfo
 * @since 1.0.0
 */
public class AdditionalResourceInfoImpl implements AdditionalResourceInfo {

    private Map<String, String> resourceMap;

    /**
     * Sole constructor
     *
     * @param resourceMap a {@code Map} of resource information
     */
    public AdditionalResourceInfoImpl(Map<String, String> resourceMap) {
        this.resourceMap = resourceMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getResourceInfoMap() {
        return this.resourceMap;
    }
}
