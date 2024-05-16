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
package com.adobe.guides.konnect.definitions.salsify.constants;

/**
 * Constants to be used throughout the code.<p>
 *
 * @author Adobe
 * @since 1.0.0
 */
public class Constants {

    public static String SALSIFY = "Salsify";
    public static String PRODUCT_INFORMATION_MANAGEMENT = "Product Information Management";
    public static String CONTENT_TYPE_HEADER = "Content-Type";
    public static String APPLICATION_JSON = "application/json";
    public static final String SALSIFY_LOGO_SVG_PATH = "logo/salsify_logo.svg";
    public static String SALSIFY_VALIDATION_QUERY = "";
    public static String SALSIFY_DEFAULT_QUERY = "{\"filter\": \"<optional filter string> \",\n\t\"page\": \"<optional page number>\",\n\t\"per_page\": \"<optional no of items per page>\"\n}";
    public static String SALSIFY_PAGE_LIMIT_PARAM = "limit";
    public static final String SALSIFY_DESC = "AEM Guides Salsify data source connector to query and visualize the data.";
    public static final String[] SALSIFY_TEMPLATE_PATH = new String[]{"salsify-table_records.vm", "salsify-topic-generator.vm"};

    /*Salsify request query params*/
    public static final String SALSIFY_FILTER_QUERY_PARAM = "filter";
    public static final String SALSIFY_CURSOR_QUERY_PARAM = "cursor";
    public static final String SALSIFY_PAGE_QUERY_PARAM = "page";
    public static final String SALSIFY_PER_PAGE_QUERY_PARAM = "per_page";
    public static final String SALSIFY_TOTAL_ENTRIES_QUERY_PARAM = "total_entries";
    public static final String SALSIFY_CURRENT_PAGE_QUERY_PARAM = "current_page";
    public static final String BEARER_TOKEN = "Bearer token authentication";
    public static String BEARER_TOKEN_AUTH_KEY = "Bearer ";
    public static String DEFAULT_AUTH_HEADER = "Authorization";
    public static final String REST_BEARER_AUTH_USERNAME_INFO = "Bearer authentication (also called token authentication) is an HTTP authentication scheme that involves security tokens called bearer tokens. The name \"Bearer authentication\" can be understood as give access to the bearer of this token.";
    public static final String BEARER_AUTH_HEADER_INFO = "Header name for bearer authentication default is Authorization";
    public static final String BEARER_AUTH_TOKEN_INFO = "Bearer Token";
    public static String DEFAULT_TEMPLATE_PATH = "/libs/fmdita/konnect/default";

}
