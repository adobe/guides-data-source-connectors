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
 * This class provides the OAuth access token request object for the Akeneo connector. <p>
 * It is used to store the username, password and grant type.
 *
 * @since 1.0.0
 */
public class OuathAccessTokenRequestDto {

    private String username;
    private String password;
    private String grant_type;

    /**
     * Constructor to initialize the grant type.
     */
    public OuathAccessTokenRequestDto() {
        grant_type = "password";
    }

    /**
     * Returns the username.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username.
     * @return {@link OuathAccessTokenRequestDto} object.
     */
    public OuathAccessTokenRequestDto setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Returns the password.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password.
     * @return {@link OuathAccessTokenRequestDto} object.
     */
    public OuathAccessTokenRequestDto setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Returns the grant type.
     *
     * @return the grant type.
     */
    public String getGrantType() {
        return grant_type;
    }

    /**
     * Sets the grant type.
     *
     * @param grant_type the grant type.
     * @return {@link OuathAccessTokenRequestDto} object.
     */
    public OuathAccessTokenRequestDto setGrantType(String grant_type) {
        this.grant_type = grant_type;
        return this;
    }
}
