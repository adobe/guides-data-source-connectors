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
import com.adobe.guides.konnect.definitions.core.query.QueryInfoDto;
import com.adobe.guides.konnect.definitions.core.query.QueryResultDto;
import com.adobe.guides.konnect.definitions.core.urlResource.AdditionalUrlResources;
import com.adobe.guides.konnect.definitions.core.urlResource.RestResourceDao;
import com.adobe.guides.konnect.definitions.core.util.RestInvoker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.ADOBE_SYSTEMS;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.REST;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.RESOURCE_ID;

/**
 * This class provides a skeletal implementation of the {@link Connector}
 * interface to minimize the effort required to implement this interface
 * for the REST-based connector.  For a SQL data source connector, {@link SqlConnector}
 * should be used. For a NoSQL data source connector, {@link NoSqlConnector}
 * should be used.
 *
 * <p>To implement a REST connector, the programmer needs only to extend
 * this class and provide implementations for the {@link #getName()},
 * {@link #getConfigClass()} and {@link #getRestConfig(Config)} methods.
 *
 * <p>The documentation for each non-abstract method in this class describes its
 * implementation in detail.  Each of these methods may be overridden if the
 * Connector being implemented admits a more efficient implementation.
 *
 * @author Adobe
 * @since 1.0.0
 */
public abstract class RestConnector implements Connector, AdditionalUrlResources {

    /**
     * Gson object to be used for JSON serialization.
     */
    private final transient Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * Logger object for logs.
     */
    private static final Logger log = LoggerFactory.getLogger(RestConnector.class);

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
        return REST;
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation connects to a REST API using the connector configs
     * passed in the {@link ConfigDto} object.
     */
    @Override
    public boolean validateConnection(ConfigDto configDto) {
        RestConfig restConfig = getRestConfig(configDto.getConfig());
        try {
            RestInvoker invoker = new RestInvoker();
            HttpUriRequest uriRequest = invoker.prepareConnection(restConfig.getAuthenticationDetails(), restConfig.getUrl(true), restConfig.getRequestType(true), restConfig.getBody(true), "", restConfig.getHeaders(true));
            invoker.invokeRequest(uriRequest, getHttpClient());
            return true;
        } catch (IOException | URISyntaxException | KonnectException e) {
            log.error("[RestConnector] Error in connecting to client", e);
        } catch (Exception e) {
            log.error("[RestConnector] Error in sending request", e);
        }
        return false;
    }

