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
 * Additional query details interface.  The user of this interface
 * can provide additional details that are required for query
 * execution. These details need to be query-specific and not
 * connector-specific.
 * <p>
 * The <tt>Connector</tt> interface provides the method <tt>getInfoMap</tt>
 * which gives a map for the additional query information. This map
 * can be accessed in {@code Connector} to execute a query and
 * validate a connection.<p>
 *
 * @author Adobe
 * @since 1.0.0
 */
public interface AdditionalQueryInfo {

    /**
     * Returns the map of additional query info. This map will be used
     * to execute a query and validate a connection.
     *
     * @return a {@code Map} which is the map of additional query fields
     * and their values.
     */
    Map<String, String> getInfoMap();
}
