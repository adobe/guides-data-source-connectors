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
package com.adobe.guides.konnect.definitions.core.util;

import com.adobe.guides.konnect.definitions.core.urlResource.RestResourceDao;
import com.google.gson.JsonParseException;
import org.apache.commons.lang3.StringUtils;

import javax.jcr.ItemExistsException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.HTTP_METHOD_GET;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.FORWARD_SLASH;

/**
 * This class provides utility methods for the REST resource.
 * <p>
 * It provides methods to check and fix the REST resource DAO object,
 * validate the default array of URL list, and check if all resource names are unique.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class RestResourceUtil {

    /**
     * Checks and fixes the REST resource DAO object.
     * <p>
     * This method checks if the name, URL, ID, and request type are empty or blank.
     * It also checks if the URL is absolute or relative and sets the ID if it is empty.
     *
     * @param restResourceDao the REST resource DAO object
     * @return the fixed REST resource DAO object
     */
    public static RestResourceDao checkAndFixRestResourceDaoObject(RestResourceDao restResourceDao) {
        if (StringUtils.isEmpty(restResourceDao.getName())) {
            throw new JsonParseException("resource name is empty or blank ");
        }
        if (StringUtils.isEmpty(restResourceDao.getUrl())) {
            throw new JsonParseException("resource url is empty or blank ");
        }
        try {
            if (UrlUtils.isUrlAbsolute(restResourceDao.getUrl())) {
                restResourceDao.setUrl(UrlUtils.getCompletePathSegment(restResourceDao.getUrl()));
            } else {
                if (!restResourceDao.getUrl().startsWith(FORWARD_SLASH)) {
                    restResourceDao.setUrl(StringUtils.prependIfMissing(restResourceDao.getUrl(), FORWARD_SLASH));
                }
            }
        } catch (Exception e) {
            throw new JsonParseException("Unable to get relative path from url " + e.getMessage(), e);
        }
        if (StringUtils.isEmpty(restResourceDao.getId())) {
            restResourceDao.setId(UUID.randomUUID().toString());
        }
        if (StringUtils.isEmpty(restResourceDao.getRequestType())) {
            restResourceDao.setRequestType(HTTP_METHOD_GET);
        }
        return restResourceDao;
    }

    /**
     * Checks if all resource names are unique.
     * <p>
     * This method checks if the resource names are duplicated.
     *
     * @param restResourceDaoList the list of REST resource DAO objects
     * @throws ItemExistsException if the resource names are duplicated
     */
    public static void checkIfAllResourceNamesAreUnique(List<RestResourceDao> restResourceDaoList) throws ItemExistsException {
        Set<String> nameArray = new HashSet<>();
        for (int i = 0; restResourceDaoList != null && i < restResourceDaoList.size(); i++) {
            nameArray.add(restResourceDaoList.get(i).getName().toLowerCase());
        }
        if (nameArray.size() != restResourceDaoList.size()) {
            throw new ItemExistsException("Resource names are duplicated. Resource name should be unique");
        }
    }

    /**
     * Validates the default array of URL list.
     * <p>
     * This method validates the default array of URL list and checks if the resource names are unique.
     *
     * @param restResourceDaoList the list of REST resource DAO objects
     * @return the validated list of REST resource DAO objects
     * @throws ItemExistsException if the resource names are duplicated
     */
    public static List<RestResourceDao> validateDefaultArrayOfUrlList(List<RestResourceDao> restResourceDaoList) throws ItemExistsException {
        for (int i = 0; restResourceDaoList != null && i < restResourceDaoList.size(); i++) {
            restResourceDaoList.set(i, RestResourceUtil.checkAndFixRestResourceDaoObject(restResourceDaoList.get(i)));
        }
        checkIfAllResourceNamesAreUnique(restResourceDaoList);
        return restResourceDaoList;
    }
}
