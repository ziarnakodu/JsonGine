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

package org.szb.ziarnakodu.plugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.szb.ziarnakodu.JsonGineParser_04;

public class PluginTest {
    @Test
    public void test_expr01() throws Exception{
        if (System.getenv("FTP_HOST")==null) {
            return;
        }
        class Cexpr01  {String a;}
        Cexpr01 cexpr01 = new Cexpr01();
        new JsonGineParser_04().parse("jsongine(version=0.1) {a : org.szb.ziarnakodu.plugins.Crypto.decryptAES(\"FTP_HOST\") }", cexpr01);
        assertTrue(cexpr01.a.endsWith(".at")); //".at"
    }

    @Test
    public void test_expr02() throws Exception{
        if (System.getenv("FTP_HOST")==null) {
            return;
        }
        class Cexpr01  {int ii;}
        Cexpr01 cexpr01 = new Cexpr01();
        JsonGineParser_04 parser = new JsonGineParser_04();
        parser.parse("jsongine(version=0.1, X=org.szb.ziarnakodu.plugins.Crypto.decryptAES(\"FTP_HOST\")) {ii : 7 }", cexpr01);
        assertEquals("host-106865.webhosting.magentabusiness.at", parser.getProperties().get("X").toString());
        assertEquals(7, cexpr01.ii);
    }

    @Test
    public void test_expr03() throws Exception{
        class Cexpr03  {String ss;}
        Cexpr03 cexpr03 = new Cexpr03();
        JsonGineParser_04 parser = new JsonGineParser_04();
        parser.parse("jsongine(version=0.1) {ss : org.szb.ziarnakodu.plugins.TestPlugin.plugin1(\"xx\", \"y\") }", cexpr03);
        assertEquals("xxy", cexpr03.ss);
    }

}
