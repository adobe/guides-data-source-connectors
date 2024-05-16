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
package com.adobe.guides.konnect.definitions.salsify.dto;

import com.google.gson.annotations.SerializedName;

import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_FILTER_QUERY_PARAM;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_PAGE_QUERY_PARAM;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_PER_PAGE_QUERY_PARAM;

/**
 * Data Access Object for the Salsify API request.
 * <p>
 * This class is used to store the filter, page, and per_page
 * query parameters for the Salsify API request.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class SalsifyRequestDao {

    @SerializedName(SALSIFY_FILTER_QUERY_PARAM)
    private String filter;

    @SerializedName(SALSIFY_PAGE_QUERY_PARAM)
    private int page = -1;

    @SerializedName(SALSIFY_PER_PAGE_QUERY_PARAM)
    private int perPage = -1;

    /**
     * Sole constructor
     */
    public SalsifyRequestDao() {
    }

    /**
     * Returns the filter query parameter.
     *
     * @return the filter query parameter
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Sets the filter query parameter.
     *
     * @param filter the filter query parameter
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * Returns the page query parameter.
     *
     * @return the page query parameter
     */
    public int getPage() {
        return page;
    }

    /**
     * Sets the page query parameter.
     *
     * @param page the page query parameter
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Returns the per_page query parameter.
     *
     * @return the per_page query parameter
     */
    public int getPerPage() {
        return perPage;
    }

    /**
     * Sets the per_page query parameter.
     *
     * @param per_page the per_page query parameter
     */
    public void setPerPage(int per_page) {
        this.perPage = per_page;
    }

}
