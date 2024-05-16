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
package com.adobe.guides.konnect.definitions.core.constants;

import org.apache.sling.api.resource.ResourceResolverFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Constants which will be used in the system for
 * connectors and their definitions.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class Constants {

    /*
     * Adobe system author name.
     */
    public static String ADOBE_SYSTEMS = "Adobe Systems";

    /*
     * Default groups for abstract connectors.
     */
    public static String SQL_DB = "SQL Database";
    public static String PROJECT_MANAGEMENT = "Project Management";
    public static String PRODUCT_INFORMATION_MANAGEMENT = "Product Information Management";
    public static String NOSQL_DB = "NoSQL Database";
    public static String REST = "REST Connector";
    public static String FILE_CONNECTOR = "File Connector";
    public static String GRAPHQL = "GraphQL";
    public static String E_COMMERCE = "E-Commerce";

    public static Integer DEFAULT_LIMIT_PREVIEW = 5;
    public static String SEMICOLON = ";";
    /*
     * GRaphqlParams
     */
    public static String contentType = "application/json";
    public static String QUERY = "query";
    public static String OPERATIONNAME = "operationName";
    public static String VARIABLES = "variables";
    /* ANNOTATION CONSTANTS*/
    public static final String ANNOTATION_IGNORE = "ignore";
    public static final String ANNOTATION_LABEL = "label";
    public static final String ANNOTATION_REQUIRED = "required";
    public static final String ANNOTATION_INFO = "info";
    public static final String ANNOTATION_TYPE = "type";
    public static final String ANNOTATION_SUBTYPE = "subtype";
    public static final String ANNOTATION_DEFAULT = "default";
    public static final String ANNOTATION_TRUE = "true";
    public static final String ANNOTATION_FALSE = "false";
    public static final String ANNOTATION_PATH = "path";
    public static final String ANNOTATION_ENUM = "enum";
    public static final String ANNOTATION_VALUES = "values";
    public static final String ANNOTATION_FILE = "file";
    public static final String ANNOTATION_FOLDER = "folder";
    public static final String ANNOTATION_INCLUDE_SUBTYPE = "includeSubtype";
    public static final String ANNOTATION_PLACEHOLDER = "placeholder";
    public static final String ANNOTATION_DEFAULT_AUTH_HEADER = "Authorization";
    public static final String ANNOTATION_SQL_AUTHENTICATION = "SQL authentication";
    public static final String ANNOTATION_MONGO_PLACEHOLDER = "mongodb://[username:password@]host1[:port1][,...hostN[:portN]][/[defaultauthdb][?options]]";
    public static final String REST_CONFIG_ID = "id";
    public static final String REST_CONFIG_NAME = "name";
    public static final String REST_CONFIG_URL = "url";
    public static final String REST_CONFIG_REQUEST_TYPE = "requestType";
    public static final String REST_CONFIG_REQUEST_BODY = "body";
    public static final String REST_CONFIG_SAMPLE_QUERY = "sampleQuery";
    public static final String REST_CONFIG_RESOURCE_IS_DEFAULT = "isDefault";
    public static final String REST_CONFIG_RESOURCE_IS_ENABLED = "isEnabled";
    public static final String REST_CONFIG_REQUEST_HEADERS = "headers";
    /* CIPHER ANNOTATION CONSTANTS*/
    public static final String ACTIVE = "active";
    public static final String DEPRECATED = "deprecated";

    /*DRIVER CLASS NAME*/
    public static final String MYSQL_DEFAULT_DRIVER = "com.mysql.jdbc.Driver";
    public static final String POSTGRE_DEFAULT_DRIVER = "org.postgresql.Driver";
    public static final String MSSQL_DEFAULT_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String SQLLITE_DEFAULT_DRIVER = "org.sqlite.JDBC";
    public static final String MARIADB_DEFAULT_DRIVER = "org.mariadb.jdbc.Driver";
    public static final String H2DB_DEFAULT_DRIVER = "org.h2.Driver";
    public static final String ORACLE_DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";


    /*CONNECTION STRING SQL*/
    public static final String MYSQL_CONNECTION_STRING_SAMPLE = "jdbc:mysql://<host>:<port>/<database>";
    public static final String POSTGRE_CONNECTION_STRING_SAMPLE = "jdbc:postgresql://<host>:<port>/<database>";
    public static final String MSSQL_CONNECTION_STRING_SAMPLE = "jdbc:sqlserver://[host[\\instanceName][:portNumber]][;property=value[;property=value]]";
    public static final String SQLLITE_CONNECTION_STRING_SAMPLE = "jdbc:sqlite:<database>";
    public static final String MARIADB_CONNECTION_STRING_SAMPLE = "jdbc:mariadb://<host>:<port>/<database>";
    public static final String H2DB_CONNECTION_STRING_SAMPLE = "jdbc:h2:[file:][<path>]<database>";
    public static final String ORACLE_CONNECTION_STRING_SAMPLE = "jdbc:oracle:thin:@//<host>[:<port>]/<service_name>";

    /* HTTP Constants*/
    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_POST = "POST";
    public static final String REQUEST_TYPE_ENUM = HTTP_METHOD_GET + "|" + HTTP_METHOD_POST;
    public static final String JCR_CONTENT_RENDITION = "/jcr:content/renditions/original/jcr:content";
    public static final String SERVICE_USER_NAME = "konnect-serviceuser";
    public static final Map<String, Object> serviceUserParams = Collections.unmodifiableMap(new HashMap<String, Object>() {
        private static final long serialVersionUID = 1L;

        {
            put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER_NAME);
        }
    });

    public static final String NO_AUTHENTICATION = "No authentication config";
    public static String BACKWARD_SLASH = "\\";
    public static String FORWARD_SLASH = "/";
    public static char BACKWARD_SLASH_CHAR = '\\';
    public static String DEFAULT_PROTOCOL = "http";
    public static char FORWARD_SLASH_CHAR = '/';
    public static final String SQL_DESC = "AEM Guides %s data source connector to query and visualize the data.";
    public static String SQL_LIMIT_PARAMS = "LIMIT";
    public static String RESOURCE_ID = "resourceId";
}
