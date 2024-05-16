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

package com.adobe.guides.konnect.definitions.ado;


import com.adobe.guides.konnect.definitions.core.annotations.APIDefinition;
import com.adobe.guides.konnect.definitions.core.annotations.CipherText;
import com.adobe.guides.konnect.definitions.core.config.RestWrapperConfig;
import com.adobe.guides.konnect.definitions.core.exception.KonnectException;
import com.adobe.guides.konnect.definitions.core.urlResource.RestResourceDao;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

import static com.adobe.guides.konnect.definitions.ado.Constants.ADO_ORG_INFO;
import static com.adobe.guides.konnect.definitions.ado.Constants.ADO_PAT_CONFIG;
import static com.adobe.guides.konnect.definitions.ado.Constants.ADO_PAT_CONFIG_INFO;
import static com.adobe.guides.konnect.definitions.ado.Constants.ADO_PAT_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_INFO;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_LABEL;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_REQUIRED;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.ANNOTATION_TRUE;

/**
 * This class provides an implementation of the {@link RestWrapperConfig}
 * interface. It is a REST-based config that uses a personal access token.
 *
 * @author Adobe
 * @since 1.0.0
 */
@APIDefinition.List({
        @APIDefinition(name = ANNOTATION_LABEL, value = ADO_PAT_CONFIG),
        @APIDefinition(name = ANNOTATION_INFO, value = ADO_PAT_CONFIG_INFO),
})
public class PersonalAccessTokenConfig extends RestWrapperConfig {

    /**
     * The organization name.
     */
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Organization"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_TRUE),
            @APIDefinition(name = ANNOTATION_INFO, value = ADO_ORG_INFO),
    })
    private String organization;

    /**
     * The personal access token.
     */
    @CipherText
    @APIDefinition.List({
            @APIDefinition(name = ANNOTATION_LABEL, value = "Personal access token"),
            @APIDefinition(name = ANNOTATION_REQUIRED, value = ANNOTATION_TRUE),
            @APIDefinition(name = ANNOTATION_INFO, value = ADO_PAT_INFO),
    })
    private String token;

    public PersonalAccessTokenConfig(String organization, String token, List<RestResourceDao> resourceList) {
        super(resourceList);
        this.organization = organization;
        this.token = token;
    }

    /**
     * Returns the unique name for this config.
     *
     * @return a {@code String} which is the name of this config.
     */
    @Override
    public String getName() {
        return ADO_PAT_CONFIG;
    }

    /**
     * Returns the organisation name for this config.
     *
     * @return a {@code String} which is the organisation name of this config.
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Returns the personal access token for this config.
     *
     * @return a {@code String} which is the personal access token of this config.
     */
    public String getToken() {
        return token;
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
}
