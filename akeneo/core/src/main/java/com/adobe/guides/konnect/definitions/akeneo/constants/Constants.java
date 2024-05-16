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
package com.adobe.guides.konnect.definitions.akeneo.constants;

/**
 * Constants to be used throughout the code.<p>
 *
 * @author Adobe
 * @since 1.0.0
 */
public class Constants {

    public static String AKENEO = "Akeneo";
    public static Integer DEFAULT_QUERY_LIMIT = 100;
    public static String CONTENT_TYPE_HEADER = "Content-Type";
    public static String APPLICATION_JSON = "application/json";
    public static final String AKENEO_LOGO_SVG_PATH = "logo/akeneo_logo.svg";
    public static String AKENEO_VALIDATION_QUERY = "";
    public static String PRODUCT_INFORMATION_MANAGEMENT = "Product Information Management";
    public static String AKENEO_DEFAULT_QUERY = "search={\"categories\":[{\"operator\":\"IN\",\"value\":[\"0001\"]}]}";
    public static String AKENEO_PAGE_LIMIT_PARAM = "limit";
    public static final String AKENEO_DESC = "AEM Guides Akeneo data source connector to query and visualize the data.";
    public static final String AKENEO_AUTH_CONFIG = "App username password authentication";
    public static final String AKENEO_AUTH_INFO = "App username password authentication";
    public static final String AKENEO_PASS_INFO = "App password";
    public static final String AKENEO_CLIENTID_TOKEN = "App client id";
    public static final String AKENEO_CLIENTID_SECRET = "App secret";
    public static final String AKENEO_CONFIG_INFO = "An App username password authentication containing the security credentials for Akeneo Connector";
    public static final String[] AKENEO_TEMPLATE_PATH = new String[]{"akeneo_attributes.vm", "akeneo-families.vm", "akeneo-locales-template.vm", "akeneo-product-table_records.vm", "akeneo-single.vm", "akeneoCategories.vm"};
    public static final String BEARER_TOKEN = "Bearer token authentication";
    public static String BEARER_TOKEN_AUTH_KEY = "Bearer ";
    public static String DEFAULT_AUTH_HEADER = "Authorization";
    public static final String REST_BEARER_AUTH_USERNAME_INFO = "Bearer authentication (also called token authentication) is an HTTP authentication scheme that involves security tokens called bearer tokens. The name \"Bearer authentication\" can be understood as give access to the bearer of this token.";
    public static final String BEARER_AUTH_HEADER_INFO = "Header name for bearer authentication default is Authorization";
    public static final String BEARER_AUTH_TOKEN_INFO = "Bearer Token";
    public static String DEFAULT_TEMPLATE_PATH = "/libs/fmdita/konnect/default";
    public static String BASIC_AUTH_KEY = "Basic ";
    public static String SPACE_PLUS_URL = "+";
    public static String SPACE_URL = "%2B";
    public static String CHARSET_UTF8 = "UTF-8";
}
