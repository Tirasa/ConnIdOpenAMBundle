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
package org.connid.bundles.openam.realenvironment;

import org.connid.bundles.openam.OpenAMConnector;
import org.connid.bundles.openam.utilities.SharedMethodsForTests;
import org.connid.bundles.openam.utilities.TestAccountsValue;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.junit.Before;
import org.junit.Test;

public class OpenAMDeleteTest extends SharedMethodsForTests {

    private OpenAMConnector connector = null;
    private Name name = null;
    private Uid newAccount = null;
    private TestAccountsValue attrs = null;

    @Before
    public final void initTest() {
        attrs = new TestAccountsValue();
        connector = new OpenAMConnector();
        connector.init(createConfiguration());
        name = new Name(attrs.getUsername());
    }

    @Test(expected = ConnectorException.class)
    public final void deleteTestOfNotExistsUser() {
        connector.init(createConfiguration());
        connector.delete(ObjectClass.ACCOUNT,
                new Uid(attrs.getWrongUsername()), null);
        connector.dispose();
    }
    
    @Test(expected = ConnectorException.class)
    public final void deleteNullUser() {
        connector.delete(ObjectClass.ACCOUNT, null, null);
    }
    
    @Test(expected = ConnectorException.class)
    public final void deleteNull() {
        connector.delete(null, null, null);
    }

    @Test(expected = ConnectorException.class)
    public final void deleteWithWrongObjectClass() {
        connector.delete(attrs.getWrongObjectClass(),
                newAccount, null);
    }
}
