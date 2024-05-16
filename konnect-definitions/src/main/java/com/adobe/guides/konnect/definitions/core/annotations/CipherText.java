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
package com.adobe.guides.konnect.definitions.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.ACTIVE;

/**
 * An annotation to make a field secret. The user of this annotation
 * has control over which configuration fields are to be marked secret.
 * <p>
 * If annotated with @CipherText, any configuration field will be
 * encrypted while storing. They will not be visible to the user. <p>
 *
 * @author Adobe
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CipherText {
    String state() default ACTIVE;
}
