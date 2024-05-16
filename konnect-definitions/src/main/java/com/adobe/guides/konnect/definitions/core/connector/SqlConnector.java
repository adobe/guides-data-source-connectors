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
import com.adobe.guides.konnect.definitions.core.exception.KonnectConnectionException;
import com.adobe.guides.konnect.definitions.core.exception.KonnectException;
import com.adobe.guides.konnect.definitions.core.exception.KonnectQueryException;
import com.adobe.guides.konnect.definitions.core.query.QueryInfoDto;
import com.adobe.guides.konnect.definitions.core.query.QueryResultDto;
import com.adobe.guides.konnect.definitions.core.util.CustomResultSetHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.ADOBE_SYSTEMS;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.SEMICOLON;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.SQL_DB;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.SQL_DESC;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.SQL_LIMIT_PARAMS;

/**
 * This class provides a skeletal implementation of the {@link Connector}
 * interface to minimize the effort required to implement this interface
 * for the SQL data source connector.  For a REST-based connector, {@link RestConnector}
 * should be used. For a NoSQL data source connector, {@link NoSqlConnector}
 * should be used.
 *
 * <p>To implement a SQL connector, the programmer needs only to extend
 * this class and provide implementations for the {@link #getName()},
 * {@link #getConfigClass()} and {@link #getConnection(Config)} methods.
 *
 * <p>The documentation for each non-abstract method in this class describes its
 * implementation in detail.  Each of these methods may be overridden if the
 * Connector being implemented admits a more efficient implementation.
 *
 * @author Adobe
 * @since 1.0.0
 */
public abstract class SqlConnector implements Connector {

    /**
     * Gson object to be used for JSON serialization.
     */
    private final transient Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * Logger object for logs.
     */
    private static final Logger log = LoggerFactory.getLogger(SqlConnector.class);

    /**
     * Returns a {@link Connection} object for connecting to the data source
     *
     * @param config The {@link Config} object which is used to fetch the connection
     * @return {@link Connection} which is a valid connection to the SQL database
     * @throws SQLException if an exception occurs while fetching the connection
     */
    public abstract Connection getConnection(Config config) throws SQLException;

