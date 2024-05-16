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
package com.adobe.guides.konnect.definitions.akeneo.dto;

/**
 * This class provides the token response object for the Akeneo connector. <p>
 * It is used to store the access token, expires in, token type, scope and refresh token.
 *
 * @since 1.0.0
 */
public class AkeneoTokenResponseDto {

    private String access_token;
    private Integer expires_in;
    private String token_type;
    private Object scope;
    private String refresh_token;

    /**
     * Sole constructor
     */
    public AkeneoTokenResponseDto() {
    }

    /**
     * Returns the access token.
     *
     * @return the access token.
     */
    public String getAccessToken() {
        return access_token;
    }

    /**
     * Sets the access token.
     *
     * @param accessToken the access token.
     * @return {@link AkeneoTokenResponseDto} object.
     */
    public AkeneoTokenResponseDto setAccessToken(String accessToken) {
        this.access_token = accessToken;
        return this;
    }

    /**
     * Returns the expires in.
     *
     * @return the expires in.
     */
    public Integer getExpiresIn() {
        return expires_in;
    }

    /**
     * Sets the expires in.
     *
     * @param expiresIn the expires in.
     * @return {@link AkeneoTokenResponseDto} object.
     */
    public AkeneoTokenResponseDto setExpiresIn(Integer expiresIn) {
        this.expires_in = expiresIn;
        return this;
    }

    /**
     * Returns the token type.
     *
     * @return {@code String} which is the token type.
     */
    public String getTokenType() {
        return token_type;
    }

    /**
     * Sets the token type.
     *
     * @param tokenType the token type.
     * @return {@link AkeneoTokenResponseDto} object.
     */
    public AkeneoTokenResponseDto setTokenType(String tokenType) {
        this.token_type = tokenType;
        return this;
    }

    /**
     * Returns the scope.
     *
     * @return the scope.
     */
    public Object getScope() {
        return scope;
    }

    /**
     * Sets the scope.
     *
     * @param scope the scope.
     * @return {@link AkeneoTokenResponseDto} object.
     */
    public AkeneoTokenResponseDto setScope(Object scope) {
        this.scope = scope;
        return this;
    }

    /**
     * Returns the refresh token.
     *
     * @return {@code String} which is the refresh token.
     */
    public String getRefreshToken() {
        return refresh_token;
    }

    /**
     * Sets the refresh token.
     *
     * @param refreshToken the refresh token.
     * @return {@link AkeneoTokenResponseDto} object.
     */
    public AkeneoTokenResponseDto setRefreshToken(String refreshToken) {
        this.refresh_token = refreshToken;
        return this;
    }
}
