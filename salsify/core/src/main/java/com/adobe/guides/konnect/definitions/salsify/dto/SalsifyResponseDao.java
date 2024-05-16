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

import java.util.List;

/**
 * This class provides the response object for the Salsify connector.
 * <p>
 * This class is used to store the data and metadata of
 * the Salsify API response.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class SalsifyResponseDao {

    private List<Object> data;
    private SalsifyMetadataDao meta;

    /**
     * Sole constructor
     */
    public SalsifyResponseDao() {
    }

    /**
     * Returns the data of the Salsify API response.
     *
     * @return {@code List} which is the data of the Salsify
     * API response.
     */
    public List<Object> getData() {
        return data;
    }

    /**
     * Sets the data of the Salsify API response.
     *
     * @param data the data of the Salsify API response
     */
    public void setData(List<Object> data) {
        this.data = data;
    }

    /**
     * Returns the metadata of the Salsify API response.
     *
     * @return {@link SalsifyMetadataDao} which is the metadata of the Salsify
     * API response.
     */
    public SalsifyMetadataDao getMetadata() {
        return meta;
    }

    /**
     * Sets the metadata of the Salsify API response.
     *
     * @param metadata the metadata of the Salsify API response
     */
    public void setMetadata(SalsifyMetadataDao metadata) {
        this.meta = metadata;
    }
}