    /**
     * Executes a list of queries on a REST API.
     *
     * <p>This implementation connects to a REST API using the connector config
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
            throw new KonnectConnectionException("[RestConnector] Error in connecting to client");
        }
        RestConfig restConfig = null;
        try {
            JsonObject queryResult = new JsonObject();
            for (QueryInfoDto queryInfo : queryInfoList) {
                restConfig = getRestConfig(configDto.getConfig(), queryInfo);
                RestInvoker invoker = new RestInvoker();
                HttpUriRequest uriRequest = invoker.prepareConnection(restConfig.getAuthenticationDetails(), restConfig.getUrl(), restConfig.getRequestType(), restConfig.getBody(), queryInfo.getQuery(), restConfig.getHeaders());
                String subQueryResult = invoker.invokeRequest(uriRequest, getHttpClient());
                JsonElement jsonElement = gson.fromJson(subQueryResult, JsonElement.class);
                queryResult.add(queryInfo.getQueryName(), jsonElement);
                clearRestConfigOfResourceUrl(restConfig);
            }
            return queryResult.toString();
        } catch (IOException | URISyntaxException e) {
            throw new KonnectQueryException("[RestConnector] Error in connecting to client", e);
        } catch (KonnectException e) {
            throw e;
        } catch (Exception e) {
            throw new KonnectException("[RestConnector] Error in sending request", e);
        }
    }

    /**
     * Executes a query on a REST API.
     *
     * <p>This implementation connects to a REST API using the connector config
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
            throw new KonnectConnectionException("[RestConnector] Error in connecting to client");
        }
        RestConfig restConfig = null;
        try {
            restConfig = getRestConfig(configDto.getConfig(), queryInfo);
            RestInvoker invoker = new RestInvoker();
            HttpUriRequest uriRequest = invoker.prepareConnection(restConfig.getAuthenticationDetails(), restConfig.getUrl(), restConfig.getRequestType(), restConfig.getBody(), queryInfo.getQuery(), restConfig.getHeaders());
            return invoker.invokeRequest(uriRequest, getHttpClient());
        } catch (IOException | URISyntaxException e) {
            throw new KonnectQueryException("[RestConnector] Error in connecting to client", e);
        } catch (KonnectException e) {
            throw e;
        } catch (Exception e) {
            throw new KonnectException("[RestConnector] Error in sending request", e);
        } finally {
            clearRestConfigOfResourceUrl(restConfig);
        }
    }

    /**
     * Executes a query on a REST API.
     *
     * <p>The response is a JSON string of the result from executing the query.
     *
     * @param configDto Connector config which needs to be executed.
     * @param queryInfo The query which will be executed.
     * @param query     The query which will be executed.
     * @return A <tt>String</tt> which is the JSON response of query execution.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    public String execute(ConfigDto configDto, String query, QueryInfoDto queryInfo) throws KonnectException {
        boolean isValidConnection = validateConnection(configDto);
        if (!isValidConnection) {
            throw new KonnectConnectionException("[RestConnector] Error in connecting to client");
        }
        RestConfig restConfig = null;
        try {
            restConfig = getRestConfig(configDto.getConfig(), queryInfo);
            RestInvoker invoker = new RestInvoker();
            HttpUriRequest uriRequest = invoker.prepareConnection(restConfig.getAuthenticationDetails(), restConfig.getUrl(), restConfig.getRequestType(), restConfig.getBody(), query, restConfig.getHeaders());
            return invoker.invokeRequest(uriRequest, getHttpClient());
        } catch (IOException | URISyntaxException e) {
            throw new KonnectQueryException("[RestConnector] Error in connecting to client", e);
        } catch (KonnectException e) {
            throw e;
        } catch (Exception e) {
            throw new KonnectException("[RestConnector] Error in sending request", e);
        } finally {
            clearRestConfigOfResourceUrl(restConfig);
        }
    }

    /**
     * Executes a query with a limit on a REST API.
     *
     * <p>This implementation connects to a REST API using the connector config
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
     * {@inheritDoc}
     */
    @Override
    public List<RestResourceDao> getDefaultUrlList() {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean areMoreResourceUrlAllowed() {
        return false;
    }

    /**
     * Returns a {@link RestConfig} object for the Config to be used to execute
     * a query.
     *
     * @param config       {@link Config} object which contains the configuration details
     *                     for the REST API.
     * @param queryInfoDto {@link QueryInfoDto} object which contains the query details
     *                     for the REST API.
     * @return {@link RestConfig} which is the Config to be used to execute a query.
     * @throws KonnectException if the URL Resource selected for query is invalid.
     */
    public RestConfig getRestConfig(Config config, QueryInfoDto queryInfoDto) throws KonnectException {
        log.debug("Setting resource {} for connector ", queryInfoDto.getAdditionalResourceInfo());
        RestConfig restConfig = getRestConfig(config);
        if (restConfig != null && queryInfoDto.getAdditionalResourceInfo().containsKey(RESOURCE_ID) && StringUtils.isNotEmpty(queryInfoDto.getAdditionalResourceInfo().get(RESOURCE_ID))) {
            restConfig.setCurrentResource(queryInfoDto.getAdditionalResourceInfo().get(RESOURCE_ID));
        }
        return restConfig;
    }

    /**
     * Function to clear the resource URL from the {@link RestConfig} object.
     *
     * @param restConfig - {@link RestConfig} object which needs to be cleared.
     * @throws KonnectException if the URL Resource selected for query is invalid.
     */
    public void clearRestConfigOfResourceUrl(RestConfig restConfig) throws KonnectException {
        if (restConfig != null) {
            restConfig.setCurrentResource(StringUtils.EMPTY);
        }
    }
}