    /**
     * Returns a {@code String} query with limit added to it
     *
     * @param query {@code String} query to which limit needs to be added.
     * @return {@code String} query with limit added to it
     */
    public abstract String getQueryWithLimit(String query);

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
        return SQL_DB;
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation connects to a SQL database using the connector configs
     * passed in the {@link ConfigDto} object.
     */
    @Override
    public boolean validateConnection(ConfigDto configDto) {
        Connection connection = null;
        try {
            connection = getConnection(configDto.getConfig());
            if (connection != null) {
                QueryRunner run = new QueryRunner();
                CustomResultSetHandler handler = new CustomResultSetHandler();
                Map<String, List<Object>> queryResult = run.query(connection, getValidationQuery(), handler);
                if (!queryResult.isEmpty()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            log.error("[SqlConnector] Error in connecting to driver", e);
        } finally {
            closeConnection(connection);
        }
        return false;
    }

    /**
     * Executes a list of queries on a SQL database.
     *
     * <p>This implementation connects to a SQL database using the connector config
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
        Connection connection = null;
        try {
            connection = getConnection(configDto.getConfig());
        } catch (SQLException e) {
            closeConnection(connection);
            throw new KonnectConnectionException("[SqlConnector] Error in connecting to driver", e);
        }

        try {
            QueryRunner run = new QueryRunner();
            CustomResultSetHandler handler = new CustomResultSetHandler();
            JsonObject queryResult = new JsonObject();
            for (QueryInfoDto queryInfo : queryInfoList) {
                Map<String, List<Object>> subQueryResult = run.query(connection, trimQuery(queryInfo.getQuery()), handler);
                JsonElement jsonElement = gson.fromJson(gson.toJson(subQueryResult), JsonElement.class);
                queryResult.add(queryInfo.getQueryName(), jsonElement);
            }
            return queryResult.toString();
        } catch (SQLException e) {
            throw new KonnectQueryException("[SqlConnector] Error in executing query", e);
        } catch (Exception e) {
            throw new KonnectException("[SqlConnector] Error in executing query", e);
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * Executes a query on a SQL database.
     *
     * <p>This implementation connects to a SQL database using the connector config
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
        Connection connection = null;
        try {
            connection = getConnection(configDto.getConfig());
        } catch (SQLException e) {
            closeConnection(connection);
            throw new KonnectConnectionException("[SqlConnector] Error in connecting to driver", e);
        }
        try {
            QueryRunner run = new QueryRunner();
            CustomResultSetHandler handler = new CustomResultSetHandler();
            Map<String, List<Object>> queryResult = run.query(connection, trimQuery(queryInfo.getQuery()), handler);
            return gson.toJson(queryResult);
        } catch (SQLException e) {
            throw new KonnectQueryException("[SqlConnector] Error in executing query", e);
        } catch (Exception e) {
            throw new KonnectException("[SqlConnector] Error in executing query", e);
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * Executes a query with limits on a SQL database.
     *
     * <p>This implementation connects to a SQL database using the connector config
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
        String query = getQueryWithLimit(trimQuery(queryInfo.getQuery()));
        queryInfo.setQuery(query);
        QueryResultDto queryResultDto = new QueryResultDto();
        queryResultDto.setQuery(query);
        queryResultDto.setResponse(execute(configDto, queryInfo));
        return queryResultDto;
    }

    /**
     * Helper function to trim query.
     *
     * <p>This function removed any leading and trailing whitespace. Then it
     * removes and trailing semicolon from the query.
     *
     * @param query String query to trim.
     * @return A {@code String} which is the trimmed query.
     */
    private String trimQuery(String query) {
        return StringUtils.stripEnd(StringUtils.trim(query), SEMICOLON);
    }

    /**
     * Returns the default driver for the SQL connector.
     *
     * @return A <tt>String</tt> which is the default driver for the SQL
     * connector.
     */
    public String getDefaultDriver() {
        return StringUtils.EMPTY;
    }

    /**
     * Returns the default validation query for the SQL connector.
     *
     * @return A {@code String} which is the default validation query for the SQL
     * connector.
     */
    public String getDefaultConnectionString() {
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return String.format(SQL_DESC, getName());
    }

    /**
     * Closes the connection to the SQL database.
     *
     * @param connection The {@link Connection} object to be closed.
     */
    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed())
                    connection.close();
            } catch (SQLException e) {
                log.error("[SqlConnector] Error in closing connection", e);
            }
        }
    }

    /**
     * Returns the query with the maximum number of rows that can be
     * fetched for preview.
     *
     * @param query The query to be stripped.
     * @return An {@code String} which is the query with the maximum
     * number of rows that can be fetched for preview.
     */
    protected String getStrippedQueryWithLimit(String query) {
        query = StringUtils.trim(StringUtils.stripEnd(StringUtils.trim(query), SEMICOLON));
        if (!StringUtils.containsIgnoreCase(query, SQL_LIMIT_PARAMS)) {
            return query + " " + SQL_LIMIT_PARAMS + " " + getMaxNoRowsForPreviewQuery();
        }

        int index = StringUtils.indexOfIgnoreCase(query, SQL_LIMIT_PARAMS);
        if (index == 0) {
            return query;
        }
        String queryBeforeLimit = StringUtils.trim(StringUtils.substring(query, 0, index));
        String limit = StringUtils.trim(StringUtils.substring(query, index + SQL_LIMIT_PARAMS.length()));
        if (StringUtils.isNumeric(limit)) {
            int limitNum = Integer.parseInt(limit);
            if (limitNum > 0 && limitNum <= getMaxNoRowsForPreviewQuery()) {
                return query;
            } else {
                return queryBeforeLimit + " " + SQL_LIMIT_PARAMS + " " + getMaxNoRowsForPreviewQuery();
            }
        } else {
            return query;
        }
    }
}
