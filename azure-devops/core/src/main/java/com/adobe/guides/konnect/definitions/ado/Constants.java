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

/**
 * Constants to be used throughout the code.<p>
 *
 * @author Adobe
 * @since 1.0.0
 */
public class Constants {
    public static String ADO = "Azure DevOps";
    public static final String ADO_PAT_CONFIG = "Personal access token";
    public static final String ADO_PAT_CONFIG_INFO = "A personal access token authentication contains the security credentials for Azure DevOps as a token. A PAT identifies a user, the user's accessible organizations, and scopes of access.";
    public static String ADO_VALIDATION_QUERY = "System.Id";
    public static String ADO_DEFAULT_QUERY = "{\n" +
            "  \"project\": \"Guides\",\n" +
            "  \"query\":" +
            "\"Select [System.Id], [System.Title], [System.State] From WorkItems Where [System.WorkItemType] = 'Task' order by [Microsoft.VSTS.Common.Priority] asc, [System.CreatedDate] desc\"" +
            " \n" +
            "}";
    public static String ADO_DEFAULT_QUERY_BY_ID = "{\n" +
            "  \"project\": \"Guides\",\n" +
            "  \"query\":" +
            "\"1,2,5,22\"" +
            " \n" +
            "}";
    public static String ADO_DEFAULT_QUERY_BY_QUERY_ID = "{\n" +
            "  \"project\": \"Guides\",\n" +
            "  \"query\":" +
            "\"queryid12345\"" +
            " \n" +
            "}";
    public static String ADO_DEFAULT_QUERY_BY_QUERY = "{\n" +
            "  \"project\": \"Guides\",\n" +
            "  \"query\":" +
            "\"Select [System.Id], [System.Title], [System.State] From WorkItems Where [System.WorkItemType] = 'Task' order by [Microsoft.VSTS.Common.Priority] asc, [System.CreatedDate] desc\"" +
            " \n" +
            "}";
    public static final String ADO_DESC = "AEM Guides Azure DevOps data source connector to query and visualize the data.";
    public static final String ADO_PAT_INFO = "Personal access token for authentication";
    public static final String ADO_ORG_INFO = "Organization name";
    public static final String ADO_LOGO_SVG_PATH = "logo/ado_logo.svg";
    public static final String[] ADO_TEMPLATES_PATH = new String[]{"ado-ordered-list.vm", "ado-unordered-list.vm", "ado-table.vm", "ado-table-url.vm"};
}
