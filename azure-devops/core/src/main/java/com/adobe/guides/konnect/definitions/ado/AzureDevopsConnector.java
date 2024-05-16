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
package com.adobe.guides.konnect.definitions.ado;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.azd.enums.QueryExpand;
import org.azd.enums.WorkItemErrorPolicy;
import org.azd.enums.WorkItemExpand;
import org.azd.exceptions.AzDException;
import org.azd.workitemtracking.WorkItemTrackingApi;
import org.azd.workitemtracking.types.QueryHierarchyItem;
import org.azd.workitemtracking.types.WorkItem;
import org.azd.workitemtracking.types.WorkItemFieldReference;
import org.azd.workitemtracking.types.WorkItemList;
import org.azd.workitemtracking.types.WorkItemQueryResult;
import org.azd.workitemtracking.types.WorkItemReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.adobe.guides.konnect.definitions.ado.Constants.ADO;
import static com.adobe.guides.konnect.definitions.ado.Constants.ADO_DEFAULT_QUERY;
import static com.adobe.guides.konnect.definitions.ado.Constants.ADO_DEFAULT_QUERY_BY_ID;
import static com.adobe.guides.konnect.definitions.ado.Constants.ADO_DEFAULT_QUERY_BY_QUERY;
import static com.adobe.guides.konnect.definitions.ado.Constants.ADO_DEFAULT_QUERY_BY_QUERY_ID;
import static com.adobe.guides.konnect.definitions.ado.Constants.ADO_DESC;
import static com.adobe.guides.konnect.definitions.ado.Constants.ADO_LOGO_SVG_PATH;
import static com.adobe.guides.konnect.definitions.ado.Constants.ADO_TEMPLATES_PATH;
import static com.adobe.guides.konnect.definitions.ado.Constants.ADO_VALIDATION_QUERY;
import static com.adobe.guides.konnect.definitions.ado.ResourceEnum.BY_ID;
import static com.adobe.guides.konnect.definitions.ado.ResourceEnum.BY_QUERY;
import static com.adobe.guides.konnect.definitions.ado.ResourceEnum.BY_QUERY_ID;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ADOBE_SYSTEMS;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.PROJECT_MANAGEMENT;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.RESOURCE_ID;

/**
 * Azure DevOps Boards connector.<p>
 * This connector will connect to Azure DevOps configured via a Personal
 * Access Token (PAT). The user can query the work items using the
 * work item ID, WIQL query, or WIQL query ID.<p>
 *
 * @author Adobe
 * @since 1.0.0
 */
@Component(service = Connector.class)
public class AzureDevopsConnector extends RestConnector {

    private static final Logger log = LoggerFactory.getLogger(AzureDevopsConnector.class);
    private final transient Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @Reference
    private AzureDevopsUtility azureDevopsUtility;

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
     * Returns the query used to display as a sample in the UI.
     *
     * @return a {@code String} which is the sample query string.
     */
    @Override
    public String getSampleQuery() {
        return ADO_DEFAULT_QUERY;
    }

    /**
     * Returns the description used to display in the UI.
     *
     * @return a {@code String} which is the description string.
     */
    @Override
    public String getDescription() {
        return ADO_DESC;
    }

    /**
     * Returns the query used to validate if the connector is connected.
     *
     * @return a {@code String} which is the validation query string.
     */
    @Override
    public String getValidationQuery() {
        return ADO_VALIDATION_QUERY;
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
        return null;
    }

    /**
     * Returns a {@code String} query with limit added to it. This query is used
     * to show a preview for the connector on the UI.
     * <p> In case of this connector, it is same as original query.
     *
     * @return {@code String} query with limit added to it.
     */
    @Override
    public String getQueryWithLimit(String query) {
        return query;
    }

    /**
     * Returns a {@link HttpClient} object to be used to execute the HTTP request.
     * <p>
     * In case of this connector, it is not required.
     *
     * @return {@link HttpClient} object to be used to execute the HTTP request.
     */
    @Override
    public HttpClient getHttpClient() {
        return null;
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
        List<RestResourceDao> resourceDaoList = new ArrayList<>();
        resourceDaoList.add(new RestResourceDao().setName(BY_ID.toString()).setUrl("_apis/wit/workitems").setSampleQuery(ADO_DEFAULT_QUERY_BY_ID).setDefault(true).setEnabled(true));
        resourceDaoList.add(new RestResourceDao().setName(BY_QUERY.toString()).setUrl("_apis/wit/wiql").setSampleQuery(ADO_DEFAULT_QUERY_BY_QUERY).setDefault(true).setEnabled(true));
        resourceDaoList.add(new RestResourceDao().setName(BY_QUERY_ID.toString()).setUrl("_apis/wit/wiql/{id}").setSampleQuery(ADO_DEFAULT_QUERY_BY_QUERY_ID).setDefault(true).setEnabled(true));
        return resourceDaoList;
    }

    /**
     * Returns the unique name for this connector.
     *
     * @return a {@code String} which is the name of this connector.
     */
    @Override
    public String getName() {
        return ADO;
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
        return PROJECT_MANAGEMENT;
    }

