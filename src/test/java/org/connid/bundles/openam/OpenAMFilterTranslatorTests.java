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

import static org.junit.Assert.assertEquals;

import org.connid.bundles.openam.utilities.SharedMethodsForTests;
import org.junit.Test;

public class OpenAMFilterTranslatorTests extends SharedMethodsForTests {

    private OpenAMFilterTranslator openAMFilterTranslator = new OpenAMFilterTranslator();

    @Test
    public void createFilterTranslator() {
        String andFilter = openAMFilterTranslator.createAndExpression("uid=testuid", "cn=test");
        assertEquals(andFilter, "(&(uid=testuid)(cn=test)");
        String orFilter = openAMFilterTranslator.createOrExpression("uid=testuid", "cn=test");
        assertEquals(orFilter, "(|(uid=testuid)(cn=test)");
    }
}
