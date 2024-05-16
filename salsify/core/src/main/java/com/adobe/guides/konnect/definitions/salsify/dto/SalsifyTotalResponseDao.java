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

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides the total response object for the Salsify
 * connector.
 * <p>
 * This class is used to store the data and total records.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class SalsifyTotalResponseDao {

    private List<Object> data;
    private int totalRecords;

    /**
     * Sole constructor
     */
    public SalsifyTotalResponseDao() {
        data = new ArrayList<>();
    }

    /**
     * Returns a {@code List} of data objects.
     *
     * @return {@code List} of data objects.
     */
    public List<Object> getData() {
        return data;
    }

    /**
     * Sets the data objects.
     *
     * @param data a {@code List} of data objects.
     */
    public void setData(List<Object> data) {
        this.data = data;
    }

    /**
     * Returns the total number of records.
     *
     * @return the total number of records.
     */
    public int getTotalRecords() {
        return totalRecords;
    }

    /**
     * Sets the total number of records.
     *
     * @param totalRecords the total number of records.
     */
    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
}
