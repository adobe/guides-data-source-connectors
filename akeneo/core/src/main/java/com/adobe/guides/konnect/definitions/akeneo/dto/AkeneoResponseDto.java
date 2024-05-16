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
 * This class provides the response object for the Akeneo connector. <p>
 * It is used to store the embedded items, current page and links.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class AkeneoResponseDto {

    private AkeneoEmebeddedItems _embedded;
    private Integer current_page;
    private AkeneoLinksDto _links;

    /**
     * Sole constructor
     */
    public AkeneoResponseDto() {
    }

    /**
     * Returns embedded items.
     *
     * @return {@link AkeneoEmebeddedItems} object.
     */
    public AkeneoEmebeddedItems getEmbedded() {
        return _embedded;
    }

    /**
     * Sets the list of items.
     *
     * @param _embedded a list of items.
     * @return {@link AkeneoResponseDto} object.
     */
    public AkeneoResponseDto setEmbedded(AkeneoEmebeddedItems _embedded) {
        this._embedded = _embedded;
        return this;
    }

    /**
     * Returns the current page.
     *
     * @return the current page.
     */
    public Integer getCurrentPage() {
        return current_page;
    }

    /**
     * Sets the current page.
     *
     * @param current_page the current page.
     * @return {@link AkeneoResponseDto} object.
     */
    public AkeneoResponseDto setCurrentPage(Integer current_page) {
        this.current_page = current_page;
        return this;
    }

    /**
     * Returns the links.
     *
     * @return {@link AkeneoLinksDto} which contains the links.
     */
    public AkeneoLinksDto getLinks() {
        return _links;
    }

    /**
     * Sets the links.
     *
     * @param _links {@link AkeneoLinksDto} which contains the links.
     * @return {@link AkeneoResponseDto} object.
     */
    public AkeneoResponseDto setLinks(AkeneoLinksDto _links) {
        this._links = _links;
        return this;
    }
}
