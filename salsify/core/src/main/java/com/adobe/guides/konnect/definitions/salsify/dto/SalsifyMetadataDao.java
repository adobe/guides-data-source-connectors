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

import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_CURRENT_PAGE_QUERY_PARAM;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_PER_PAGE_QUERY_PARAM;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_TOTAL_ENTRIES_QUERY_PARAM;

/**
 * Data transfer object for the Salsify metadata.
 * This class is used to store the metadata information
 * of the Salsify API response.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class SalsifyMetadataDao {

    @SerializedName(SALSIFY_PER_PAGE_QUERY_PARAM)
    private int perPage;
    private String cursor;

    @SerializedName(SALSIFY_TOTAL_ENTRIES_QUERY_PARAM)
    private int totalEntries;

    @SerializedName(SALSIFY_CURRENT_PAGE_QUERY_PARAM)
    private int currentPage;

    private SalsifyMetadataDao() {

    }

    /**
     * Returns the number of entries per page.
     *
     * @return {@code int} the number of entries per page.
     */
    public int getPerPage() {
        return perPage;
    }

    /**
     * Sets the number of entries per page.
     *
     * @param perPage the number of entries per page.
     */
    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    /**
     * Returns the cursor.
     *
     * @return {@code String} the cursor.
     */
    public String getCursor() {
        return cursor;
    }

    /**
     * Sets the cursor.
     *
     * @param cursor the cursor.
     */
    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    /**
     * Returns the total number of entries.
     *
     * @return {@code int} the total number of entries.
     */
    public int getTotalEntries() {
        return totalEntries;
    }

    /**
     * Sets the total number of entries.
     *
     * @param totalEntries the total number of entries.
     */
    public void setTotalEntries(int totalEntries) {
        this.totalEntries = totalEntries;
    }

    /**
     * Returns the current page.
     *
     * @return {@code int} the current page.
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the current page.
     *
     * @param currentPage the current page.
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
