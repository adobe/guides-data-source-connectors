package com.adobe.guides.konnect.definitions.akeneo;

import com.adobe.guides.konnect.definitions.core.annotations.APIDefinition;
import com.adobe.guides.konnect.definitions.core.annotations.CipherText;
import com.adobe.guides.konnect.definitions.core.config.AuthenticationDetails;
import com.adobe.guides.konnect.definitions.core.config.RestConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.BEARER_AUTH_HEADER_INFO;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.BEARER_AUTH_TOKEN_INFO;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.BEARER_TOKEN;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.BEARER_TOKEN_AUTH_KEY;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.DEFAULT_AUTH_HEADER;
import static com.adobe.guides.konnect.definitions.akeneo.constants.Constants.REST_BEARER_AUTH_USERNAME_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_DEFAULT;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_DEFAULT_AUTH_HEADER;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_FALSE;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_LABEL;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_REQUIRED;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_TRUE;

/**
 * This class provides an implementation of the {@link RestConfig}
 * interface. It is a REST-based config that uses a bearer token.
 *
 * @author Adobe
 * @since 1.0.0
 */
@APIDefinition.List({
        @APIDefinition(name = ANNOTATION_LABEL, value = BEARER_TOKEN),
        @APIDefinition(name = ANNOTATION_INFO, value = REST_BEARER_AUTH_USERNAME_INFO),
})
public class BearerTokenRestConfig extends RestConfig {

    /**
     * The token.
     */
    @CipherText
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Bearer authentication token"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_TRUE),
            @APIDefinition(name = ANNOTATION_INFO, value = BEARER_AUTH_TOKEN_INFO)
    })
    private String token;

    /**
     * The Authentication Header Name.
     */
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Authentication header name"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_FALSE),
            @APIDefinition(name = ANNOTATION_DEFAULT, value = ANNOTATION_DEFAULT_AUTH_HEADER),
            @APIDefinition(name = ANNOTATION_INFO, value = BEARER_AUTH_HEADER_INFO),
    })
    private String authHeaderName;

    public BearerTokenRestConfig(String url, String requestType, String body, Map<String, String> headers, String token, String authHeaderName) {
        super(url, requestType, body, headers);
        this.token = token;
        this.authHeaderName = authHeaderName;
        if (StringUtils.isBlank(this.authHeaderName)) {
            this.authHeaderName = DEFAULT_AUTH_HEADER;
        }
    }

    /**
     * Returns the unique name for this config.
     *
     * @return a {@code String} which is the name of this config.
     */
    @Override
    public String getName() {
        return BEARER_TOKEN;
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
}
