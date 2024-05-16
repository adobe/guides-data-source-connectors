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
package com.adobe.guides.konnect.definitions.core.models.template;

/**
 * This class represents a template.
 * <p>
 * It contains the name and content of the template.
 *
 * @author Adobe
 * @since 1.0.0
 */
public class TemplateDto {
    private String name;
    private String content;

    /**
     * Constructor for the template.
     */
    public TemplateDto() {
    }

    /**
     * Constructor for the template.
     *
     * @param name    {@code String} which is the name of the template.
     * @param content {@code String} which is the content of the template.
     */
    public TemplateDto(String name, String content) {
        this.name = name;
        this.content = content;
    }

    /**
     * Returns the name of the template.
     *
     * @return a {@code String} which is the name of the template.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the template.
     *
     * @param name a {@code String} which is the name of the template.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the content of the template.
     *
     * @return a {@code String} which is the content of the template.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the template.
     *
     * @param content a {@code String} which is the content of the template.
     */
    public void setContent(String content) {
        this.content = content;
    }
}
