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
package com.adobe.guides.konnect.definitions.core.exception;

/**
 * <tt>KonnectException</tt> is an exception
 * which will be thrown when attempting to execute a query on
 * an unavailable external data source or invalid connection
 * config.
 * <p>
 * It can also be thrown when the query is invalid or the
 * data source is not reachable.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class KonnectException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message.
     */
    public KonnectException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * @param message the detail message.
     * @param cause the cause.
     */
    public KonnectException(String message, Throwable cause) {
        super(message, cause);
    }
}
