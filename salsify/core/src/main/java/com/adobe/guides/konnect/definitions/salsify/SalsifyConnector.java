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
package com.adobe.guides.konnect.definitions.salsify;

import com.adobe.guides.konnect.definitions.core.config.Config;
import com.adobe.guides.konnect.definitions.core.config.ConfigDto;
import com.adobe.guides.konnect.definitions.core.config.RestConfig;
import com.adobe.guides.konnect.definitions.core.connector.Connector;
import com.adobe.guides.konnect.definitions.core.connector.RestConnector;
import com.adobe.guides.konnect.definitions.core.exception.KonnectConnectionException;
import com.adobe.guides.konnect.definitions.core.exception.KonnectException;
import com.adobe.guides.konnect.definitions.core.exception.KonnectQueryException;
import com.adobe.guides.konnect.definitions.core.models.template.TemplateDto;
import com.adobe.guides.konnect.definitions.core.query.QueryInfoDto;
import com.adobe.guides.konnect.definitions.core.query.QueryResultDto;
import com.adobe.guides.konnect.definitions.core.urlResource.RestResourceDao;
import com.adobe.guides.konnect.definitions.core.util.HttpClient;
import com.adobe.guides.konnect.definitions.core.util.RestInvoker;
import com.adobe.guides.konnect.definitions.core.util.UrlUtils;
import com.adobe.guides.konnect.definitions.salsify.config.BearerTokenRestConfig;
import com.adobe.guides.konnect.definitions.salsify.config.RestConfigFactory;
import com.adobe.guides.konnect.definitions.salsify.dto.SalsifyRequestDao;
import com.adobe.guides.konnect.definitions.salsify.dto.SalsifyResponseDao;
import com.adobe.guides.konnect.definitions.salsify.dto.SalsifyTotalResponseDao;
import com.adobe.guides.konnect.definitions.salsify.utils.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.RESOURCE_ID;
import static com.adobe.guides.konnect.definitions.salsify.ResourceEnum.GET_ALL_ATTRIBUTES;
import static com.adobe.guides.konnect.definitions.salsify.ResourceEnum.GET_ALL_PRODUCTS;
import static com.adobe.guides.konnect.definitions.salsify.ResourceEnum.GET_ALL_RECORDS;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.DEFAULT_TEMPLATE_PATH;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.PRODUCT_INFORMATION_MANAGEMENT;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_CURSOR_QUERY_PARAM;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_DEFAULT_QUERY;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_DESC;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_FILTER_QUERY_PARAM;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_LOGO_SVG_PATH;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_PAGE_QUERY_PARAM;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_PER_PAGE_QUERY_PARAM;
import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.SALSIFY_TEMPLATE_PATH;

/**
 * Salsify connector.<p>
 * <p>
 * This connector will connect to Salsify configured via a bearer token.<p>
 * The user can query using the resources shipped inside the connector.<p>
 *
 * @author Adobe
 * @since 1.0.0
 */
@Component(service = Connector.class)
public class SalsifyConnector extends RestConnector {

    private static final Logger log = LoggerFactory.getLogger(SalsifyConnector.class);

    @Reference
    private HttpClient httpClient;

    /**
     * Returns a {@link CloseableHttpClient} object to be used to execute the HTTP request.
     * <p>
     *
     * @return {@link CloseableHttpClient} object to be used to execute the HTTP request.
     */
    @Override
    public CloseableHttpClient getHttpClient() {
        return httpClient.getCloseableHttpClient();
    }

    /**
     * Returns {@code true} if this connector is enabled.
     *
     * @return {@code true} if this connector is enabled.
     */
    @Override
    public boolean enabled() {
        return true;
    }

    /**
     * Returns the default path set for the templates of this connector.
     *
     * @return a {@code String} which is the path of templates.
     */
    @Override
    public String getDefaultTemplatePath() {
        return DEFAULT_TEMPLATE_PATH;
    }

