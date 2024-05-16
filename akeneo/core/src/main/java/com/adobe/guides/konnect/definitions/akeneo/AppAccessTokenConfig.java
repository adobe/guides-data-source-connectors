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
package com.adobe.guides.konnect.definitions.akeneo;

import com.adobe.guides.konnect.definitions.core.annotations.APIDefinition;
import com.adobe.guides.konnect.definitions.core.annotations.CipherText;
import com.adobe.guides.konnect.definitions.core.config.AuthenticationDetails;
import com.adobe.guides.konnect.definitions.core.config.RestConfig;
import com.adobe.guides.konnect.definitions.core.exception.KonnectException;
import com.adobe.guides.konnect.definitions.core.urlResource.RestResourceDao;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.AKENEO_AUTH_CONFIG;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.AKENEO_AUTH_INFO;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.AKENEO_CLIENTID_SECRET;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.AKENEO_CLIENTID_TOKEN;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.AKENEO_CONFIG_INFO;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.AKENEO_PASS_INFO;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.BASIC_AUTH_KEY;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.BEARER_AUTH_HEADER_INFO;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.BEARER_TOKEN_AUTH_KEY;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.DEFAULT_AUTH_HEADER;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_DEFAULT;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_DEFAULT_AUTH_HEADER;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_FALSE;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_LABEL;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_REQUIRED;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_TRUE;

/**
 * This class provides an implementation of the {@link RestConfig}
 * interface. It is a REST-based config that uses an app access token.
 *
 * @author Adobe
 * @since 1.0.0
 */
@APIDefinition.List({
        @APIDefinition(name = ANNOTATION_LABEL, value = AKENEO_AUTH_CONFIG),
        @APIDefinition(name = ANNOTATION_INFO, value = AKENEO_CONFIG_INFO),
})

public class AppAccessTokenConfig extends RestConfig {

    /**
     * The username.
     */
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Username"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_TRUE),
            @APIDefinition(name = ANNOTATION_INFO, value = AKENEO_AUTH_INFO),
    })
    private String username;

    /**
     * The password.
     */
    @CipherText
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Password"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_TRUE),
            @APIDefinition(name = ANNOTATION_INFO, value = AKENEO_PASS_INFO),
    })
    private String password;

    /**
     * The client ID.
     */
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Clientid"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_TRUE),
            @APIDefinition(name = ANNOTATION_INFO, value = AKENEO_CLIENTID_TOKEN),
    })
    private String clientId;

    /**
     * The client secret for authentication.
     */
    @CipherText
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Secret"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_TRUE),
            @APIDefinition(name = ANNOTATION_INFO, value = AKENEO_CLIENTID_SECRET),
    })
    private String secret;

    /**
     * The header name to add the authentication to.
     */
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Authentication header name"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_FALSE),
            @APIDefinition(name = ANNOTATION_DEFAULT, value = ANNOTATION_DEFAULT_AUTH_HEADER),
            @APIDefinition(name = ANNOTATION_INFO, value = BEARER_AUTH_HEADER_INFO),
    })
    private String authHeaderName;

    /**
     * The token for authentication.
     */
    private String token;

    public AppAccessTokenConfig(String url, String requestType, String body, Map<String, String> headers, String authHeaderName, String username, String password, String clientId, String secret, List<RestResourceDao> resourceList) {
        super(url, requestType, body, headers, resourceList);
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.secret = secret;
        this.authHeaderName = authHeaderName;
        if (StringUtils.isBlank(this.authHeaderName)) {
            this.authHeaderName = DEFAULT_AUTH_HEADER;
        }
    }

    /**
     * Returns a {@link AuthenticationDetails} object to be used to execute a
     * query.
     *
     * @return {@link AuthenticationDetails} object to be used to execute a query.
     */
    @Override
    public AuthenticationDetails getAuthenticationDetails() {
        AuthenticationDetails authDetails = new AuthenticationDetails();
        Map<String, String> authHeader = new HashMap<>();
        if (StringUtils.isBlank(this.authHeaderName)) {
            this.authHeaderName = DEFAULT_AUTH_HEADER;
        }
        authHeader.put(this.authHeaderName, BEARER_TOKEN_AUTH_KEY + this.token);
        authDetails.setHeader(authHeader);
        return authDetails;
    }

    /**
     * Returns a {@link AuthenticationDetails} object to be used to execute a
     * query using OAuth authentication.
     *
     * @return {@link AuthenticationDetails} object to be used to execute a query using OAuth authentication.
     */
    public AuthenticationDetails getAuthenticationDetailsForOauth() {
        AuthenticationDetails authDetails = new AuthenticationDetails();
        Map<String, String> authHeader = new HashMap<>();
        if (StringUtils.isBlank(this.authHeaderName)) {
            this.authHeaderName = DEFAULT_AUTH_HEADER;
        }
        String authString = this.clientId + ":" + this.secret;
        String encode = DatatypeConverter.printBase64Binary(authString.getBytes(StandardCharsets.UTF_8));

        authHeader.put(this.authHeaderName, BASIC_AUTH_KEY + encode);
        authDetails.setHeader(authHeader);
        return authDetails;
    }

    /**
     * Returns the unique name for this config.
     *
     * @return a {@code String} which is the name of this config.
     */
    @Override
    public String getName() {
        return AKENEO_AUTH_CONFIG;
    }

    /**
     * Sets the resources to be used for this config.
     *
     * @param resourceList a {@code List} of {@link RestResourceDao} objects.
     *                     Each object represents a resource to be used.
     */
    @Override
    public void setResourceList(List<RestResourceDao> resourceList) throws KonnectException {
        for (RestResourceDao restResourceDao : resourceList) {
            if (StringUtils.isEmpty(restResourceDao.getId())) {
                restResourceDao.setId(UUID.randomUUID().toString());
            }
        }
        this.setUrlResourceList(resourceList);
    }

    /**
     * Sets the current resource to be used for this config.
     *
     * @param id a {@code String} which is the id of the resource to be used.
     */
    @Override
    public void setCurrentResource(String id) {
        List<RestResourceDao> resourceList = this.getResourceList();
        if (!resourceList.isEmpty() && StringUtils.isNotBlank(id)) {
            for (RestResourceDao resourceDao : resourceList) {
                if (resourceDao.getId().equalsIgnoreCase(id)) {
                    this.setCurrentResource(resourceDao);
                    break;
                }
            }
        }
    }

    /**
     * Returns the username for this config.
     *
     * @return a {@code String} which is the username of this config.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password for this config.
     *
     * @return a {@code String} which is the password of this config.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the token for this config.
     *
     * @param token a {@code String} which is the token to be set.
     */
    public void setToken(String token) {
        this.token = token;
    }
}
