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
package com.adobe.guides.konnect.definitions.akeneo;

import com.adobe.guides.konnect.definitions.akeneo.dto.AkeneoEmebeddedItems;
import com.adobe.guides.konnect.definitions.akeneo.dto.AkeneoResponseDto;
import com.adobe.guides.konnect.definitions.akeneo.dto.AkeneoTokenResponseDto;
import com.adobe.guides.konnect.definitions.akeneo.dto.OuathAccessTokenRequestDto;
import com.adobe.guides.konnect.definitions.akeneo.utils.AkeneoUtils;
import com.adobe.guides.konnect.definitions.akeneo.utils.GsonUtils;
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
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adobe.guides.konnect.definitions.akeneo.ResourceEnum.GET_ALL_ATTRIBUTES;
import static com.adobe.guides.konnect.definitions.akeneo.ResourceEnum.GET_ALL_FAMILIES;
import static com.adobe.guides.konnect.definitions.akeneo.ResourceEnum.GET_ALL_PRODUCTS;
import static com.adobe.guides.konnect.definitions.akeneo.ResourceEnum.GET_ALL_PRODUCTS_UUID;
import static com.adobe.guides.konnect.definitions.akeneo.ResourceEnum.GET_A_PRODUCT_UUID;
import static com.adobe.guides.konnect.definitions.akeneo.ResourceEnum.GET_LIST_CATEGORIES;
import static com.adobe.guides.konnect.definitions.akeneo.ResourceEnum.GET_LIST_LOCALES;
import static com.adobe.guides.konnect.definitions.akeneo.ResourceEnum.GET_OAUTH_TOKEN;
import static com.adobe.guides.konnect.definitions.akeneo.ResourceEnum.GET_PRODUCT_BY_ID;
import static com.adobe.guides.konnect.definitions.akeneo.ResourceEnum.GET_SYSTEM_INFO;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.AKENEO;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.AKENEO_DEFAULT_QUERY;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.AKENEO_DESC;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.AKENEO_LOGO_SVG_PATH;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.AKENEO_TEMPLATE_PATH;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.AKENEO_VALIDATION_QUERY;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.APPLICATION_JSON;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.CONTENT_TYPE_HEADER;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.DEFAULT_QUERY_LIMIT;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.DEFAULT_TEMPLATE_PATH;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.PRODUCT_INFORMATION_MANAGEMENT;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.HTTP_METHOD_POST;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.RESOURCE_ID;

/**
 * Akeneo connector.<p>
 * <p>
 * This connector will connect to Akeneo configured via an
 * App username password authentication or a Bearer token.
 * <p>
 * The user can query via the resources shipped inside the connector.<p>
 *
 * @author Adobe
 * @since 1.0.0
 */
@Component(service = Connector.class)
public class AkeneoConnector extends RestConnector {

    private static final Logger log = LoggerFactory.getLogger(AkeneoConnector.class);

