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
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_ID;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_NAME;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_REQUEST_BODY;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_REQUEST_HEADERS;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_REQUEST_TYPE;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_RESOURCE_IS_DEFAULT;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_RESOURCE_IS_ENABLED;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_SAMPLE_QUERY;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST_CONFIG_URL;

/**
 * This class is used to deserialize a RestResourceDao object
 * from a JSON object.
 *
 * @author Adobe
 * @see RestResourceDao
 * @see JsonDeserializer
 * @since 1.0.0
 */
public class RestResourceDeserializer implements JsonDeserializer<RestResourceDao> {

    private static final Logger log = LoggerFactory.getLogger(RestResourceDeserializer.class);

    /**
     * This method is used to deserialize a JSON object into a RestResourceDao object.
     * <p>
     * It converts the JSON object into a RestResourceDao object.
     * It also extracts the headers from the JSON object.
     * The headers are then added to the RestResourceDao object.
     * The RestResourceDao object is then returned.
     *
     * @param json    JSON object to be deserialized
     * @param typeOfT type of the object to be deserialized
     * @param context JSON deserialization context
     * @return a RestResourceDao object deserialized from the JSON object
     * @throws JsonParseException if there is an error while parsing the JSON object.
     */
    @Override
    public RestResourceDao deserialize(JsonElement json, Type typeOfT,
                                       JsonDeserializationContext context) throws JsonParseException {
        log.debug("deserializing rest resource {} ", json.toString());
        JsonObject jsonObject = json.getAsJsonObject();
        return convertJsonObjectToDao(jsonObject, getHeadersFromObject(jsonObject, context));
    }

    /**
     * This method is used to extract the headers from a JSON object.
     * <p>
     * It extracts the headers from the JSON object and returns them as a map.
     * If the headers are not present in the JSON object, an empty map is returned.
     *
     * @param jsonObject JSON object from which headers are to be extracted
     * @param context    JSON deserialization context
     * @return a map of headers extracted from the JSON object
     */
    public static Map<String, String> getHeadersFromObject(JsonObject jsonObject, JsonDeserializationContext context) {
        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        JsonElement headerObject = jsonObject.has(REST_CONFIG_REQUEST_HEADERS) ? jsonObject.get(REST_CONFIG_REQUEST_HEADERS) : null;
        Map<String, String> headers = headerObject != null ? context.deserialize(headerObject, mapType) : new HashMap<>();
        return headers;
    }

    /**
     * This method is used to convert a JSON object into a RestResourceDao object.
     * <p>
     * It converts the JSON object into a RestResourceDao object.
     * It also extracts the headers from the JSON object.
     * The headers are then added to the RestResourceDao object.
     * The RestResourceDao object is then returned.
     *
     * @param jsonObject JSON object to be converted
     * @param headers    map of headers to be added to the RestResourceDao object
     * @return a RestResourceDao object converted from the JSON object
     */
    public static RestResourceDao convertJsonObjectToDao(JsonObject jsonObject, Map<String, String> headers) {
        RestResourceDao restResourceDao = new RestResourceDao();
        String id = jsonObject.has(REST_CONFIG_ID) ? jsonObject.get(REST_CONFIG_ID).getAsString().trim() : "";
        String name = jsonObject.has(REST_CONFIG_NAME) ? jsonObject.get(REST_CONFIG_NAME).getAsString().trim() : "";
        String url = jsonObject.has(REST_CONFIG_URL) ? jsonObject.get(REST_CONFIG_URL).getAsString().trim() : "";
        String type = jsonObject.has(REST_CONFIG_REQUEST_TYPE) ? jsonObject.get(REST_CONFIG_REQUEST_TYPE).getAsString().trim() : "";
        String body = jsonObject.has(REST_CONFIG_REQUEST_BODY) ? jsonObject.get(REST_CONFIG_REQUEST_BODY).getAsString().trim() : "";
        String sampleQuery = jsonObject.has(REST_CONFIG_SAMPLE_QUERY) ? jsonObject.get(REST_CONFIG_SAMPLE_QUERY).getAsString().trim() : "";
        boolean isDefault = false;
        if (jsonObject.has(REST_CONFIG_RESOURCE_IS_DEFAULT)) {
            isDefault = jsonObject.get(REST_CONFIG_RESOURCE_IS_DEFAULT).getAsBoolean();
        }
        boolean isEnabled = false;
        if (jsonObject.has(REST_CONFIG_RESOURCE_IS_ENABLED)) {
            isEnabled = jsonObject.get(REST_CONFIG_RESOURCE_IS_ENABLED).getAsBoolean();
        }
        restResourceDao.setId(id)
                .setName(name)
                .setUrl(url)
                .setRequestType(type.toUpperCase(Locale.ROOT))
                .setBody(body)
                .setDefault(isDefault).setEnabled(isEnabled)
                .setHeaders(headers)
                .setSampleQuery(sampleQuery);
        restResourceDao = RestResourceUtil.checkAndFixRestResourceDaoObject(restResourceDao);
        return restResourceDao;
    }


}
