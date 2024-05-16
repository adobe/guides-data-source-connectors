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
package com.adobe.guides.konnect.definitions.core.connector;

import com.adobe.guides.konnect.definitions.core.exception.KonnectConnectionException;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Session;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.adobe.guides.konnect.definitions.core.constants.Constants.ADOBE_SYSTEMS;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.FILE_CONNECTOR;
import static com.adobe.guides.konnect.definitions.core.constants.Constants.JCR_CONTENT_RENDITION;

/**
 * This class provides a skeletal implementation of the {@link Connector}
 * interface to minimize the effort required to implement this interface
 * for the File-based connector.
 *
 * <p>To implement a File connector, the programmer needs only to extend
 * this class and provide implementations for the {@link #getName()} and
 * {@link #getConfigClass()} methods.
 *
 * <p>The documentation for each non-abstract method in this class describes its
 * implementation in detail.  Each of these methods may be overridden if the
 * Connector being implemented admits a more efficient implementation.
 *
 * @author Adobe
 * @since 1.0.0
 */
public abstract class FileConnector implements Connector {
    private Session session = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthor() {
        return ADOBE_SYSTEMS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGroup() {
        return FILE_CONNECTOR;
    }

    /**
     * Reads the file from the JCR repository.
     *
     * @param filePath the path of the file to be read.
     * @param resolver the resource resolver.
     * @return {@code String} the content of the file.
     * @throws KonnectConnectionException if an error occurs while reading the file.
     */
    protected String readJCRFile(String filePath, ResourceResolver resolver) throws KonnectConnectionException {
        String dataString;
        try {
            session = resolver.adaptTo(Session.class);
            Node fileNode = session.getNode(filePath + JCR_CONTENT_RENDITION);
            Binary binaryData = fileNode.getProperty("jcr:data").getBinary();

            try (BufferedInputStream bis = new BufferedInputStream(binaryData.getStream());
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                int bytesRead;
                while ((bytesRead = bis.read()) != -1) {
                    baos.write(bytesRead);
                }
                dataString = new String(baos.toByteArray(), "UTF-8");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new KonnectConnectionException("[FileConnector] Error while reading the file", e);
        }
        return dataString;
    }
}
