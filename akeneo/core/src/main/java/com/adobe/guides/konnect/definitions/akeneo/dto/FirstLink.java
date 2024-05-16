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
package com.adobe.guides.konnect.definitions.akeneo.dto;

/**
 * This class provides the First Link for the Akeneo connector. <p>
 * It extends the {@link AkeneoHrefDto} class and is used to store the first link.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class FirstLink extends AkeneoHrefDto {

    /**
     * Constructor to initialize empty object.
     */
    public FirstLink() {
        super();
    }

    /**
     * Constructor to initialize the href string.
     */
    public FirstLink(String href) {
        super(href);
    }
}