    /**
     * Returns the group name for this connector.
     *
     * <p>This group name is used to club multiple connectors into a logical grouping.
     *
     * @return a {@code String} which is the group name of this connector.
     */
    @Override
    public String getGroup() {
        return PRODUCT_INFORMATION_MANAGEMENT;
    }

    @Reference
    private RestConfigFactory restConfigFactory;

    /**
     * Returns the SVG of the logo of the connector as a string.
     *
     * @return a {@code String} which is the SVG of the logo.
     */
    @Override
    public String getLogoSvg() {
        String logoSvg = "";
        try {
            InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(SALSIFY_LOGO_SVG_PATH);
            logoSvg = new String(IOUtils.toByteArray(inStream), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("[SalsifyConnector] Error in reading logo svg file", e);
        }
        return logoSvg;
    }

    /**
     * Returns the query used to display as a sample in the UI.
     *
     * @return a {@code String} which is the sample query string.
     */
    @Override
    public String getSampleQuery() {
        return SALSIFY_DEFAULT_QUERY;
    }

    /**
     * Returns the description used to display in the UI.
     *
     * @return a {@code String} which is the description string.
     */
    @Override
    public String getDescription() {
        return SALSIFY_DESC;
    }

    /**
     * Returns an array containing all the classes implementing {@link Config} which
     * are supported by this Connector.
     *
     * @return an array containing the classes implementing {@link Config} and supported by
     * this connector
     * @see Arrays#asList(Object[])
     */
    @Override
    public Class[] getConfigClass() {
        return new Class[]{BearerTokenRestConfig.class};
    }

    /**
     * Returns a {@link RestConfig} object for the Config to be used to execute
     * a query.
     * <p>
     * In case of this connector, it is not required.
     *
     * @return {@link RestConfig} which is the Config to be used to execute a query.
     */
    @Override
    public RestConfig getRestConfig(Config config) {
        return restConfigFactory.getRestConfig(config);
    }

    /**
     * Returns a {@code String} query with limit added to it. This query is used
     * to show a preview for the connector on the UI.
     *
     * @return {@code String} query with limit added to it.
     */
    @Override
    public String getQueryWithLimit(String query) {
        SalsifyRequestDao salsifyRequestDao = GsonUtils.getInstance().getObjectFromString(query, SalsifyRequestDao.class);
        salsifyRequestDao.setPage(0);
        salsifyRequestDao.setPerPage(getMaxNoRowsForPreviewQuery());
        return GsonUtils.getInstance().getStringFromObject(salsifyRequestDao);
    }

    /**
     * Returns the unique name for this connector.
     *
     * @return a {@code String} which is the name of this connector.
     */
    @Override
    public String getName() {
        return SALSIFY;
    }

    /**
     * Returns {@code true} if more resources (apart from default shipped in this connector)
     * are allowed to be added from the UI.
     *
     * @return {@code true} if more resources can be added from the UI.
     */
    @Override
    public Boolean areMoreResourceUrlAllowed() {
        return false;
    }

    /**
     * Returns a list of default resources being shipped with this connector.
     * <p>
     *
     * @return {@code List} of {@link RestResourceDao} which are the default resources
     * being shipped.
     */
    @Override
    public List<RestResourceDao> getDefaultUrlList() {
        List<RestResourceDao> defaultUrlMapping = new ArrayList<>();
        defaultUrlMapping.add(new RestResourceDao().setName(GET_ALL_PRODUCTS.getValue()).setUrl(GET_ALL_PRODUCTS.getUrl()).setSampleQuery(GET_ALL_PRODUCTS.getSampleQuery()).setDefault(true).setEnabled(true));
        defaultUrlMapping.add(new RestResourceDao().setName(GET_ALL_RECORDS.getValue()).setUrl(GET_ALL_RECORDS.getUrl()).setSampleQuery(GET_ALL_RECORDS.getSampleQuery()).setDefault(true).setEnabled(true));
        defaultUrlMapping.add(new RestResourceDao().setName(GET_ALL_ATTRIBUTES.getValue()).setUrl(GET_ALL_ATTRIBUTES.getUrl()).setSampleQuery(GET_ALL_ATTRIBUTES.getSampleQuery()).setDefault(true).setEnabled(true));
        return defaultUrlMapping;
    }

    /**
     * Returns {@code true} if this connector can connect to
     * its data source. More formally, returns {@code true} if this
     * connector can execute the validation query successfully.
     *
     * @param configDto - Connector config whose validity has to be tested.
     * @return {@code true} if this connector can connect to its external
     * data source.
     */
    @Override
    public boolean validateConnection(ConfigDto configDto) {
        RestConfig restConfig = getRestConfig(configDto.getConfig());
        Map<String, String> headers = new HashMap<>();
        if (restConfig.getHeaders() != null) {
            headers.putAll(restConfig.getHeaders(true));
            headers.put("Host", getHostname(restConfig.getUrl(true)));
        } else {
            headers.put("Host", getHostname(restConfig.getUrl(true)));
        }
        try {
            RestInvoker invoker = new RestInvoker();
            String url = UrlUtils.getAbsoluteURLFromBaseAndRelativeUrl(restConfig.getUrl(), GET_ALL_PRODUCTS.getUrl(), restConfig.getUrl());
            log.debug("[SalsifyConnector] Executing connection with url {}", url);
            log.debug("[SalsifyConnector] Executing connection with headers {}", headers);
            log.debug("[SalsifyConnector] Executing connection with authentication details {}", restConfig.getAuthenticationDetails());
            HttpUriRequest uriRequest = invoker.prepareConnection(restConfig.getAuthenticationDetails(), url, restConfig.getRequestType(true), restConfig.getBody(true), "", headers);
            String result = invoker.invokeRequest(uriRequest, getHttpClient());
            log.debug("[SalsifyConnector] Execute query result {}", result);
            return true;
        } catch (IOException | URISyntaxException | KonnectException e) {
            log.error("[SalsifyConnector] Error in connecting to client", e);
        } catch (Exception e) {
            log.error("[SalsifyConnector] Error in sending request", e);
        }
        return false;
    }

    /**
     * Returns a {@code String} which is the hostname of the URL.
     *
     * @param url - URL from which hostname needs to be extracted.
     * @return {@code String} which is the hostname of the URL.
     */
    private String getHostname(String url) {
        if (StringUtils.isNotBlank(UrlUtils.getHostname(url))) {
            return UrlUtils.getHostname(url);
        } else {
            return url;
        }
    }

    /**
     * Executes a single query for this connector and returns response after
     * parsing it to {@code JSON}.
     *
     * <p>A query is an object of type {@link QueryInfoDto}.
     *
     * @param configDto Connector config which needs to be executed.
     * @param queryInfo The query which will be executed.
     * @return A {@code String} which is the JSON response of query execution.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    @Override
    public String execute(ConfigDto configDto, QueryInfoDto queryInfo) throws KonnectException {
        boolean isValidConnection = validateConnection(configDto);
        if (!isValidConnection) {
            throw new KonnectConnectionException("[SalsifyConnector] Error in connecting to client");
        }
        try {
            log.debug("Executing query for to get data");
            RestInvoker invoker = new RestInvoker();
            String result = executeAQuery(configDto, queryInfo, invoker, false, false);
            return result;
        } catch (KonnectQueryException e) {
            throw new KonnectQueryException("[SalsifyConnector] Error in executing query", e);
        } catch (KonnectException e) {
            throw e;
        } catch (Exception e) {
            throw new KonnectException("[SalsifyConnector] Error in sending request", e);
        }
    }

    /**
     * Executes a single query for this connector with a limit.
     * This is used to show a preview for the connector on the UI.
     * Response should be returned after parsing into {@code JSON} string.
     *
     * <p>A query is an object of type {@link QueryInfoDto}.
     *
     * @param configDto Connector config which needs to be executed.
     * @param queryInfo The query which will be executed.
     * @return A {@code QueryResponseDto} which is the JSON response of query execution.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    @Override
    public QueryResultDto executeWithLimit(ConfigDto configDto, QueryInfoDto queryInfo) throws KonnectException {
        String query = getQueryWithLimit(queryInfo.getQuery());
        queryInfo.setQuery(query);
        QueryResultDto queryResultDto = new QueryResultDto();
        queryResultDto.setQuery(query);
        RestInvoker invoker = new RestInvoker();
        queryResultDto.setResponse(executeAQuery(configDto, queryInfo, invoker, true, true));
        return queryResultDto;
    }

    /**
     * Executes a single query for this connector and returns response after
     * parsing it to {@code JSON}.
     *
     * @param configDto          Connector config which needs to be executed.
     * @param queryInfo          The query which will be executed.
     * @param invoker            RestInvoker object to execute the query.
     * @param getLimitedResults  boolean to get limited results.
     * @param validateConnection boolean to validate connection.
     * @return A {@code String} which is the JSON response of query execution.
     * @throws KonnectException if any exception or error occurs while connecting to
     */
    private String executeAQuery(ConfigDto configDto, QueryInfoDto queryInfo, RestInvoker invoker, boolean getLimitedResults, boolean validateConnection) throws KonnectException {

        if (validateConnection) {
            boolean isValidConnection = validateConnection(configDto);
            if (!isValidConnection) {
                throw new KonnectConnectionException("[SalsifyConnector] Error in connecting to client");
            }
        }

        try {
            log.debug("Executing query for to get data");
            SalsifyTotalResponseDao allResultsForAQuery = getAllResultsForAQuery(configDto, queryInfo, invoker, getLimitedResults);
            return GsonUtils.getInstance().getStringFromObject(allResultsForAQuery);
        } catch (IOException | URISyntaxException e) {
            throw new KonnectQueryException("[SalsifyConnector] Error in executing query", e);
        } catch (KonnectException e) {
            throw e;
        } catch (Exception e) {
            throw new KonnectException("[SalsifyConnector] Error in sending request", e);
        }

    }

    /**
     * Executes a list of queries for this connector and returns response after
     * parsing it to {@code JSON}.
     *
     * <p>Each query is an object of type {@link QueryInfoDto}.
     *
     * @param configDto     Connector config which needs to be executed.
     * @param queryInfoList The {@code List} of queries which will be executed.
     * @return A {@code String} which is a single JSON response of all query executions.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    @Override
    public String execute(ConfigDto configDto, List<QueryInfoDto> queryInfoList) throws KonnectException {
        boolean isValidConnection = validateConnection(configDto);
        if (!isValidConnection) {
            throw new KonnectConnectionException("[SalsifyConnector] Error in connecting to client");
        }
        RestInvoker invoker = new RestInvoker();
        JsonObject queryResult = new JsonObject();

        for (QueryInfoDto queryInfo : queryInfoList) {
            String result = executeAQuery(configDto, queryInfo, invoker, false, false);
            JsonElement jsonElement = GsonUtils.getInstance().getObjectFromString(result, JsonElement.class);
            queryResult.add(queryInfo.getQueryName(), jsonElement);
        }
        return queryResult.toString();
    }

    /**
     * Gets the filter and page query parameters for the given Salsify request.
     *
     * @param salsifyRequestDao Salsify request object.
     * @return {@code Map} of query parameters.
     * @throws UnsupportedEncodingException if the encoding is not supported.
     */
    private Map<String, String> getFilterAndPageQueryParams(SalsifyRequestDao salsifyRequestDao) throws UnsupportedEncodingException {
        Map<String, String> requestQueryParams = new HashMap<>();
        if (StringUtils.isNotEmpty(salsifyRequestDao.getFilter()))
            requestQueryParams.put(SALSIFY_FILTER_QUERY_PARAM, salsifyRequestDao.getFilter());
        if (salsifyRequestDao.getPage() >= 0) {
            requestQueryParams.put(SALSIFY_PAGE_QUERY_PARAM, String.valueOf(salsifyRequestDao.getPage()));
        }
        if (salsifyRequestDao.getPerPage() > 0) {
            requestQueryParams.put(SALSIFY_PER_PAGE_QUERY_PARAM, String.valueOf(salsifyRequestDao.getPerPage()));
        }
        return requestQueryParams;
    }

    /**
     * Gets all the results for a query.
     *
     * @param configDto         Config object which needs to be executed.
     * @param queryInfo         The query which will be executed.
     * @param invoker           RestInvoker object to execute the query.
     * @param getLimitedResults boolean to get limited results.
     * @return {@link SalsifyTotalResponseDao} which is the total response of the query.
     * @throws Exception if any exception or error occurs while connecting to
     *                   the external data source.
     */
    private SalsifyTotalResponseDao getAllResultsForAQuery(ConfigDto configDto, QueryInfoDto queryInfo, RestInvoker invoker, boolean getLimitedResults) throws Exception {
        RestConfig restConfig = getRestConfig(configDto.getConfig(), queryInfo);
        SalsifyTotalResponseDao salsifyTotalResponseDao = new SalsifyTotalResponseDao();

        try {

            SalsifyRequestDao salsifyRequestDao = GsonUtils.getInstance().getObjectFromString(queryInfo.getQuery(), SalsifyRequestDao.class);
            Map<String, String> requestQueryParams = getFilterAndPageQueryParams(salsifyRequestDao);
            salsifyTotalResponseDao = getAllResultsForRequest(restConfig, queryInfo, invoker, getLimitedResults, salsifyRequestDao, requestQueryParams, GsonUtils.getInstance().getGson(), getHttpClient());
            return salsifyTotalResponseDao;
        } catch (IOException e) {
            throw new KonnectQueryException("[SalsifyConnector] Error in connecting to client", e);
        } catch (KonnectException e) {
            throw e;
        } catch (Exception e) {
            throw new KonnectException("[SalsifyConnector] Error in sending request", e);
        } finally {
            clearRestConfigOfResourceUrl(restConfig);
        }
    }

    /**
     * Returns list of templates shipped with the connector.
     *
     * @return a {@code List} of {@link TemplateDto} which list of templates.
     */
    @Override
    public List<TemplateDto> getTemplates() {
        List<TemplateDto> templates = new ArrayList<>();
        ClassLoader classLoader = this.getClass().getClassLoader();
        for (int i = 0; i < SALSIFY_TEMPLATE_PATH.length; i++) {
            try {
                InputStream inStream = classLoader.getResourceAsStream("templates/" + SALSIFY_TEMPLATE_PATH[i]);
                templates.add(new TemplateDto(SALSIFY_TEMPLATE_PATH[i], new String(IOUtils.toByteArray(inStream), StandardCharsets.UTF_8)));
            } catch (Exception e) {
                log.error("[SalsifyConnector] error in reading template file", e);
            }
        }
        return templates;
    }

    /**
     * Returns a {@link RestConfig} object for the Config to be used to execute
     * a query.
     *
     * @param config       Config object which needs to be executed.
     * @param queryInfoDto The query which will be executed.
     * @return {@link RestConfig} which is the Config to be used to execute a query.
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
     */
    public void clearRestConfigOfResourceUrl(RestConfig restConfig) throws KonnectException {
        if (restConfig != null) {
            restConfig.setCurrentResource(StringUtils.EMPTY);
        }
    }

    /**
     * Gets all the results for a request.
     *
     * @param restConfig         Config object which needs to be executed.
     * @param queryInfo          The query which will be executed.
     * @param invoker            RestInvoker object to execute the query.
     * @param getLimitedResults  boolean to get limited results.
     * @param salsifyRequestDao  Salsify request object.
     * @param requestQueryParams {@code Map} of query parameters.
     * @param gson               Gson object to convert object to string.
     * @param httpClient         {@link CloseableHttpClient} object to execute the query.
     * @return {@link SalsifyTotalResponseDao} which is the total response of the query.
     * @throws Exception if any exception or error occurs while connecting to
     *                   the external data source.
     */
    private SalsifyTotalResponseDao getAllResultsForRequest(RestConfig restConfig, QueryInfoDto queryInfo, RestInvoker invoker, boolean getLimitedResults, SalsifyRequestDao salsifyRequestDao, Map<String, String> requestQueryParams, Gson gson, org.apache.http.client.HttpClient httpClient) throws KonnectException {

        SalsifyTotalResponseDao salsifyTotalResponseDao = new SalsifyTotalResponseDao();
        try {
            Map<String, String> headers = new HashMap<>();
            if (restConfig.getHeaders() != null) {
                headers.putAll(restConfig.getHeaders());
                headers.put("Host", UrlUtils.getHostname(restConfig.getUrl()));
            } else {
                headers.put("Host", UrlUtils.getHostname(restConfig.getUrl()));
            }
            String urlWithResource = restConfig.getUrl();
            String urlWithQueryParams = "";
            urlWithQueryParams = UrlUtils.appendUri(urlWithResource, requestQueryParams);
            log.debug("[SalsifyConnector] Executing connection with url {}", urlWithQueryParams);
            log.debug("[SalsifyConnector] Executing connection with headers {}", headers);
            log.debug("[SalsifyConnector] Executing connection with authentication details {}", restConfig.getAuthenticationDetails());
            HttpUriRequest uriRequest = invoker.prepareConnection(restConfig.getAuthenticationDetails(), urlWithQueryParams, restConfig.getRequestType(), restConfig.getBody(), "", headers);
            String subQueryResult = invoker.invokeRequest(uriRequest, httpClient);
            log.debug("[SalsifyConnector] query result {}", subQueryResult);
            SalsifyResponseDao salsifyResponseDao = gson.fromJson(subQueryResult, SalsifyResponseDao.class);
            if (salsifyResponseDao.getMetadata() != null) {
                salsifyTotalResponseDao.setTotalRecords(salsifyResponseDao.getMetadata().getTotalEntries());
                salsifyTotalResponseDao.getData().addAll(salsifyResponseDao.getData());
                if (getLimitedResults != true) {
                    while (salsifyResponseDao.getMetadata().getCursor() != null) {
                        requestQueryParams = getFilterAndCursorQueryParams(salsifyRequestDao, salsifyResponseDao.getMetadata().getCursor());
                        urlWithQueryParams = UrlUtils.appendUri(urlWithResource, requestQueryParams);
                        uriRequest = invoker.prepareConnection(restConfig.getAuthenticationDetails(), urlWithQueryParams, restConfig.getRequestType(), restConfig.getBody(), queryInfo.getQuery(), restConfig.getHeaders());
                        subQueryResult = invoker.invokeRequest(uriRequest, httpClient);
                        salsifyResponseDao = gson.fromJson(subQueryResult, SalsifyResponseDao.class);
                        salsifyTotalResponseDao.getData().addAll(salsifyResponseDao.getData());
                    }
                }
            }
            return salsifyTotalResponseDao;
        } catch (IOException e) {
            throw new KonnectQueryException("[SalsifyConnector] Error in connecting to client", e);
        } catch (KonnectException e) {
            throw e;
        } catch (Exception e) {
            throw new KonnectException("[SalsifyConnector] Error in sending request", e);
        }
    }

    /**
     * Gets the filter and cursor query parameters for the given Salsify request.
     *
     * @param salsifyRequestDao Salsify request object.
     * @param cursor            Cursor value.
     * @return {@code Map} of query parameters.
     * @throws UnsupportedEncodingException if the encoding is not supported.
     */
    private Map<String, String> getFilterAndCursorQueryParams(SalsifyRequestDao salsifyRequestDao, String cursor) throws UnsupportedEncodingException {
        Map<String, String> requestQueryParams = new HashMap<>();
        if (StringUtils.isNotEmpty(salsifyRequestDao.getFilter()))
            requestQueryParams.put(SALSIFY_FILTER_QUERY_PARAM, salsifyRequestDao.getFilter());

        if (StringUtils.isNotEmpty(cursor)) {
            requestQueryParams.put(SALSIFY_CURSOR_QUERY_PARAM, cursor);
        }
        return requestQueryParams;
    }

}