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
package com.adobe.guides.konnect.definitions.core.connector;

import com.adobe.guides.konnect.definitions.core.config.Config;
import com.adobe.guides.konnect.definitions.core.config.ConfigDto;
import com.adobe.guides.konnect.definitions.core.config.RestConfig;
import com.adobe.guides.konnect.definitions.core.exception.KonnectConnectionException;
import com.adobe.guides.konnect.definitions.core.exception.KonnectException;
import com.adobe.guides.konnect.definitions.core.exception.KonnectQueryException;
import com.adobe.guides.konnect.definitions.core.models.graphql.GraphQLRequest;
import com.adobe.guides.konnect.definitions.core.models.graphql.GraphQLResponse;
import com.adobe.guides.konnect.definitions.core.query.QueryInfoDto;
import com.adobe.guides.konnect.definitions.core.query.QueryResultDto;
import com.adobe.guides.konnect.definitions.core.util.RestInvoker;
import com.adobe.guides.konnect.definitions.core.util.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.ADOBE_SYSTEMS;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.GRAPHQL;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.HTTP_METHOD_GET;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.HTTP_METHOD_POST;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.OPERATIONNAME;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.QUERY;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.VARIABLES;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.contentType;

/**
 * This class provides a skeletal implementation of the {@link Connector}
 * interface to minimize the effort required to implement this interface
 * for the GraphQL-based connector.  For a SQL data source connector, {@link SqlConnector}
 * should be used. For a NoSQL data source connector, {@link NoSqlConnector}
 * should be used.
 *
 * <p>To implement a GraphQL connector, the programmer needs only to extend
 * this class and provide implementations for the {@link #getName()} and
 * {@link #getConfigClass()} methods.
 *
 * <p>The documentation for each non-abstract method in this class describes its
 * implementation in detail.  Each of these methods may be overridden if the
 * Connector being implemented admits a more efficient implementation.
 *
 * @author Adobe
 * @since 1.0.0
 */
public abstract class GraphqlConnector implements Connector {

    private final transient Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private static final Logger log = LoggerFactory.getLogger(GraphqlConnector.class);

    /**
     * Returns a {@link RestConfig} object for the Config to be used to execute
     * a query.
     *
     * @param config {@link Config} object which contains the configuration details
     *               for the REST API.
     * @return {@link RestConfig} which is the Config to be used to execute a query
     */
    public abstract RestConfig getRestConfig(Config config);

    /**
     * Returns a {@code String} query with limit added to it
     *
     * @param query {@code String} query to which limit needs to be added
     *              to execute the query.
     * @return {@code String} query with limit added to it
     */
    public abstract String getQueryWithLimit(String query);

