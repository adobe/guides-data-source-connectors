package com.adobe.guides.konnect.definitions.salsify.config;

import com.adobe.guides.konnect.definitions.core.config.Config;
import com.adobe.guides.konnect.definitions.core.config.RestConfig;
import org.osgi.service.component.annotations.Component;

import static com.adobe.guides.konnect.definitions.salsify.constants.Constants.BEARER_TOKEN;

/**
 * This is a factory class which provides all the available
 * RestConfig classes.
 *
 * @author Adobe
 * @since 1.0.0
 */
@Component(service = RestConfigFactory.class)
public class RestConfigFactory {

    /**
     * This method returns the RestConfig class based on the
     * config name.
     *
     * @param config The config object.
     * @return The RestConfig object.
     */
    public RestConfig getRestConfig(Config config) {
        switch (config.getName()) {
            case BEARER_TOKEN:
                return (BearerTokenRestConfig) config;
            default:
                return (RestConfig) config;
        }
    }

    /**
     * This method returns all the available RestConfig classes.
     *
     * @return The array of RestConfig classes.
     */
    public Class[] getAllRestConfigClass() {
        return new Class[]{BearerTokenRestConfig.class};
    }
}
