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

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.BACKWARD_SLASH;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.BACKWARD_SLASH_CHAR;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.DEFAULT_PROTOCOL;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.FORWARD_SLASH_CHAR;

/**
 * Utility class for URL operations.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class UrlUtils {

    private static final Logger log = LoggerFactory.getLogger(UrlUtils.class);

    /**
     * Returns the hostname from the URL.
     *
     * @param urlString the URL string from which the hostname is to be extracted.
     * @return {@code String} which is the hostname from the URL.
     */
    public static String getHostname(String urlString) {
        try {
            URL url = new URL(urlString);
            return url.getHost();
        } catch (Exception e) {
            log.debug("Failed to get hostname ", e);
        }
        return "";
    }

    /**
     * Returns the protocol from the URL.
     *
     * @param urlString the URL string from which the protocol is to be extracted.
     * @return {@code String} which is the protocol from the URL.
     */
    public static String getProtocol(String urlString) {
        try {
            URL url = new URL(urlString);
            return url.getProtocol();
        } catch (Exception e) {
            log.debug("Failed to get proptocol ", e);
        }
        return DEFAULT_PROTOCOL;
    }

    /**
     * Returns the port from the URL.
     *
     * @param urlString the URL string from which the port is to be extracted.
     * @return {@code String} which is the port from the URL.
     */
    public static String getPort(String urlString) {
        try {
            URL url = new URL(urlString);
            if (url.getPort() == -1) {
                return StringUtils.EMPTY;
            } else {
                return String.valueOf(url.getPort());
            }
        } catch (Exception e) {
            log.debug("Failed to get port ", e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * Creates a new URL with the given protocol and hostname.
     *
     * @param protocol the protocol to be used in the URL.
     *                 e.g. http, https
     * @param hostname the hostname to be used in the URL.
     * @return {@code String} which is the new URL.
     */
    public static String createNewUri(String protocol, String hostname) {
        try {
            URL url = new URL(protocol, hostname, "");
            return url.toString();

        } catch (Exception e) {
            log.debug("Failed to create new URL ", e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * Creates a new URL with the given protocol, hostname, and port.
     *
     * @param protocol the protocol to be used in the URL.
     *                 e.g. http, https
     * @param hostname the hostname to be used in the URL.
     * @param port     the port to be used in the URL.
     * @return {@code String} which is the new URL.
     */
    public static String createNewUri(String protocol, String hostname, String port) {
        try {
            URL url = new URL(protocol, hostname, Integer.parseInt(port), "");
            return url.toString();

        } catch (Exception e) {
            log.debug("Failed to create new URL ", e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * Appends the given parameters to the URL.
     *
     * @param uri        the URL to which the parameters are to be appended.
     * @param parameters the parameters to be appended.
     * @return {@code String} which is the new URL.
     * @throws URISyntaxException if the URI is invalid.
     */
    public static String appendUri(String uri, Map<String, String> parameters) throws URISyntaxException {


        if (parameters != null && parameters.size() > 0) {
            URI oldUri = new URI(uri);
            StringBuilder queries = new StringBuilder();
            for (Map.Entry<String, String> query : parameters.entrySet()) {
                queries.append("&" + query.getKey() + "=" + query.getValue());
            }

            String newQuery = oldUri.getQuery();
            if (newQuery == null) {
                newQuery = queries.substring(1);
            } else {
                newQuery += queries.toString();
            }

            URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(),
                    oldUri.getPath(), newQuery, oldUri.getFragment());
            return newUri.toString();
        }
        return uri;
    }

    /**
     * Appends the given query to the URL.
     *
     * @param uri         the URL to which the query is to be appended.
     * @param appendQuery the query to be appended.
     * @return {@code String} which is the new URL.
     * @throws URISyntaxException if the URI is invalid.
     */
    public static String appendQueryFromUri(String uri, String appendQuery) throws URISyntaxException {
        URI oldUri = new URI(uri);
        String newQuery = oldUri.getQuery();
        if (StringUtils.isNotEmpty(appendQuery)) {
            if (newQuery == null) {
                newQuery = appendQuery;
            } else {
                newQuery += "&" + appendQuery;
            }
        }
        return newQuery;
    }

    /**
     * Appends the given path segments to the URL.
     *
     * @param url          the URL to which the path segments are to be appended.
     * @param pathSegments the path segments to be appended.
     * @return {@code String} which is the new URL.
     * @throws URISyntaxException if the URI is invalid.
     */
    public static String appendPathSegments(String url, List<String> pathSegments) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        String oldPath = uriBuilder.getPath() == null ? "" : uriBuilder.getPath();
        for (int i = 0; i < pathSegments.size(); i++) {
            oldPath = Paths.get(oldPath, pathSegments.get(i)).toString();
            if (oldPath.contains(BACKWARD_SLASH))
                oldPath = oldPath.replace(BACKWARD_SLASH_CHAR, FORWARD_SLASH_CHAR);
        }
        uriBuilder = uriBuilder.setPath(oldPath);
        return uriBuilder.toString();
    }

    /**
     * Checks if the URL is absolute.
     *
     * @param url the URL to be checked.
     * @return {@code true} if the URL is absolute, {@code false} otherwise.
     */
    public static boolean isUrlAbsolute(String url) {
        return url.matches("\\w+://.*");
    }

    /**
     * Returns the absolute URL from the base and relative URL.
     *
     * @param baseUrl    the base URL.
     * @param relUrl     the relative URL.
     * @param defaultURL the default URL.
     * @return {@code String} which is the absolute URL.
     */
    public static String getAbsoluteURLFromBaseAndRelativeUrl(String baseUrl, String relUrl, String defaultURL) {
        try {

            if (StringUtils.isNotEmpty(relUrl)) {
                String pathSegmentBaseUrl = getCompletePathSegment(baseUrl);
                URI basePath = URI.create(pathSegmentBaseUrl);
                URI relativeUrlSegment = URI.create(relUrl);
                String relativePath = basePath.relativize(relativeUrlSegment).toString();
                if (StringUtils.isEmpty(relativePath)) { // no difference between base and relative URL
                    URL base = new URL(baseUrl);
                    log.debug("Creating new URL as {} ", base);
                    return base.toString();
                } else {
                    //is Base URL a substring?
                    if (relUrl.indexOf(pathSegmentBaseUrl) >= 0) {
                        relativePath = relUrl.substring(relUrl.indexOf(pathSegmentBaseUrl) + pathSegmentBaseUrl.length());
                        String url = UrlUtils.appendPathSegments(baseUrl, Arrays.asList(relativePath));
                        URL changedUrl = new URL(url);
                        log.debug("Creating new URL as {} ", changedUrl);
                        return changedUrl.toString();
                    } else {
                        // not a substring , relative is different simple append
                        String url = UrlUtils.appendPathSegments(baseUrl, Arrays.asList(relativePath));
                        URL changedUrl = new URL(url);
                        log.debug("Creating new URL as {} ", changedUrl);
                        return changedUrl.toString();
                    }

                }
            } else {
                return new URL(baseUrl).toString();
            }
        } catch (Exception e) {
            log.debug("Failed to get URL from base and relative URL, using defalt ", e);
        }
        return defaultURL;
    }

    /**
     * Returns the complete path segment from the URL.
     *
     * @param url the URL from which the path segment is to be extracted.
     * @return {@code String} which is the complete path segment from the URL.
     * @throws MalformedURLException if the URL is invalid.
     */
    public static String getCompletePathSegment(String url) throws MalformedURLException {
        URL baseUrl = new URL(url);
        return baseUrl.getPath();
    }
}
