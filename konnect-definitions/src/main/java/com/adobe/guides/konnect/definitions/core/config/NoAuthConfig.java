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

import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_LABEL;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.NO_AUTHENTICATION;

/**
 * This class provides a concrete implementation of the {@link Config}
 * interface to minimize the effort required to implement this interface
 * for a data source config with No Authentication.
 *
 * @author Adobe
 * @since 1.0.0
 */
@APIDefinition(name = ANNOTATION_LABEL, value = NO_AUTHENTICATION)
public class NoAuthConfig implements Config {

    /**
     * {@inheritDoc} Config#getName()
     */
    @Override
    public String getName() {
        return NO_AUTHENTICATION;
    }
}
