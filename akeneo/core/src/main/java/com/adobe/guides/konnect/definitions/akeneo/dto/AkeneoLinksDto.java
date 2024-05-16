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
 * This class provides the links object for the Akeneo connector. <p>
 * It is used to store self link, first link and next link.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class AkeneoLinksDto {
    private SelfLink self;
    private FirstLink first;
    private NextLink next;

    /**
     * Constructor to initialize empty object.
     */
    public AkeneoLinksDto() {
    }

    /**
     * Returns a self link.
     *
     * @return {@link SelfLink} which is a self link.
     */
    public SelfLink getSelf() {
        return self;
    }

    /**
     * Sets the self link.
     *
     * @param self {@link SelfLink} which is a self link.
     * @return {@code AkeneoLinksDto} object.
     */
    public AkeneoLinksDto setSelf(SelfLink self) {
        this.self = self;
        return this;
    }

    /**
     * Returns a first link.
     *
     * @return {@link FirstLink} which is a first link.
     */
    public FirstLink getFirst() {
        return first;
    }

    /**
     * Sets the first link.
     *
     * @param first {@link FirstLink} which is a first link.
     * @return {@code AkeneoLinksDto} object.
     */
    public AkeneoLinksDto setFirst(FirstLink first) {
        this.first = first;
        return this;
    }

    /**
     * Returns a next link.
     *
     * @return {@link NextLink} which is a next link.
     */
    public NextLink getNext() {
        return next;
    }

    /**
     * Sets the next link.
     *
     * @param next {@link NextLink} which is a next link.
     * @return {@code AkeneoLinksDto} object.
     */
    public AkeneoLinksDto setNext(NextLink next) {
        this.next = next;
        return this;
    }
}

