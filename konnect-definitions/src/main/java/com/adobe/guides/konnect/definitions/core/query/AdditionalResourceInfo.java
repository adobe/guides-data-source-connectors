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
package com.adobe.guides.konnect.definitions.core.query;

import java.util.Map;

/**
 * Additional resource interface.  The user of this interface
 * can provide additional details that are required for query
 * execution which needs a resource. These details need to be
 * resource-specific and not connector-specific.
 * <p>
 *
 * @author Adobe
 * @since 1.0.0
 */
public interface AdditionalResourceInfo {

    /**
     * Returns the map of additional resource info. This map will be used
     * to execute a query against a particular resource.
     *
     * @return a {@code Map} which is the map of additional resources and
     * its details
     */
    Map<String, String> getResourceInfoMap();
}
