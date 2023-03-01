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

package org.szb.ziarnakodu.lex.test01;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Test01LexTest {
    
    private boolean check(Test01LexInterface/*ILex*/ lex, Test01Token tk, String txt) {
        if (tk != lex.next()) return false;
        if (!txt.equals(lex.getLastTokenStr())) return false;
        return true;
    }
    @Test
    public void test_01() {
        Test01Lex tlex = new Test01Lex("xaaaay", new SmAAAA(), new SmAAAB());
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "x"));
        assertTrue(check(tlex, Test01Token.TK_AAAA     , "aaaa"));
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "y"));
        assertTrue(check(tlex, Test01Token.ENDMARKER   , ""));
    }
    @Test
    public void test_02() {
        Test01Lex tlex = new Test01Lex("xaaay", new SmAAAA(), new SmAAAB());
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "x"));
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "a"));
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "a"));
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "a"));
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "y"));
        assertTrue(check(tlex, Test01Token.ENDMARKER   , ""));
    }
    @Test
    public void test_03() {
        Test01Lex tlex = new Test01Lex("xaaaby", new SmAAAA(), new SmAAAB());
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "x"));
        assertTrue(check(tlex, Test01Token.TK_AAAB     , "aaab"));
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "y"));
        assertTrue(check(tlex, Test01Token.ENDMARKER   , ""));
    }
    @Test
    public void test_04() {
        Test01Lex tlex = new Test01Lex("xaaaab", new SmAAAA(), new SmAAAB());
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "x"));
        assertTrue(check(tlex, Test01Token.TK_AAAA     , "aaaa"));
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "b"));
        assertTrue(check(tlex, Test01Token.ENDMARKER   , ""));
    }
    @Test
    public void test_unfinished_01() {
        Test01Lex tlex = new Test01Lex("xaaa", new SmAAAA(), new SmAAAB());
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "x"));
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "a"));
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "a"));
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "a"));
        assertTrue(check(tlex, Test01Token.ENDMARKER   , ""));
    }
    @Test
    public void test_unfinished_02() {
        Test01Lex tlex = new Test01Lex("xa", new SmAAAA(), new SmAAAB());
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "x"));
        assertTrue(check(tlex, Test01Token.TK_SIGLECHAR, "a"));
        assertTrue(check(tlex, Test01Token.ENDMARKER   , ""));
    }
    @Test
    public void test_unfinished_03() {
        Test01Lex tlex = new Test01Lex("cccc", new SmCplus());
        assertTrue(check(tlex, Test01Token.TK_Cplus  , "cccc"));
        assertTrue(check(tlex, Test01Token.ENDMARKER , ""    ));
    }
}
