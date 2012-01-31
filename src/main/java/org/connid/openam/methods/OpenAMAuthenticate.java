/*
 * ====================
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011 Tirasa. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License").  You may not use this file
 * except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * http://IdentityConnectors.dev.java.net/legal/license.txt
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing the Covered Code, include this
 * CDDL Header Notice in each file
 * and include the License file at identityconnectors/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * ====================
 */
package org.connid.openam.methods;

import org.connid.openam.OpenAMConfiguration;
import org.connid.openam.OpenAMConnection;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Uid;
import org.springframework.web.client.HttpClientErrorException;

public class OpenAMAuthenticate extends CommonMethods {

    private static final Log LOG = Log.getLog(OpenAMAuthenticate.class);
    private OpenAMConnection connection = null;
    String username = "";
    GuardedString password = null;

    public OpenAMAuthenticate(final OpenAMConfiguration openAMConfiguration,
            final String username, final GuardedString password) {
        connection = OpenAMConnection.openConnection(openAMConfiguration);
        this.username = username;
        this.password = password;
    }

    public Uid authenticate() {
        try {
            return executeImpl();
        } catch (Exception e) {
            LOG.error(e, "error during authentication");
            throw new ConnectorException(e);
        }
    }

    private Uid executeImpl() {
        try {
            connection.authenticate(username, getPlainPassword(password));
        } catch (HttpClientErrorException hcee) {
            throw hcee;
        }
        return new Uid(username);
    }
}
