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
package org.connid.bundles.openam.methods;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.connid.bundles.openam.OpenAMConfiguration;
import org.connid.bundles.openam.OpenAMConnection;
import org.connid.bundles.openam.utilities.AdminToken;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.springframework.web.client.HttpClientErrorException;

public class OpenAMSearch extends CommonMethods {

    private static final Log LOG = Log.getLog(OpenAMSearch.class);

    private OpenAMConnection connection = null;

    private String uid = "";

    private AdminToken adminToken = null;

    public OpenAMSearch(final OpenAMConfiguration configuration, final String uid)
            throws UnsupportedEncodingException {

        super(configuration);

        connection = OpenAMConnection.openConnection(configuration);
        this.uid = uid;
        adminToken = new AdminToken(configuration);
    }

    public final boolean existsUser() {
        try {
            return doExistsUser();
        } catch (Exception e) {
            LOG.error(e, "error during search user operation");
            throw new ConnectorException(e);
        }
    }

    public final boolean doExistsUser()
            throws IOException {
        try {
            boolean userExists = userExists(uid, configuration.getOpenamRealm(), adminToken.getToken(), connection);
            connection.logout(adminToken.getToken());
            return userExists;
        } catch (HttpClientErrorException hcee) {
            throw hcee;
        }
    }
}
