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
import com.adobe.guides.konnect.definitions.core.exception.KonnectException;
import com.adobe.guides.konnect.definitions.core.models.template.TemplateDto;
import com.adobe.guides.konnect.definitions.core.query.QueryInfoDto;
import com.adobe.guides.konnect.definitions.core.query.QueryResultDto;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.DEFAULT_LIMIT_PREVIEW;

/**
 * An external data source connector.  The user of this interface
 * has precise control over how the system will connect to the
 * external data source. The user can control how it will be
 * queried and what authentication will be used.
 * <p>
 * The <tt>Connector</tt> interface places additional stipulations,
 * beyond the <tt>execute</tt> method. <tt>executeWithLimit</tt>,
 * <tt>validateConnection</tt>, <tt>getName</tt>,  <tt>getGroup</tt>,
 * <tt>getAuthor</tt>, and <tt>getConfigClass</tt> also need to be
 * implemented to be able to successfully enable the connector on the UI.
 * <p>
 * The <tt>Connector</tt> interface provides six default methods which
 * can be optionally implemented.  These default methods are used to
 * enable the connector through <tt>enabled</tt> and set default
 * configurations through <tt>getDefaultTemplatePath</tt>,
 * <tt>getLogoClassName</tt>, <tt>getLogoURL</tt> and <tt>getValidationQuery</tt>.
 * <p>
 * The <tt>Connector</tt> interface provides three methods to execute queries.
 * One of them executes and single query and the other is used to execute
 * a list of queries.
 * <p>
 * Attempting to execute when the external data source is not available or
 * the connection is invalid will throw an unchecked exception
 * <tt>KonnectRuntimeException</tt>. Since this exception is optional, the
 * implementation can decide whether to handle it or throw it.
 *
 * @author Adobe
 * @since 1.0.0
 */
public interface Connector {

    /**
     * Returns <tt>true</tt> if this connector is enabled.
     *
     * @return <tt>true</tt> if this connector is enabled.
     */
    default boolean enabled() {
        return false;
    }

    /**
     * Returns the default path set for the templates of this connector.
     *
     * @return a {@code String} which is the path of templates.
     */
    default String getDefaultTemplatePath() {
        return StringUtils.EMPTY;
    }

    /**
     * Returns list of templates shipped with the connector.
     *
     * @return a {@code List} of {@link TemplateDto} which list of templates.
     */
    default List<TemplateDto> getTemplates() {
        return new ArrayList<>();
    }

    /**
     * Returns the class name to be used as the logo of this connector.
     *
     * @return a {@code String} which is the class name of the logo.
     * @implSpec The default implementation returns an empty string "".
     * @implNote If the logo URL and logo class name both are present, the URL will be
     * picked to display the logo on the UI. Additionally, if the logo class name
     * is passed through the Configuration settings, it will be picked
     * over the logo class name set in the implementation.
     */
    default String getLogoClassName() {
        return StringUtils.EMPTY;
    }

    /**
     * Returns the URL to be used as the logo of this connector.
     *
     * @return a {@code String} which is the URL of the logo.
     * @implSpec The default implementation returns an empty string "".
     * @implNote If the logo URL and logo class name both are present, the URL will be
     * picked to display the logo on the UI. Additionally, if the logo URL
     * is passed through the Configuration settings, it will be picked
     * over the logo URL set in the implementation.
     */
    default String getLogoURL() {
        return StringUtils.EMPTY;
    }

    /**
     * Returns the SVG of the logo of the connector as a string.
     *
     * @return a {@code String} which is the SVG of the logo.
     */
    default String getLogoSvg() {
        return StringUtils.EMPTY;
    }

    /**
     * Returns the query used to validate this connector.
     *
     * @return a {@code String} which is the URL of the logo.
     * @implSpec The default implementation returns an empty string "".
     * @implNote If a validation query is not defined, the <tt>validateConnection</tt>
     * method should be able to validate the connection without this query.
     * Additionally, if the validation query is passed through the Configuration
     * settings, it will be picked over the query set in the implementation.
     */
    default String getValidationQuery() {
        return StringUtils.EMPTY;
    }

