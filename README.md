# Guides Data Source Connectors

![GitHub license](https://img.shields.io/badge/license-Apache%202.0-blue.svg)

# Description
This repository contains the interface definitions used by the AEM Guides to create connectors
and configurations which connect to external data sources.

This repository also contains connector implementations for various external data sources. 
These connectors are designed to facilitate the integration of external data sources with AEM Guides, 
providing a seamless experience for accessing data.


# Contents

### 1. Konnect Interface Definitions [![javadoc](https://javadoc.io/badge2/com.adobe.aem.addon.guides/konnect-definitions/javadoc.svg)](https://javadoc.io/doc/com.adobe.aem.addon.guides/konnect-definitions)

- Description: Interfaces for connector and configurations.
- Usage: Use these interfaces to create connector implementations. 

### 2. Akeneo Connector [![javadoc](https://javadoc.io/badge2/com.adobe.aem.addon.guides/konnect-akeneo/javadoc.svg)](https://javadoc.io/doc/com.adobe.aem.addon.guides/konnect-akeneo)

- Description: Connector implementation for Akeneo.
- Usage: Use this connector to connect to Akeneo PIM and fetch data into AEM Guides.

### 3. Azure DevOps Connector [![javadoc](https://javadoc.io/badge2/com.adobe.aem.addon.guides/konnect-azure-devops/javadoc.svg)](https://javadoc.io/doc/com.adobe.aem.addon.guides/konnect-azure-devops)

- Description: Connector implementation for Azure DevOps Boards.
- Usage: Use this connector to connect to Azure DevOps Boards and get work items details in AEM Guides.

### 4. Salsify Connector [![javadoc](https://javadoc.io/badge2/com.adobe.aem.addon.guides/konnect-salsify/javadoc.svg)](https://javadoc.io/doc/com.adobe.aem.addon.guides/konnect-salsify)

- Description: Connector implementation for Salsify.
- Usage: Use this connector to connect to external Salsify PIM and fetch data into AEM Guides.

# Contributing

Contributions to this repository are welcome. 
If you have a new connector implementation or improvements to existing connectors, please feel free to submit a pull request.

# License

This repository is licensed under the Apache 2.0 License. See the [LICENSE](LICENSE) file for details.
