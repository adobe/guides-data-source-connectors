package com.adobe.guides.konnect.definitions.akeneo.utils;

import com.adobe.guides.konnect.definitions.core.exception.KonnectRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class for the URL.<p>
 * This class provides utility methods for working with URLs.
 *
 * @author Adobe
 * @since 1.0.0
 */
@Component(service = URLUtility.class)
public class URLUtility {

    private static final Logger log = LoggerFactory.getLogger(URLUtility.class);

    /**
     * Returns true if the query string contains the given key.
     *
     * @param source the query string
     * @param key    the key to search for
     * @return true if the query string contains the given key
     */
    public boolean doesQueryHaveQueryParameter(
            String source, String key) {

        HashMap<String, String> data = new HashMap<String, String>();

        final String[] arrParameters = source.split("&");
        for (final String tempParameterString : arrParameters) {

            final String[] arrTempParameter = tempParameterString
                    .split("=");
            final String queryParam = arrTempParameter[0];
            final String parameterKey = queryParam;
            if (key.equalsIgnoreCase(parameterKey)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the query parameters from the given query string.
     *
     * @param source the query string
     * @return the query parameters
     */
    public Map<String, String> getQueryParams(String source) {
        Map<String, String> data = new LinkedHashMap<String, String>();

        final String[] arrParameters = source.split("&");
        for (final String tempParameterString : arrParameters) {

            final String[] arrTempParameter = tempParameterString
                    .split("=");
            String name = StringUtils.isBlank(arrTempParameter[0]) ? "" : arrTempParameter[0];
            data.put(name, tempParameterString);
        }

        return data;
    }

    /**
     * Replaces the value of the given key in the query string.
     *
     * @param query the query string
     * @param key   the key to search for
     * @param value the value to replace
     * @return the updated query string
     */
    public String replaceQueryParamWithValue(String query, String key, String value) {
        try {
            String queryParamRegex = createQueryParamRegex(key);
            String replacementString = createReplacementString(key, value);
            if (doesQueryHaveQueryParameter(query, key)) {
                query = query.replaceFirst(queryParamRegex, replacementString);
            }
        } catch (Exception e) {
            throw new KonnectRuntimeException("[RestConnector] Error in replacing query param", e);
        }
        return query;
    }

    /**
     * Creates a regex pattern for the given key.
     *
     * @param key the key
     * @return the regex pattern
     */
    public String createQueryParamRegex(String key) {
        return "\\b" + key + "=.*?(&|$)";
    }

    /**
     * Creates a query param for the given key and value.
     *
     * @param key   the key
     * @param value the value
     * @return the query param
     */
    public String createReplacementString(String key, String value) {
        return key + "=" + value;
    }

    /**
     * Returns the query string by using values of the map.
     *
     * @param queryParams the query parameters
     * @return the query string
     */
    public String buildQuery(Map<String, String> queryParams) {
        String query = "";
        if (queryParams == null) {
            return query;
        }
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            query = query + "&" + entry.getValue();
        }
        return StringUtils.strip(query, "&");
    }


}