    /**
     * Returns the query used to display as a sample in the UI.
     *
     * @return a {@code String} which is the sample query string.
     * @implSpec The default implementation returns an empty string "".
     * @implNote If a sample query is not defined, no sample query
     * will be displayed in the UI insert query dialog
     */
    default String getSampleQuery() {
        return StringUtils.EMPTY;
    }

    /**
     * Returns the description used to display in the UI.
     *
     * @return a {@code String} which is the description string.
     */
    default String getDescription() {
        return StringUtils.EMPTY;
    }

    /**
     * Returns the maximum number of rows that will be queried/displayed
     * while showing the preview in the UI.
     *
     * @return a {@code Integer} which is the maximum number of rows in
     * preview in UI.
     */
    default Integer getMaxNoRowsForPreviewQuery() {
        return DEFAULT_LIMIT_PREVIEW;
    }

    /**
     * Returns <tt>true</tt> if this connector can connect to
     * its external data source. More formally, returns <tt>true</tt> if this
     * connector can execute the validation query successfully.
     *
     * @param configDto - Connector config whose validity has to be tested.
     * @return <tt>true</tt> if this connector can connect to its external
     * data source.
     */
    boolean validateConnection(ConfigDto configDto);

    /**
     * Executes a single query for this connector.
     *
     * <p>Connectors that support this operation should parse the response
     * from the data source and convert it into a <tt>JSON</tt> string if it's
     * not already a JSON result.
     * <p>A query is an object of type {@link QueryInfoDto}.
     *
     * @param configDto Connector config which needs to be executed.
     * @param queryInfo The query which will be executed.
     * @return A <tt>String</tt> which is the JSON response of query execution.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    String execute(ConfigDto configDto, QueryInfoDto queryInfo) throws KonnectException;

    /**
     * Executes a list of queries for this connector.
     *
     * <p>Connectors that support this operation should parse the response
     * from the data source and convert it into a <tt>JSON</tt> string if it's
     * not already a JSON result. The query name should be used to bind the result
     * into a single JSON object.
     * <p>Each query is an object of type {@link QueryInfoDto}.
     *
     * @param configDto     Connector config which needs to be executed.
     * @param queryInfoList The {@code List} of queries which will be executed.
     * @return A <tt>String</tt> which is a single JSON response of all query executions.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    String execute(ConfigDto configDto, List<QueryInfoDto> queryInfoList) throws KonnectException;

    /**
     * Executes a single query for this connector with a limiting query.
     * This is used to show a preview for the connector on the UI.
     *
     * <p>Connectors that support this operation should parse the response
     * from the data source and convert it into a <tt>JSON</tt> string if it's
     * not already a JSON result.
     * <p>A query is an object of type {@link QueryInfoDto}.
     *
     * @param configDto Connector config which needs to be executed.
     * @param queryInfo The query which will be executed.
     * @return A <tt>QueryResponseDto</tt> which is the JSON response of query execution.
     * @throws KonnectException if any exception or error occurs while connecting to
     *                          the external data source.
     */
    QueryResultDto executeWithLimit(ConfigDto configDto, QueryInfoDto queryInfo) throws KonnectException;

    /**
     * Returns the unique name for this connector.
     *
     * <p>This name is used to identify this connector on UI if its Configuration settings
     * do not specify any name.
     *
     * @return a {@code String} which is the name of this connector.
     */
    String getName();

    /**
     * Returns the group name for this connector.
     *
     * <p>This group name is used to club multiple connectors into a logical grouping.
     *
     * @return a {@code String} which is the group name of this connector.
     */
    String getGroup();

    /**
     * Returns the author name for this connector.
     *
     * @return a {@code String} which is the author name of this connector.
     */
    String getAuthor();

    /**
     * Returns an array containing all the classes implementing {@link Config} which
     * are supported by this Connector.
     *
     * @return an array containing the classes implementing {@link Config} and supported by
     * this connector
     * @see Arrays#asList(Object[])
     */
    Class[] getConfigClass();
}