    /**
     * Returns the author name for this connector.
     *
     * @return a {@code String} which is the author name of this connector.
     */
    @Override
    public String getAuthor() {
        return ADOBE_SYSTEMS;
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
     * Returns an array containing all the classes implementing {@link Config} which
     * are supported by this Connector.
     *
     * @return an array containing the classes implementing {@link Config} and supported by
     * this connector
     * @see Arrays#asList(Object[])
     */
    @Override
    public Class[] getConfigClass() {
        return new Class[]{PersonalAccessTokenConfig.class};
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
        try {
            WorkItemTrackingApi connection = azureDevopsUtility.getConnection(configDto.getConfig(), "");
            connection.getWorkItemField(getValidationQuery());
            return true;
        } catch (AzDException e) {
            log.error("[AzureDevopsConnector] Error in connecting", e);
        } catch (Exception e) {
            log.error("[AzureDevopsConnector] Error in sending request", e);
        }
        return false;
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
            throw new KonnectConnectionException("[AzureDevopsConnector] Error in connecting to client");
        }

        String resourceId = queryInfo.getAdditionalResourceInfo().get(RESOURCE_ID);
        AzureDevopsQueryDto queryDto = azureDevopsUtility.getQueryDto(queryInfo.getQuery());
        WorkItemTrackingApi connection = azureDevopsUtility.getConnection(configDto.getConfig(), queryDto.getProject());
        String result = execute(connection, configDto, queryDto, resourceId, false);
        JsonObject jsonResult = new JsonObject();
        jsonResult.add("data", gson.fromJson(result, JsonElement.class));
        return jsonResult.toString();
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
            throw new KonnectConnectionException("[AzureDevopsConnector] Error in connecting to client");
        }

