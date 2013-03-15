/**
 * ====================
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008-2009 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2011-2013 Tirasa. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License"). You may not use this file
 * except in compliance with the License.
 *
 * You can obtain a copy of the License at https://oss.oracle.com/licenses/CDDL
 * See the License for the specific language governing permissions and limitations
 * under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each file
 * and include the License file at https://oss.oracle.com/licenses/CDDL.
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * ====================
 */
package org.connid.bundles.openam;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.httpclient.HttpStatus;
import org.connid.bundles.openam.http.HttpClientMethods;
import org.connid.bundles.openam.restful.RestfulClientMethods;
import org.connid.bundles.openam.utilities.UrlFactory;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.springframework.web.client.HttpClientErrorException;

public class OpenAMConnection {

    private static final Log LOG = Log.getLog(OpenAMConnection.class);

    private OpenAMConfiguration configuration = null;

    private final RestfulClientMethods restfulClient =
            new RestfulClientMethods();

    private final HttpClientMethods httpClientMethods =
            new HttpClientMethods();

    private UrlFactory urlFactory = null;

    private OpenAMConnection(final OpenAMConfiguration configuration) {
        this.configuration = configuration;
        urlFactory = new UrlFactory(configuration);
    }

    public static OpenAMConnection openConnection(
            final OpenAMConfiguration configuration) {

        return new OpenAMConnection(configuration);
    }

    public String authenticate(final String username, final String password)
            throws HttpClientErrorException {
        try {
            return restfulClient.getMethod(new URI(urlFactory.authenticateUrl(username, password)));
        } catch (URISyntaxException e) {
            LOG.error(e, "Authentication failed. Invalid URI.");
            return null;
        }
    }

    public void create(final String parameters) {
        try {
            restfulClient.getMethod(new URI(urlFactory.createUrl(parameters)));
        } catch (URISyntaxException e) {
            LOG.error(e, "Creation failed. Invalid URI.");
            throw new ConnectorException(e);
        }
    }

    public void logout(final String token) {
        restfulClient.getMethod(urlFactory.logoutUrl(token));
    }

    public void update(final String parameters) {
        try {
            restfulClient.getMethod(new URI(urlFactory.updateUrl(parameters)));
        } catch (URISyntaxException e) {
            LOG.error(e, "Update failed. Invalid URI.");
            throw new ConnectorException(e);
        }
    }

    public void delete(final String parameters) {
        restfulClient.getMethod(urlFactory.deleteUrl(parameters));
    }

    public String userSearch(final String parameters) {
        return restfulClient.getMethod(urlFactory.searchUrl(parameters));
    }

    public String search(final String parameters)
            throws IOException {
        return httpClientMethods.get(urlFactory.searchUrl(parameters));
    }

    public String read(final String parameters)
            throws HttpClientErrorException {
        return restfulClient.getMethod(urlFactory.readUrl(parameters));
    }

    public boolean isAlive()
            throws IOException {
        Integer statusCode = new Integer(httpClientMethods.getMethod(urlFactory.testUrl()));
        return statusCode.equals(HttpStatus.SC_OK);
    }

    public String isTokenValid(final String parameters) {
        return restfulClient.getMethod(urlFactory.isTokenValidUrl(parameters));
    }
}
