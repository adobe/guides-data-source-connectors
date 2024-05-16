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

import java.util.List;

/**
 * An interface for Additional Resources of a connector.
 * <p>
 * The user of this interface has control over what is the
 * default list of resources which are shipped with a connector
 * It also defines if a connector allows more resources to be added.
 *
 * @author Adobe
 * @since 1.0.0
 */
public interface AdditionalUrlResources {

    /**
     * Returns the default list of resources which are shipped with a connector.
     *
     * @return a {@code List} of {@link RestResourceDao} objects.
     */
    List<RestResourceDao> getDefaultUrlList();

    /**
     * Returns if more resources can be added to the connector.
     *
     * @return a {@code Boolean} which is {@code true} if more resources can be added.
     */
    Boolean areMoreResourceUrlAllowed();
}
