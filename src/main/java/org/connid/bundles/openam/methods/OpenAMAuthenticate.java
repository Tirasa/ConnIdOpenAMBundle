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

import org.connid.bundles.openam.OpenAMConfiguration;
import org.connid.bundles.openam.OpenAMConnection;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.springframework.web.client.HttpClientErrorException;

public class OpenAMAuthenticate extends CommonMethods {

    private ObjectClass objectClass = null;

    private OpenAMConnection connection = null;

    private String username = "";

    private GuardedString password = null;

    public OpenAMAuthenticate(final ObjectClass oc,
            final OpenAMConfiguration configuration, final String username, final GuardedString password) {

        super(configuration);

        objectClass = oc;
        connection = OpenAMConnection.openConnection(configuration);
        this.username = username;
        this.password = password;
    }

    public final Uid authenticate() {
        try {
            return doAuthenticate();
        } catch (Exception e) {
            LOG.error(e, "error during authentication");
            throw new ConnectorException(e);
        }
    }

    private Uid doAuthenticate() {
        if (!objectClass.equals(ObjectClass.ACCOUNT)) {
            throw new IllegalStateException("Wrong object class");
        }
        try {
            connection.authenticate(username, getPlainPassword(password));
        } catch (HttpClientErrorException hcee) {
            throw hcee;
        }
        return new Uid(username);
    }
}
