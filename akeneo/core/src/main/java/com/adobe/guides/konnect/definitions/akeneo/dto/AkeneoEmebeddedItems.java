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


import java.util.ArrayList;
import java.util.List;

/**
 * This class provides the embedded items object for the Akeneo connector. <p>
 * It is used to store the item list.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class AkeneoEmebeddedItems {

    private List<Object> items;

    /**
     * Sole constructor
     */
    public AkeneoEmebeddedItems() {
        items = new ArrayList<>();
    }

    /**
     * Returns a list of items.
     *
     * @return a list of items.
     */
    public List<Object> getItems() {
        return items;
    }

    /**
     * Sets the list of items.
     *
     * @param items a list of items.
     * @return {@code AkeneoEmebeddedItems} object.
     */
    public AkeneoEmebeddedItems setItems(List<Object> items) {
        this.items = items;
        return this;
    }
}