    @Reference
    private AkeneoUtils akeneoUtils;

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
     * Returns the SVG of the logo of the connector as a string.
     *
     * @return a {@code String} which is the SVG of the logo.
     */
    @Override
    public String getLogoSvg() {
        String logoSvg = "";
        try {
            InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(AKENEO_LOGO_SVG_PATH);
            logoSvg = new String(IOUtils.toByteArray(inStream), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("[AkeneoConnector] Error in reading logo svg file", e);
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
        return AKENEO_DEFAULT_QUERY;
    }

    /**
     * Returns the breather time interval in milliseconds.
     *
     * @return a {@code Long} which is the breather time interval in
     * milliseconds.
     */
    private Long getBreatherTimeInMillis() {
        return 15000L;
    }

    /**
     * Returns the description used to display in the UI.
     *
     * @return a {@code String} which is the description string.
     */
    @Override
    public String getDescription() {
        return AKENEO_DESC;
    }

    /**
     * Returns the query used to validate if the connector is connected.
     *
     * @return a {@code String} which is the validation query string.
     */
    @Override
    public String getValidationQuery() {
        return AKENEO_VALIDATION_QUERY;
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
        return new Class[]{AppAccessTokenConfig.class, BearerTokenRestConfig.class};
    }

    /**
     * Returns a {@link RestConfig} object for the Config to be used to execute
     * a query.
     * <p>
     * In case of this connector, it is not required.
     *
     * @param config - Config object which needs to be executed.
     * @return {@link RestConfig} which is the Config to be used to execute a query.
     */
    @Override
    public RestConfig getRestConfig(Config config) {
        return null;
    }

    /**
     * Returns a {@code String} query with limit added to it. This query is used
     * to show a preview for the connector on the UI.
     *
     * @return {@code String} query with limit added to it.
     */
    @Override
    public String getQueryWithLimit(String query) {
        query = akeneoUtils.replaceLimitWithDefaultLimit(query, getMaxNoRowsForPreviewQuery());
        return query;
    }

    /**
     * Returns the unique name for this connector.
     *
     * @return a {@code String} which is the name of this connector.
     */
    @Override
    public String getName() {
        return AKENEO;
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
        defaultUrlMapping.add(new RestResourceDao().setName(GET_PRODUCT_BY_ID.getValue()).setUrl(GET_PRODUCT_BY_ID.getUrl()).setSampleQuery(GET_PRODUCT_BY_ID.getSampleQuery()).setDefault(true).setEnabled(true));
        defaultUrlMapping.add(new RestResourceDao().setName(GET_ALL_PRODUCTS_UUID.getValue()).setUrl(GET_ALL_PRODUCTS_UUID.getUrl()).setSampleQuery(GET_ALL_PRODUCTS_UUID.getSampleQuery()).setDefault(true).setEnabled(true));
        defaultUrlMapping.add(new RestResourceDao().setName(GET_A_PRODUCT_UUID.getValue()).setUrl(GET_A_PRODUCT_UUID.getUrl()).setSampleQuery(GET_A_PRODUCT_UUID.getSampleQuery()).setDefault(true).setEnabled(true));
        defaultUrlMapping.add(new RestResourceDao().setName(GET_ALL_FAMILIES.getValue()).setUrl(GET_ALL_FAMILIES.getUrl()).setSampleQuery(GET_ALL_FAMILIES.getSampleQuery()).setDefault(true).setEnabled(true));
        defaultUrlMapping.add(new RestResourceDao().setName(GET_ALL_ATTRIBUTES.getValue()).setUrl(GET_ALL_ATTRIBUTES.getUrl()).setSampleQuery(GET_ALL_ATTRIBUTES.getSampleQuery()).setDefault(true).setEnabled(true));
        defaultUrlMapping.add(new RestResourceDao().setName(GET_LIST_CATEGORIES.getValue()).setUrl(GET_LIST_CATEGORIES.getUrl()).setSampleQuery(GET_LIST_CATEGORIES.getSampleQuery()).setDefault(true).setEnabled(true));
        defaultUrlMapping.add(new RestResourceDao().setName(GET_LIST_LOCALES.getValue()).setUrl(GET_LIST_LOCALES.getUrl()).setSampleQuery(GET_LIST_LOCALES.getSampleQuery()).setDefault(true).setEnabled(true));
        return defaultUrlMapping;
    }

    /**
     * Helper function to set the OAuth token to authenticate the client.
     * <p>
     * This function returns {@code true} if the token is set successfully.
     *
     * @return a {@code true} if the OAuth token is set successfully.
     */
    private boolean getOauthToken(ConfigDto configDto) {
        log.debug("[AkeneoConnector] getting access token ");
        RestConfig restConfigBase = getConfigForAkeneo(configDto.getConfig());
        AppAccessTokenConfig restConfig = null;
        if (restConfigBase instanceof AppAccessTokenConfig)
            restConfig = (AppAccessTokenConfig) restConfigBase;

        String url = UrlUtils.getAbsoluteURLFromBaseAndRelativeUrl(restConfig.getUrl(), GET_OAUTH_TOKEN.getUrl(), restConfig.getUrl());
        try {
            OuathAccessTokenRequestDto ouathAccessTokenRequestDto = new OuathAccessTokenRequestDto().setUsername(restConfig.getUsername()).setPassword(restConfig.getPassword());
            RestInvoker invoker = new RestInvoker();
            Map<String, String> headers = new HashMap<>();
            if (restConfig.getHeaders() != null) {
                headers.putAll(restConfig.getHeaders(true));
                headers.put(CONTENT_TYPE_HEADER, APPLICATION_JSON);
            } else {
                headers.put(CONTENT_TYPE_HEADER, APPLICATION_JSON);
            }
            String body = GsonUtils.getInstance().getStringFromObject(ouathAccessTokenRequestDto);
            HttpUriRequest uriRequest = invoker.prepareConnection(restConfig.getAuthenticationDetailsForOauth(), url, HTTP_METHOD_POST, body, "", headers);
            String tokenJson = invoker.invokeRequest(uriRequest, getHttpClient());
            AkeneoTokenResponseDto tokenResponseDto = GsonUtils.getInstance().getObjectFromString(tokenJson, AkeneoTokenResponseDto.class);
            restConfig.setToken(tokenResponseDto.getAccessToken());
            configDto.setConfig(restConfig);
            return true;
        } catch (IOException | URISyntaxException | KonnectException e) {
            log.error("[AkeneoConnector] Error in connecting to client", e);
        } catch (Exception e) {
            log.error("[AkeneoConnector] Error in sending request", e);
        }
        return false;
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
        log.debug("[AkeneoConnector] validateConnection connection");
        RestConfig restConfig = getConfigForAkeneo(configDto.getConfig());
        if (restConfig instanceof AppAccessTokenConfig) {
            if (!getOauthToken(configDto)) {
                return false;
            }
        }
        String url = UrlUtils.getAbsoluteURLFromBaseAndRelativeUrl(restConfig.getUrl(), GET_SYSTEM_INFO.getUrl(), restConfig.getUrl());
        try {
            RestInvoker invoker = new RestInvoker();
            HttpUriRequest uriRequest = invoker.prepareConnection(restConfig.getAuthenticationDetails(), url, restConfig.getRequestType(true), restConfig.getBody(true), "", restConfig.getHeaders(true));
            invoker.invokeRequest(uriRequest, getHttpClient());
            return true;
        } catch (IOException | URISyntaxException | KonnectException e) {
            log.error("[AkeneoConnector] Error in connecting to client", e);
        } catch (Exception e) {
            log.error("[AkeneoConnector] Error in sending request", e);
        }
        return false;
    }

    /**
     * Helper function to set default limits in a query.
     * <p>
     * This function returns the query with default limit set.
     *
     * @return a {@code String} which is the query with default limit set.
     */
    private String getQueryWithDefaultLimit(String query) {
        query = akeneoUtils.replaceLimitWithDefaultLimit(query, DEFAULT_QUERY_LIMIT);
        return query;
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
            throw new KonnectConnectionException("[AkeneoConnector] Error in connecting to client");
        }
        try {
            JsonObject queryResult = new JsonObject();
            for (QueryInfoDto queryInfo : queryInfoList) {
                String query = getQueryWithDefaultLimit(queryInfo.getQuery());
                queryInfo.setQuery(query);
                RestInvoker invoker = new RestInvoker();
                AkeneoResponseDto results = executeAndGetResultFromQuery(configDto, queryInfo, invoker, false);

                String subQueryResult = GsonUtils.getInstance().getStringFromObject(results);
                ;
                JsonElement jsonElement = GsonUtils.getInstance().getObjectFromString(subQueryResult, JsonElement.class);
                queryResult.add(queryInfo.getQueryName(), jsonElement);
            }
            return queryResult.toString();
        } catch (KonnectException e) {
            throw e;
        } catch (Exception e) {
            throw new KonnectException("[AkeneoConnector] Error in sending request", e);
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
            throw new KonnectConnectionException("[AkeneoConnector] Error in connecting to client");
        }
        String query = getQueryWithDefaultLimit(queryInfo.getQuery());
        queryInfo.setQuery(query);
        RestInvoker invoker = new RestInvoker();
        AkeneoResponseDto results = executeAndGetResultFromQuery(configDto, queryInfo, invoker, false);

        return GsonUtils.getInstance().getStringFromObject(results);
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
        boolean isValidConnection = validateConnection(configDto);
        if (!isValidConnection) {
            throw new KonnectConnectionException("[AkeneoConnector] Error in connecting to client");
        }
        String query = getQueryWithLimit(queryInfo.getQuery());
        queryInfo.setQuery(query);
        QueryResultDto queryResultDto = new QueryResultDto();
        queryResultDto.setQuery(query);
        RestInvoker invoker = new RestInvoker();
        AkeneoResponseDto results = executeAndGetResultFromQuery(configDto, queryInfo, invoker, true);

        queryResultDto.setResponse(GsonUtils.getInstance().getStringFromObject(results));
        return queryResultDto;
    }

    /**
     * Helper function to execute an Akeneo query and get the result
     * as a {@link AkeneoResponseDto} object.
     * <p>
     *
     * @param configDto      - Connector config which needs to be executed.
     * @param queryInfo      - The query which will be executed.
     * @param invoker        - {@link RestInvoker} object to execute the query.
     * @param skipPagination - {@code true} if pagination is to be skipped.
     * @return {@link AkeneoResponseDto} which is the response of the query.
     */
    private AkeneoResponseDto executeAndGetResultFromQuery(ConfigDto configDto, QueryInfoDto queryInfo, RestInvoker invoker, boolean skipPagination) throws KonnectException {
        RestConfig restConfig = null;
        AkeneoResponseDto allResponsesDto = new AkeneoResponseDto();
        allResponsesDto.setEmbedded(new AkeneoEmebeddedItems());
        String responseString = "";
        try {
            restConfig = getConfigForAkeneo(configDto.getConfig(), queryInfo);
            responseString = getResultFromUrl(restConfig.getUrl(), restConfig, queryInfo, invoker);
            AkeneoResponseDto responseDto = GsonUtils.getInstance().getObjectFromString(responseString, AkeneoResponseDto.class);
            allResponsesDto.getEmbedded().getItems().addAll(responseDto.getEmbedded().getItems());
            if (skipPagination)
                return allResponsesDto;
            while (responseDto.getLinks() != null && responseDto.getLinks().getNext() != null && StringUtils.isNotBlank(responseDto.getLinks().getNext().getHref())) {
                log.debug("[AkeneoConnector] fetching data for {}", responseDto.getLinks().getNext().getHref());
                addBreatherForAkeneo();
                responseString = getResultFromUrl(responseDto.getLinks().getNext().getHref(), restConfig, queryInfo, invoker);
                responseDto = GsonUtils.getInstance().getObjectFromString(responseString, AkeneoResponseDto.class);
                if (responseDto != null && responseDto.getEmbedded() != null && responseDto.getEmbedded().getItems() != null)
                    allResponsesDto.getEmbedded().getItems().addAll(responseDto.getEmbedded().getItems());
            }
            log.debug("[AkeneoConnector] returning all results ");
            return allResponsesDto;
        } catch (IOException | URISyntaxException e) {
            throw new KonnectQueryException("[AkeneoConnector] Error in connecting to client", e);
        } catch (KonnectException e) {
            throw e;
        } catch (Exception e) {
            throw new KonnectException("[AkeneoConnector] Error in sending request", e);
        } finally {
            clearRestConfigOfResourceUrl(restConfig);
        }
    }

    /**
     * Helper function which adds the breather time interval
     * in milliseconds.
     */
    private void addBreatherForAkeneo() {
        try {
            Thread.sleep(getBreatherTimeInMillis());
        } catch (InterruptedException e) {
            log.debug("Failed to sleep ");
        }
    }

    /**
     * Helper function to execute a query and get the result as a
     * {@code String}.
     *
     * @return a {@code String} which is the response of the query.
     */
    private String getResultFromUrl(String url, RestConfig restConfig, QueryInfoDto queryInfo, RestInvoker invoker) throws IOException, URISyntaxException, KonnectQueryException {
        HttpUriRequest uriRequest = null;
        log.debug("[AkeneoConnector] getting results for URL {} ", url);
        uriRequest = invoker.prepareConnection(restConfig.getAuthenticationDetails(), url, restConfig.getRequestType(), restConfig.getBody(), queryInfo.getQuery(), restConfig.getHeaders());
        String responseString = invoker.invokeRequest(uriRequest, getHttpClient());
        return responseString;
    }

    /**
     * Returns a {@link RestConfig} object for the Config to be used to execute
     * a query.
     *
     * @param config - Config object which needs to be executed.
     * @return {@link RestConfig} which is the Config to be used to execute a query.
     */
    private RestConfig getConfigForAkeneo(Config config) {
        try {
            return getConfigForAkeneo(config, null);
        } catch (Exception e) {
            log.error("[AkeneoConnector] Failed to get Remember me config from config ", e);
        }
        return null;
    }

    /**
     * Returns a {@link RestConfig} object for the Config to be used to execute
     * a query.
     *
     * @param config       - Config object which needs to be executed.
     * @param queryInfoDto - QueryInfoDto object which contains additional resource info.
     * @return {@link RestConfig} which is the Config to be used to execute a query.
     */
    private RestConfig getConfigForAkeneo(Config config, QueryInfoDto queryInfoDto) throws KonnectException {
        log.debug("Setting resource {} for connector ", queryInfoDto != null ? queryInfoDto.getAdditionalResourceInfo() : "");
        RestConfig appAccessTokenConfig = (RestConfig) config;
        if (appAccessTokenConfig != null && queryInfoDto != null && queryInfoDto.getAdditionalResourceInfo().containsKey(RESOURCE_ID) && StringUtils.isNotEmpty(queryInfoDto.getAdditionalResourceInfo().get(RESOURCE_ID))) {
            appAccessTokenConfig.setCurrentResource(queryInfoDto.getAdditionalResourceInfo().get(RESOURCE_ID));
        }
        return appAccessTokenConfig;
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
     * Returns list of templates shipped with the connector.
     *
     * @return a {@code List} of {@link TemplateDto} which list of templates.
     */
    @Override
    public List<TemplateDto> getTemplates() {
        List<TemplateDto> templates = new ArrayList<>();
        ClassLoader classLoader = this.getClass().getClassLoader();
        for (int i = 0; i < AKENEO_TEMPLATE_PATH.length; i++) {
            try {
                InputStream inStream = classLoader.getResourceAsStream("templates/" + AKENEO_TEMPLATE_PATH[i]);
                templates.add(new TemplateDto(AKENEO_TEMPLATE_PATH[i], new String(IOUtils.toByteArray(inStream), StandardCharsets.UTF_8)));
            } catch (Exception e) {
                log.error("[AzureDevopsConnector] error in reading template file", e);
            }
        }
        return templates;
    }
}