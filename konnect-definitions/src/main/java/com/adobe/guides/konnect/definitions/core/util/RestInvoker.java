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

import com.adobe.guides.konnect.definitions.core.config.AuthenticationDetails;
import com.adobe.guides.konnect.definitions.core.exception.KonnectQueryException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;

/**
 * Utility class to execute a REST call. This class provides methods
 * to prepare a REST connection and send a request.
 *
 * <p><tt>RestInvoker</tt> provides control to create a REST connection.
 * <tt>buildUri</tt> is used to create a {@link URIBuilder} with a query param.
 * <tt>buildUriRequest</tt> is used to create an {@link HttpUriRequest} using the
 * {@code URIBuilder} object. <tt>addHeaders</tt> adds custom headers in
 * the HTTP request.
 *
 * <p>There are more utility methods like <tt>buildQuery</tt> to create a query
 * and <tt>getPlainHttpClient</tt> to create an HTTP client.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class RestInvoker {

    private static final Logger log = LoggerFactory.getLogger(RestInvoker.class);

    /**
     * Returns a {@link HttpUriRequest} object which is the connector of the HTTP
     * request
     *
     * @param authDetails {@link AuthenticationDetails} object which contains
     *                    the authentication details
     * @param url         {@code String} which is the URL of the HTTP request
     * @param requestType {@code String} which is the type of the HTTP request
     * @param body        {@code String} which is the body of the HTTP request
     * @param query       {@code String} which is the query of the HTTP request
     * @param headers     {@code Map<String, String>} which contains the headers
     *                    of the HTTP request
     * @return {@link HttpUriRequest} object which is the connector of the HTTP
     * request.
     * @throws URISyntaxException           if the URI is invalid or malformed
     * @throws UnsupportedEncodingException if the encoding is not supported
     */
    public HttpUriRequest prepareConnection(AuthenticationDetails authDetails, String url, String requestType, String body, String query, Map<String, String> headers) throws URISyntaxException, UnsupportedEncodingException {
        query = buildQuery(query, authDetails.getQuery());
        URIBuilder uriBuilder = buildUri(url, query);
        HttpUriRequest uriRequest = buildUriRequest(uriBuilder, requestType, body);
        addHeaders(uriRequest, authDetails.getHeader(), headers);

        return uriRequest;
    }

    /**
     * Returns a {@link HttpUriRequest} object which is the connector of the HTTP
     * request. This method is used when the URL contains path segments.
     *
     * @param authDetails {@link AuthenticationDetails} object which contains
     *                    the authentication details
     * @param url         {@code String} which is the URL of the HTTP request
     * @param requestType {@code String} which is the type of the HTTP request
     * @param body        {@code String} which is the body of the HTTP request
     * @param query       {@code String} which is the query of the HTTP request
     * @param headers     {@code Map<String, String>} which contains the headers
     *                    of the HTTP request
     * @param pathSegs    {@code List<String>} which contains the path segments
     * @return {@link HttpUriRequest} object which is the connector of the HTTP
     * request.
     * @throws URISyntaxException           if the URI is invalid or malformed
     * @throws UnsupportedEncodingException if the encoding is not supported
     */
    public HttpUriRequest prepareConnection(AuthenticationDetails authDetails, String url, String requestType, String body, String query, Map<String, String> headers, List<String> pathSegs) throws URISyntaxException, UnsupportedEncodingException {
        query = buildQuery(query, authDetails.getQuery());
        URIBuilder uriBuilder = buildUri(url, query, pathSegs);
        HttpUriRequest uriRequest = buildUriRequest(uriBuilder, requestType, body);
        addHeaders(uriRequest, authDetails.getHeader(), headers);

        return uriRequest;
    }

    /**
     * Returns a {@code String} which is the response of an HTTP request
     *
     * @param uriRequest {@link HttpUriRequest} object which is the connector of the
     *                   HTTP request to be executed.
     * @param httpClient {@link HttpClient} object which is the client to execute the
     *                   HTTP request with.
     * @return a {@code String} which is the response of an HTTP request
     * @throws IOException           if an I/O error occurs while sending the request
     * @throws KonnectQueryException if an error is received from the remote service
     */
    public String invokeRequest(HttpUriRequest uriRequest, HttpClient httpClient) throws IOException, KonnectQueryException {
        HttpResponse response = httpClient.execute(uriRequest);
        log.debug("Response from request {} ", response);
        if (response.getStatusLine() != null && response.getStatusLine().getStatusCode() != SC_OK) {
            throw new KonnectQueryException("Error received from remote service");
        }
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity, StandardCharsets.UTF_8);
    }

    /**
     * Returns a {@link URIBuilder} object which is a URL with a query
     *
     * @param url   {@code String} which is the URL of the HTTP request
     * @param query {@code String} which is the query of the HTTP request
     * @return {@link URIBuilder} object which is a URL with a query
     * @throws URISyntaxException if the URI is invalid or malformed
     */
    private URIBuilder buildUri(String url, String query) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        String combinedQuery = UrlUtils.appendQueryFromUri(url, query);
        uriBuilder.setCustomQuery(combinedQuery);
        return uriBuilder;
    }

    /**
     * Returns a {@link URIBuilder} object which is a URL with path segments.
     *
     * @param url          {@code String} which is the URL of the HTTP request
     * @param query        {@code String} which is the query of the HTTP request
     * @param pathSegments {@code List<String>} which contains the path segments
     * @return {@link URIBuilder} object which is a URL with path segments
     * @throws URISyntaxException if the URI is invalid or malformed
     */
    private URIBuilder buildUri(String url, String query, List<String> pathSegments) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        String combinedQuery = UrlUtils.appendQueryFromUri(url, query);
        String pathSegmentsStr = UrlUtils.appendPathSegments(url, pathSegments);
        uriBuilder.setCustomQuery(combinedQuery);
        uriBuilder.setPathSegments(pathSegmentsStr);
        return uriBuilder;
    }

    /**
     * Returns a {@link HttpUriRequest} object which is an HTTP request. This
     * request is created based on the type (GET/POST) in input. If not empty,
     * <tt>body</tt> is also added to the request.
     *
     * @param uriBuilder  {@link URIBuilder} object which is the URL of the HTTP request
     * @param requestType {@code String} which is the type of the HTTP request
     * @param body        {@code String} which is the body of the HTTP request
     * @return {@link HttpUriRequest} object which is an HTTP request
     * @throws URISyntaxException           if the URI is invalid or malformed
     * @throws UnsupportedEncodingException if the encoding is not supported
     */
    private HttpUriRequest buildUriRequest(URIBuilder uriBuilder, String requestType, String body) throws URISyntaxException, UnsupportedEncodingException {
        HttpUriRequest uriRequest = null;
        if ("POST".equals(requestType)) {
            HttpPost postRequest = new HttpPost(uriBuilder.build());
            if (StringUtils.isNotBlank(body)) {
                postRequest.setEntity(new StringEntity(body));
            }
            uriRequest = postRequest;
        } else {
            uriRequest = new HttpGet(uriBuilder.build());
        }
        return uriRequest;
    }

    /**
     * Adds headers to a <tt>HttpUriRequest</tt> request
     *
     * @param uriRequest  {@link HttpUriRequest} object which is the connector of the
     *                    HTTP request
     * @param authHeaders {@code Map<String, String>} which contains the authentication
     *                    headers of the HTTP request
     * @param headers     {@code Map<String, String>} which contains the headers of the
     *                    HTTP request.
     */
    public void addHeaders(HttpUriRequest uriRequest, Map<String, String> authHeaders, Map<String, String> headers) {
        if (authHeaders != null) {
            authHeaders.forEach(uriRequest::setHeader);
        }
        if (headers != null) {
            headers.forEach(uriRequest::setHeader);
        }
    }

    /**
     * Returns a {@code String} which is the query appended with other
     * custom query parameters received in <tt>AuthenticationDetails</tt>
     *
     * @param query     {@code String} which is the query for the HTTP request
     *                  to be appended with other custom query parameters
     *                  received in <tt>AuthenticationDetails</tt>.
     * @param authQuery {@code Map<String, String>} which contains the custom query
     *                  parameters to be appended to the query.
     * @return {@code String} which is the query for the HTTP request
     */
    public String buildQuery(String query, Map<String, String> authQuery) {
        if (authQuery == null) {
            return query;
        }
        for (Map.Entry<String, String> entry : authQuery.entrySet()) {
            query = query + "&" + entry.getKey() + "=" + entry.getValue();
        }
        return StringUtils.strip(query, "&");
    }
}
