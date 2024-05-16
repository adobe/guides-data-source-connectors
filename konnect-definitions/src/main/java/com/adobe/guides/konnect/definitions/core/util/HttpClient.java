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

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * HttpClient service that creates an HTTP client for making HTTP requests.
 *
 * <p>Using the function {@link #getCloseableHttpClient()}
 * returns a {@link CloseableHttpClient} for HTTP request.
 *
 * @author Adobe
 * @since 1.0.0
 */
@Component(service = HttpClient.class)
public class HttpClient {

    @Reference
    private HttpClientBuilderFactory httpClientBuilderFactory;

    /**
     * Returns a {@link CloseableHttpClient} for HTTP request
     *
     * @return {@link CloseableHttpClient} for HTTP request
     */
    public CloseableHttpClient getCloseableHttpClient() {
        int timeout = 20;
        int socketTimeout = 120;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setSocketTimeout(socketTimeout * 1000).build();
        return httpClientBuilderFactory.newBuilder().setDefaultRequestConfig(config).build();
    }
}
