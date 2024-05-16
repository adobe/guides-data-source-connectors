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
package com.adobe.guides.konnect.definitions.akeneo.utils;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.AKENEO_PAGE_LIMIT_PARAM;

/**
 * Utility class for the Akeneo Connector.<p>
 * This class provides utility methods for the Akeneo connector.
 *
 * @author Adobe
 * @since 1.0.0
 */
@Component(service = AkeneoUtils.class)
public class AkeneoUtils {

    @Reference
    private URLUtility urlUtilityClass;
    private static final Logger log = LoggerFactory.getLogger(AkeneoUtils.class);

    /**
     * Returns the query with whitespaces removed.
     *
     * @param query the query string
     * @return the query string without whitespaces
     */
    public String removeSingleHashQuery(String query) {
        if (query.trim().toLowerCase().equals("/"))
            return "";
        return query.trim();
    }

    /**
     * Replaces the limit parameter with the default limit.
     *
     * @param query the query string
     * @param maxNoRowsForPreviewQuery the maximum number of rows for the preview query
     * @return the query string with the limit parameter replaced with the default limit
     */
    public String replaceLimitWithDefaultLimit(String query, Integer maxNoRowsForPreviewQuery) {
        query = removeSingleHashQuery(query);
        if (urlUtilityClass.doesQueryHaveQueryParameter(query, AKENEO_PAGE_LIMIT_PARAM)) {
            query = urlUtilityClass.replaceQueryParamWithValue(query, AKENEO_PAGE_LIMIT_PARAM, String.valueOf(maxNoRowsForPreviewQuery));
        } else {
            query = updateQuery(query, AKENEO_PAGE_LIMIT_PARAM, String.valueOf(maxNoRowsForPreviewQuery));
        }

        return query;
    }

    /**
     * Updates the query with the given key and value.
     *
     * @param query the query string
     * @param key the key to update
     * @param value the value to update
     * @return the updated query string
     */
    public String updateQuery(String query, String key, String value) {
        Map<String, String> allQueryParams = urlUtilityClass.getQueryParams(query);
        if (StringUtils.isNotBlank(key)) {
            String replacementString = key + "=" + value;
            allQueryParams.put(key, replacementString);
        }
        String builtQuery = urlUtilityClass.buildQuery(allQueryParams);
        if (StringUtils.isNotBlank(builtQuery)) {
            return builtQuery;
        }
        return query;

    }

}
