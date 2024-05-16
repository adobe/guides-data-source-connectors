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
import com.adobe.guides.konnect.definitions.core.exception.KonnectQueryException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.azd.connection.Connection;
import org.azd.workitemtracking.WorkItemTrackingApi;
import org.azd.workitemtracking.types.WorkItem;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for the Azure DevOps Connector.<p>
 * This class provides utility methods for the Azure DevOps connector.
 *
 * @author Adobe
 * @since 1.0.0
 */
@Component(service = AzureDevopsUtility.class)
public class AzureDevopsUtility {

    /**
     * Returns an array integers from the comma separated query string.
     *
     * @param query the query string
     * @return an array of work item IDs
     * @throws KonnectQueryException if the query is in an incorrect format
     */
    public int[] getIds(String query) throws KonnectQueryException {
        try {
            String idString[] = query.split(",");
            int ids[] = new int[idString.length];
            for (int i = 0; i < idString.length; i++) {
                ids[i] = Integer.parseInt(idString[i]);
            }
            return ids;
        } catch (NumberFormatException e) {
            throw new KonnectQueryException("[AzureDevopsConnector] Incorrect query format", e);
        }
    }

    /**
     * Sorts the work items based on the IDs list.
     *
     * @param itemList the list of work items
     * @param ids      the work item IDs
     * @return a sorted list of work items
     */
    public List<WorkItem> sort(List<WorkItem> itemList, int[] ids) {
        List<WorkItem> sortedList = new ArrayList<>();
        Map<Integer, WorkItem> workItemMap = new HashMap<>();
        for (WorkItem workItem : itemList) {
            workItemMap.put(workItem.getId(), workItem);
        }
        for (int id : ids) {
            if (workItemMap.containsKey(id)) {
                sortedList.add(workItemMap.get(id));
            }
        }
        return sortedList;
    }

    /**
     * Returns the Azure DevOps query DTO from the query JSON.
     *
     * @param queryJson the query JSON
     * @return the Azure DevOps query DTO
     * @throws KonnectQueryException if the query is in an incorrect format
     */
    public AzureDevopsQueryDto getQueryDto(String queryJson) throws KonnectQueryException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(queryJson, AzureDevopsQueryDto.class);
        } catch (JsonProcessingException e) {
            throw new KonnectQueryException("[AzureDevopsConnector] Incorrect query format", e);
        }
    }

    /**
     * Creates the connector using the config object and project name.
     *
     * @param config  the connector config
     * @param project the project name
     * @return the {@link WorkItemTrackingApi} connection object.
     */
    public WorkItemTrackingApi getConnection(Config config, String project) {
        PersonalAccessTokenConfig patConfig = (PersonalAccessTokenConfig) config;
        Connection connection;
        if (StringUtils.isBlank(project)) {
            connection = new Connection(patConfig.getOrganization(), "", patConfig.getToken());
        } else {
            connection = new Connection(patConfig.getOrganization(), project, patConfig.getToken());
        }
        return new WorkItemTrackingApi(connection);
    }
}

