/*
 * Copyright (c) 2022-present, by Szymon Blachuta and Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package org.szb.ziarnakodu.lex;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SmStringTest {
    private SmString smn = new SmString();
    
    private boolean parse(final String src) {
        final String terminated = src + IStateMachine.TRMCHAR; 
        if(!smn.isStartChar(src.charAt(0))) {
            return false;
        }
        for(int i=1; i<terminated.length(); ++i) {
            switch(smn.isComplete(terminated.charAt(i), false)) {
            case COMPLETED      : return true;
            case INPROGRESS     : break;
            case FAIL           : return false;
            }
        }
        //EOL will not be reached
        return false;
    }

    @Test
    public void test_00() throws Exception{
        assertFalse(Character.isJavaIdentifierPart(IStateMachine.TRMCHAR));
    }
    @Test
    public void test_01() throws Exception{
        assertTrue (parse("\"\""));
        assertTrue (parse("\"a\""));
        assertFalse(parse("a\""));
        assertFalse(parse("\"a"));
    }

}
