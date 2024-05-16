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
package com.adobe.guides.konnect.definitions.core.util;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ResultSetHandler implementation that gives a {@code Map} containing the
 * metadata of {@code ResultSet} and the resultant rows.
 *
 * <p>The metadata is a {@code List} of column names of the table. The ResultSet
 * is converted to a {@code List} of {@code Map}.
 *
 * <p>The resulting map has two keys <tt>metadata</tt> containing column name, and
 * <tt>data</tt> containing the resultant rows.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class CustomResultSetHandler implements ResultSetHandler<Map<String, List<Object>>> {

    private final RowProcessor convert;

    /**
     * Default constructor
     */
    public CustomResultSetHandler() {
        this(new BasicRowProcessor());
    }

    /**
     * Constructor with a custom {@link RowProcessor}
     *
     * @param convert a {@code RowProcessor} object
     */
    public CustomResultSetHandler(RowProcessor convert) {
        this.convert = convert;
    }

    /**
     * Handles the {@code ResultSet} and returns a {@code Map} containing the
     * metadata of {@code ResultSet} and the resultant rows as data.
     *
     * @param rs a {@code ResultSet} object
     * @return a {@code Map} containing the metadata of {@code ResultSet} and the resultant rows.
     * @throws SQLException if a database access error occurs
     */
    @Override
    public Map<String, List<Object>> handle(ResultSet rs) throws SQLException {
        List<Object> resultRows = new ArrayList<>();
        while (rs.next()) {
            resultRows.add(this.handleRow(rs));
        }

        List<Object> columnNames = new ArrayList<>();
        ResultSetMetaData rsMeta = rs.getMetaData();
        int cols = rsMeta.getColumnCount();
        for (int i = 1; i <= cols; ++i) {
            String columnName = rsMeta.getColumnLabel(i);
            if (null == columnName || 0 == columnName.length()) {
                columnName = rsMeta.getColumnName(i);
            }
            columnNames.add(columnName);
        }

        Map<String, List<Object>> result = new HashMap<>();
        result.put("metadata", columnNames);
        result.put("data", resultRows);
        return result;
    }

    /**
     * Handles the row of the {@code ResultSet} and returns a {@code Map}.
     *
     * @param rs a {@code ResultSet} object
     * @return a {@code Map} containing the column name and value.
     * @throws SQLException if a database access error occurs.
     */
    protected Map<String, Object> handleRow(ResultSet rs) throws SQLException {
        return this.convert.toMap(rs);
    }
}
