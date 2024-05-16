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
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.ItemExistsException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is used to deserialize a JSON array of REST resources.
 * It implements the {@link JsonDeserializer} interface.
 *
 * @author Adobe
 * @see RestResourceDao
 * @see RestResourceDeserializer
 * @since 1.0.0
 */
public class RestResourceArrayDeserializer implements JsonDeserializer<List<RestResourceDao>> {

    private static final Logger log = LoggerFactory.getLogger(RestResourceArrayDeserializer.class);

    /**
     * Deserializes a JSON array of REST resources.
     * It converts the JSON array into a list of {@link RestResourceDao} objects.
     * <p>
     * It also checks if all resource names are unique.
     * If not, it throws a {@link JsonParseException}.
     *
     * @param json    the JSON element being deserialized
     * @param typeOfT the type of the Object to deserialize to
     *                (in this case, a list of {@link RestResourceDao} objects)
     * @param context the context for deserialization
     * @return a list of {@link RestResourceDao} objects
     * @throws JsonParseException if the JSON is not in the correct format or
     *                            if the resource names are not unique.
     */
    @Override
    public List<RestResourceDao> deserialize(JsonElement json, Type typeOfT,
                                             JsonDeserializationContext context) throws JsonParseException {

        List<RestResourceDao> restResourceDaoList = new ArrayList<>();
        if (json.isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray();
            for (int i = 0; jsonArray != null && i < jsonArray.size(); i++) {
                JsonObject currentElement = jsonArray.get(i).getAsJsonObject();
                Map<String, String> headers = RestResourceDeserializer.getHeadersFromObject(currentElement, context);
                RestResourceDao restResourceDao = RestResourceDeserializer.convertJsonObjectToDao(currentElement, headers);
                restResourceDaoList.add(restResourceDao);
            }
            try {
                checkIfAllResourceNamesAreUnique(restResourceDaoList);
            } catch (Exception e) {
                throw new JsonParseException(e.getMessage());
            }
            return restResourceDaoList;
        }
        return restResourceDaoList;
    }

    /**
     * Checks if all resource names are unique.
     * If not, it throws an {@link ItemExistsException}.
     *
     * @param restResourceDaoList a list of {@link RestResourceDao} objects
     * @throws ItemExistsException if the resource names are not unique
     */
    public void checkIfAllResourceNamesAreUnique(List<RestResourceDao> restResourceDaoList) throws ItemExistsException {
        RestResourceUtil.checkIfAllResourceNamesAreUnique(restResourceDaoList);
    }
}