    /**
     * Returns a {@link HttpClient} object to be used to execute the HTTP request.
     *
     * @return {@link HttpClient} object to be used to execute the HTTP request.
     */
    public abstract HttpClient getHttpClient();

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthor() {
        return ADOBE_SYSTEMS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGroup() {
        return GRAPHQL;
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation connects to a GraphQL API using the connector configs
     * passed in the {@link ConfigDto} object.
     */
    @Override
    public boolean validateConnection(ConfigDto configDto) {
        RestConfig restConfig = getRestConfig(configDto.getConfig());
        try {
            RestInvoker invoker = new RestInvoker();
            String hostName = UrlUtils.getHostname(restConfig.getUrl(true));
            String url = "";
            if (StringUtils.isBlank(UrlUtils.getPort(restConfig.getUrl(true)))) {
                url = UrlUtils.createNewUri(UrlUtils.getProtocol(restConfig.getUrl(true)), hostName);
            } else {
                url = UrlUtils.createNewUri(UrlUtils.getProtocol(restConfig.getUrl(true)), hostName, UrlUtils.getPort(restConfig.getUrl(true)));
            }
            HttpUriRequest uriRequest = invoker.prepareConnection(restConfig.getAuthenticationDetails(), url, restConfig.getRequestType(true), restConfig.getBody(true), "", restConfig.getHeaders(true));
            invoker.invokeRequest(uriRequest, getHttpClient());
            return true;
        } catch (IOException | URISyntaxException | KonnectException e) {
            log.error("[GraphQLConnector] Error in connecting to client", e);
        } catch (Exception e) {
            log.error("[GraphQLConnector] Error in sending request", e);
        }
        return false;
    }

    /**
     * Executes a list of queries on a GraphQL API.
     *
     * <p>This implementation connects to a GraphQL API using the connector config
     * passed in the {@link ConfigDto} object. It executes the list of queries passed in
     * the {@link QueryInfoDto} list.
     *
     * <p>The response is a JSON string which merges results from all query executions
     * into a JSON object with the query name present in the <tt>QueryInfoDto</tt>
     *
     * @param configDto     Connector config which needs to be executed.
     * @param queryInfoList The {@code List} of queries which will be executed.
     * @return A <tt>String</tt> which is a single JSON response of all query executions.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    @Override
    public String execute(ConfigDto configDto, List<QueryInfoDto> queryInfoList) throws KonnectException {
        boolean isValidConnection = validateConnection(configDto);
        if (!isValidConnection) {
            throw new KonnectConnectionException("[GraphQLConnector] Error in connecting to client");
        }
        GraphQLRequest request = null;
        JsonObject queryResult = new JsonObject();
        for (QueryInfoDto queryInfo : queryInfoList) {
            request = new GraphQLRequest(queryInfo.getQuery());
            GraphQLResponse graphQLResponse = execute(request, configDto);
            String subQueryResult = gson.toJson(graphQLResponse.getData());
            JsonElement jsonElement = gson.fromJson(subQueryResult, JsonElement.class);
            queryResult.add(queryInfo.getQueryName(), jsonElement);
        }
        return queryResult.toString();
    }

    /**
     * Executes a query on a GraphQL API.
     *
     * <p>This implementation connects to a GraphQL API using the connector config
     * passed in the {@link ConfigDto} object. It executes the query passed in
     * {@link QueryInfoDto} object.
     *
     * <p>The response is a JSON string of the result from executing the query.
     *
     * @param configDto Connector config which needs to be executed.
     * @param queryInfo The query which will be executed.
     * @return A <tt>String</tt> which is the JSON response of query execution.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    @Override
    public String execute(ConfigDto configDto, QueryInfoDto queryInfo) throws KonnectException {
        boolean isValidConnection = validateConnection(configDto);
        if (!isValidConnection) {
            throw new KonnectConnectionException("[GraphQLConnector] Error in connecting to client");
        }
        GraphQLRequest request = new GraphQLRequest(queryInfo.getQuery());
        GraphQLResponse graphQLResponse = execute(request, configDto);
        String responseString = gson.toJson(graphQLResponse.getData());
        return responseString;
    }

    /**
     * Executes a query with a limit on a GraphQL API.
     *
     * <p>This implementation connects to a GraphQL API using the connector config
     * passed in the {@link ConfigDto} object. It executes the query passed in
     * {@link QueryInfoDto} object by adding a limit to it.
     *
     * <p>The response is a JSON string of the result from executing the query.
     *
     * @param configDto Connector config which needs to be executed.
     * @param queryInfo The query which will be executed.
     * @return A <tt>String</tt> which is the JSON response of query execution.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    @Override
    public QueryResultDto executeWithLimit(ConfigDto configDto, QueryInfoDto queryInfo) throws KonnectException {
        String query = getQueryWithLimit(queryInfo.getQuery());
        queryInfo.setQuery(query);
        QueryResultDto queryResultDto = new QueryResultDto();
        queryResultDto.setQuery(query);
        queryResultDto.setResponse(execute(configDto, queryInfo));
        return queryResultDto;
    }

    /**
     * Helper function which executes a query on a GraphQL API.
     *
     * @param request   The GraphQL request object which contains the query,
     *                  operation name and variables.
     * @param configDto Connector config which needs to be executed.
     * @return A {@link GraphQLResponse} object which contains the response from
     * executing the query.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    private GraphQLResponse execute(GraphQLRequest request, ConfigDto configDto) throws KonnectException {
        RestConfig restConfig = getRestConfig(configDto.getConfig());
        try {
            RestInvoker invoker = new RestInvoker();
            HttpUriRequest uriRequest = buildRequest(request, restConfig, invoker);
            String responseString = invoker.invokeRequest(uriRequest, getHttpClient());

            GraphQLResponse response = convertHttpResponseToGraphQLResponse(responseString);
            String errors = getErrorsFromResponse(response);
            if (StringUtils.isNotBlank(errors)) {
                throw new KonnectQueryException("[GraphQLConnector] GraphQL query response has errors" + errors);
            }

            return response;
        } catch (KonnectQueryException e) {
            throw e;
        } catch (IOException e) {
            throw new KonnectQueryException("[GraphQLConnector] Error in connecting to client", e);
        } catch (KonnectException e) {
            throw e;
        } catch (Exception e) {
            throw new KonnectException("[GraphQLConnector] Error in sending request", e);
        }
    }

    /**
     * Helper function to get the errors from the GraphQL response.
     *
     * @param response The {@link GraphQLResponse} object which contains the response
     *                 from executing the query.
     * @return A {@code String} which contains the errors from the response.
     */
    private String getErrorsFromResponse(GraphQLResponse response) {
        if (response.getErrors() != null) {
            Type listErrorsType = TypeToken.getParameterized(List.class, Object.class).getType();
            String errors = gson.toJson(response.getErrors(), listErrorsType);
            log.warn("GraphQL request {} returned some errors {}", errors);
            return errors;
        }
        return "";
    }

    /**
     * Helper function to convert the HTTP response to a GraphQL response.
     *
     * @param responseString The {@code String} response from executing the query.
     * @return A {@link GraphQLResponse} object which contains the response from
     * executing the query.
     */
    private GraphQLResponse convertHttpResponseToGraphQLResponse(String responseString) {
        Type type = TypeToken.getParameterized(GraphQLResponse.class).getType();
        GraphQLResponse response = gson.fromJson(responseString, type);
        return response;
    }

    /**
     * Helper function to build the HTTP request to execute the GraphQL query.
     *
     * @param request    The GraphQL request object which contains the query,
     *                   operation name and variables.
     * @param restConfig The {@link RestConfig} object which contains the configuration
     *                   details for the REST API.
     * @param invoker    The {@link RestInvoker} object which is used to invoke the
     *                   HTTP request.
     * @return A {@link HttpUriRequest} object which is the HTTP request to execute
     * the GraphQL query.
     * @throws UnsupportedEncodingException if the encoding is not supported.
     */
    private HttpUriRequest buildRequest(GraphQLRequest request, RestConfig restConfig, RestInvoker invoker) throws UnsupportedEncodingException {
        String httpMethod = HTTP_METHOD_GET;
        if (restConfig != null && restConfig.getRequestType() != null) {
            httpMethod = restConfig.getRequestType().equalsIgnoreCase("GET") ? HTTP_METHOD_GET : HTTP_METHOD_POST;
        }
        RequestBuilder rb = RequestBuilder.create(httpMethod).setUri(restConfig.getUrl());
        rb.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        String builtQuery = invoker.buildQuery(request.getQuery(), restConfig.getAuthenticationDetails().getQuery());

        if (HTTP_METHOD_GET.equals(httpMethod)) {
            rb.addParameter(QUERY, builtQuery);
            if (request.getOperationName() != null) {
                rb.addParameter(OPERATIONNAME, request.getOperationName());
            }
            if (request.getVariables() != null) {
                String json = gson.toJson(request.getVariables());
                rb.addParameter(VARIABLES, json);
            }
        } else {
            rb.setEntity(new StringEntity(gson.toJson(request), StandardCharsets.UTF_8.name()));
        }
        HttpUriRequest uriRequest = rb.build();
        invoker.addHeaders(uriRequest, restConfig.getAuthenticationDetails().getHeader(), restConfig.getHeaders());

        return uriRequest;
    }
}
