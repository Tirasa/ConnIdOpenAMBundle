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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.connid.bundles.openam.OpenAMConnector;
import org.connid.bundles.openam.utilities.SharedMethodsForTests;
import org.connid.bundles.openam.utilities.TestAccountsValue;
import org.identityconnectors.framework.common.objects.*;
import org.junit.Before;
import org.junit.Test;

public class OpenAMExecuteQueryTest extends SharedMethodsForTests {

    private OpenAMConnector connector = null;

    private Name name = null;

    private Uid newAccount = null;

    private TestAccountsValue attrs = null;

    private final static boolean ACTIVE_USER = true;

    @Before
    public final void initTest() {
        attrs = new TestAccountsValue();
        connector = new OpenAMConnector();
        connector.init(createConfiguration());
        name = new Name(attrs.getUsername());
    }

    @Test
    public final void executeQueryOnlyUidTest() {
        final Set<ConnectorObject> actual = new HashSet<ConnectorObject>();
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(),
                ACTIVE_USER), null);
        assertEquals(name.getNameValue(), newAccount.getUidValue());

        connector.executeQuery(ObjectClass.ACCOUNT, "(" + Uid.NAME + "=" + newAccount.getUidValue() + ")",
                new ResultsHandler() {

                    @Override
                    public boolean handle(final ConnectorObject connObj) {
                        actual.add(connObj);
                        return true;
                    }
                }, null);
        for (ConnectorObject connObj : actual) {
            assertEquals(name.getNameValue(), connObj.getName().getNameValue());
        }
        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
        assertFalse(connector.existsUser(newAccount.getUidValue()));
        connector.dispose();
    }

    @Test
    public final void executeQueryWithAndFilterTest() {
        final Set<ConnectorObject> actual = new HashSet<ConnectorObject>();
        newAccount = connector.create(ObjectClass.ACCOUNT, createSetOfAttributes(name, attrs.getPassword(),
                ACTIVE_USER), null);
        assertEquals(name.getNameValue(), newAccount.getUidValue());

        connector.executeQuery(ObjectClass.ACCOUNT,
                "(&(cn=" + name.getNameValue() + ")(sn=" + name.getNameValue() + ")", new ResultsHandler() {

            @Override
            public boolean handle(final ConnectorObject connObj) {
                actual.add(connObj);
                return true;
            }
        }, null);
        for (ConnectorObject connObj : actual) {
            assertEquals(name.getNameValue(), connObj.getName().getNameValue());
        }
        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
        assertFalse(connector.existsUser(newAccount.getUidValue()));
        connector.dispose();
    }

    @Test
    public final void executeQueryWithOrFilterTest() {
        final Set<ConnectorObject> actual = new HashSet<ConnectorObject>();
        newAccount = connector.create(ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(), ACTIVE_USER), null);
        assertEquals(name.getNameValue(), newAccount.getUidValue());

        connector.executeQuery(ObjectClass.ACCOUNT,
                "(|(cn=" + name.getNameValue() + ")(sn=" + name.getNameValue() + ")", new ResultsHandler() {

            @Override
            public boolean handle(final ConnectorObject co) {
                actual.add(co);
                return true;
            }
        }, null);
        for (ConnectorObject connObj : actual) {
            assertEquals(name.getNameValue(), connObj.getName().getNameValue());
        }
        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
        assertFalse(connector.existsUser(newAccount.getUidValue()));
        connector.dispose();
    }

    @Test
    public final void executeQueryNotExistingUserTest() {
        final Set<ConnectorObject> actual = new HashSet<ConnectorObject>();
        connector.executeQuery(ObjectClass.ACCOUNT,
                "(" + Uid.NAME + "=notexistsuser)", new ResultsHandler() {

            @Override
            public boolean handle(final ConnectorObject connObj) {
                actual.add(connObj);
                return true;
            }
        }, null);
        assertTrue(actual.isEmpty());
        connector.dispose();
    }
}
