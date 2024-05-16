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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Utility class for the Gson conversions.<p>
 * This class provides utility methods for the converting
 * from and to JSON using Gson library.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class GsonUtils {

    private static Gson gson;
    private static GsonUtils instance = null;

    private GsonUtils() {

    }

    /**
     * Creates an instance of the GsonUtils class.
     *
     * @return an instance of the GsonUtils class.
     */
    public static GsonUtils getInstance() {
        if (instance == null) {
            instance = new GsonUtils();
            gson = new GsonBuilder().disableHtmlEscaping().create();
        }
        return instance;
    }

    /**
     * Creates a JSON string from the object.
     *
     * @param object the object to be converted to JSON string.
     * @return {@code String} the JSON string.
     */
    public <T> String getStringFromObject(T object) {
        return gson.toJson(object);
    }

    /**
     * Creates an object from the JSON string.
     *
     * @param jsonString the JSON string to be converted to object.
     * @param valueType  the class type of the object.
     * @return {@code T} the object of class type taken in input param.
     */
    public <T> T getObjectFromString(String jsonString, Class<T> valueType) {
        T object = gson.fromJson(jsonString, valueType);
        return object;
    }
}