        JsonObject queryResult = new JsonObject();
        for (QueryInfoDto queryInfo : queryInfoList) {
            String resourceId = queryInfo.getAdditionalResourceInfo().get(RESOURCE_ID);
            AzureDevopsQueryDto queryDto;
            try {
                queryDto = azureDevopsUtility.getQueryDto(queryInfo.getQuery());
            } catch (KonnectQueryException e) {
                log.error("[AzureDevopsConnector] Query is not json ", e);
                continue;
            }
            WorkItemTrackingApi connection = azureDevopsUtility.getConnection(configDto.getConfig(), queryDto.getProject());
            String result = execute(connection, configDto, queryDto, resourceId, false);
            queryResult.add(queryInfo.getQueryName(), gson.fromJson(result, JsonElement.class));
        }
        return queryResult.toString();
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
            throw new KonnectConnectionException("[AzureDevopsConnector] Error in connecting to client");
        }
        String resourceId = queryInfo.getAdditionalResourceInfo().get(RESOURCE_ID);
        AzureDevopsQueryDto queryDto = azureDevopsUtility.getQueryDto(queryInfo.getQuery());
        WorkItemTrackingApi connection = azureDevopsUtility.getConnection(configDto.getConfig(), queryDto.getProject());
        String result = execute(connection, configDto, queryDto, resourceId, true);
        JsonObject jsonResult = new JsonObject();
        jsonResult.add("data", gson.fromJson(result, JsonElement.class));

        QueryResultDto queryResultDto = new QueryResultDto();
        queryResultDto.setQuery(queryInfo.getQuery());
        queryResultDto.setResponse(jsonResult.toString());
        return queryResultDto;
    }

    /**
     * Helper function to execute the query based on the type of resource.
     *
     * @param connection Connection object to connect to Azure DevOps.
     * @param configDto  Connector config which needs to be executed.
     * @param queryDto   The query which will be executed.
     * @param resourceId The resource ID to be used for the query.
     * @param limit      {@code true} if the query should be limited.
     * @return A {@code String} which is the JSON response of query execution.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    private String execute(WorkItemTrackingApi connection, ConfigDto configDto, AzureDevopsQueryDto queryDto, String resourceId, boolean limit) throws KonnectException {
        PersonalAccessTokenConfig patConfig = (PersonalAccessTokenConfig) configDto.getConfig();
        patConfig.setCurrentResource(resourceId);
        RestResourceDao resourceDao = patConfig.getCurrentResource();
        if (resourceDao == null) {
            throw new KonnectQueryException("[AzureDevopsConnector] Resource not found");
        }
        List<WorkItem> workItemList;
        switch (ResourceEnum.getEnum(resourceDao.getName())) {
            case BY_ID:
                workItemList = getById(connection, azureDevopsUtility.getIds(queryDto.getQuery()), new String[]{}, null, limit);
                break;
            case BY_QUERY:
                workItemList = getByQuery(connection, queryDto.getQuery(), limit);
                break;
            case BY_QUERY_ID:
                if (StringUtils.isBlank(queryDto.getProject())) {
                    throw new KonnectQueryException("[AzureDevopsConnector] Project should not be empty for this resource");
                }
                workItemList = getByQueryId(connection, queryDto.getQuery(), limit);
                break;
            default:
                throw new KonnectQueryException("[AzureDevopsConnector] Resource not found");
        }
        return workItemList.toString();
    }

    /**
     * Helper function to execute the query based on query ID.
     *
     * @param connection Connection object to connect to Azure DevOps.
     * @param queryId    The query ID to be used for the query.
     * @param limit      {@code true} if the query should be limited.
     * @return a {@code List} of {@link WorkItem} which list of work items from query.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    private List<WorkItem> getByQueryId(WorkItemTrackingApi connection, String queryId, boolean limit) throws KonnectException {
        try {
            QueryHierarchyItem queryHierarchyItem = connection.getQuery(queryId, 0, QueryExpand.ALL, false, false);
            if (StringUtils.isBlank(queryHierarchyItem.getWiql())) {
                throw new KonnectQueryException("[AzureDevopsConnector] Query is empty");
            }
            return getByQuery(connection, queryHierarchyItem.getWiql(), limit);
        } catch (AzDException e) {
            log.error("[AzureDevopsConnector] Error in executing get by query", e);
            throw new KonnectQueryException("[AzureDevopsConnector] Error in executing query", e);
        } catch (Exception e) {
            throw new KonnectException("[AzureDevopsConnector] Error in sending request", e);
        }
    }

    /**
     * Helper function to execute the query if it is in WIQL.
     *
     * @param connection Connection object to connect to Azure DevOps.
     * @param query      The query to be used for the query.
     * @param limit      {@code true} if the query should be limited.
     * @return a {@code List} of {@link WorkItem} which list of work items from query.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    private List<WorkItem> getByQuery(WorkItemTrackingApi connection, String query, boolean limit) throws KonnectException {
        try {
            WorkItemQueryResult result = connection.queryByWiql("", query);
            List<WorkItemReference> workItemReferenceList = result.getWorkItems();
            if (workItemReferenceList.size() == 0) {
                return new ArrayList<>();
            }
            int[] ids = new int[workItemReferenceList.size()];
            int i = 0;
            for (WorkItemReference workItemReference : workItemReferenceList) {
                ids[i] = workItemReference.getId();
                i++;
            }
            String asOf = result.getAsOf();
            List<WorkItemFieldReference> workItemFieldReferenceList = result.getColumns();
            if (workItemFieldReferenceList.size() == 0) {
                return new ArrayList<>();
            }
            String[] fields = new String[workItemFieldReferenceList.size()];
            i = 0;
            for (WorkItemFieldReference ref : workItemFieldReferenceList) {
                fields[i] = ref.getReferenceName();
                i++;
            }
            List<WorkItem> itemList = getById(connection, ids, fields, asOf, limit);
            return azureDevopsUtility.sort(itemList, ids);
        } catch (AzDException e) {
            log.error("[AzureDevopsConnector] Error in executing get by query", e);
            throw new KonnectQueryException("[AzureDevopsConnector] Error in executing query", e);
        } catch (Exception e) {
            throw new KonnectException("[AzureDevopsConnector] Error in sending request", e);
        }
    }

    /**
     * Helper function to execute the query if it is in WIQL.
     *
     * @param connection Connection object to connect to Azure DevOps.
     * @param ids        The work item ids to be used for the query.
     * @param fields     The fields to be used for the query.
     * @param asOf       The as of date to be used for the query.
     * @param limit      {@code true} if the query should be limited.
     * @return a {@code List} of {@link WorkItem} which list of work items from query.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    private List<WorkItem> getById(WorkItemTrackingApi connection, int[] ids, String[] fields, String asOf, boolean limit) throws KonnectException {
        try {
            if (limit && ids.length > getMaxNoRowsForPreviewQuery()) {
                ids = Arrays.copyOf(ids, getMaxNoRowsForPreviewQuery());
            }
            WorkItemList itemList;
            if (StringUtils.isBlank(asOf)) {
                asOf = null;
            }
            if (fields.length == 0) {
                itemList = connection.getWorkItems(ids, WorkItemExpand.FIELDS);
            } else {
                itemList = connection.getWorkItems(ids, WorkItemExpand.NONE, fields, asOf, WorkItemErrorPolicy.OMIT);
            }
            return itemList.getWorkItems();
        } catch (AzDException e) {
            log.error("[AzureDevopsConnector] Error in executing get by id", e);
            throw new KonnectQueryException("[AzureDevopsConnector] Error in executing query", e);
        } catch (Exception e) {
            throw new KonnectException("[AzureDevopsConnector] Error in sending request", e);
        }
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
            InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(ADO_LOGO_SVG_PATH);
            logoSvg = new String(IOUtils.toByteArray(inStream), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("[AzureDevopsConnector] Error in reading logo svg file", e);
        }
        return logoSvg;
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
        for (int i = 0; i < ADO_TEMPLATES_PATH.length; i++) {
            try {
                InputStream inStream = classLoader.getResourceAsStream("templates/" + ADO_TEMPLATES_PATH[i]);
                templates.add(new TemplateDto(ADO_TEMPLATES_PATH[i], new String(IOUtils.toByteArray(inStream), StandardCharsets.UTF_8)));
            } catch (Exception e) {
                log.error("[AzureDevopsConnector] error in reading template file", e);
            }
        }
        return templates;
    }
}
