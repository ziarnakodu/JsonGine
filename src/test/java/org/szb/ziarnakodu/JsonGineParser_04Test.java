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

package org.szb.ziarnakodu;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonGineParser_04Test {
    @Test
    public void test_01() throws Exception{
        class C01   {String a;}
        C01 c01 = new C01();
        new JsonGineParser_04().parse("jsongine(version=0.1) {a : \"123\"}", c01);
        assertEquals("123", c01.a);
    }
    @Test
    public void test_02() throws Exception{
        class C02 {int a;}
        C02 c02 = new C02();
        new JsonGineParser_04().parse("jsongine(version=0.1) {a : 123}", c02);
        assertEquals(123, c02.a);
    }
    @Test
    public void test_03() throws Exception{
        class C02b {String c;}
        class C02a {String a; C02b b=new C02b();}
        C02a c02a = new C02a();
        new JsonGineParser_04().parse("jsongine(version=0.1) {a : \"123\", b : {c: \"321\"} }", c02a, c02a.b);
        assertEquals("123", c02a.a);
        assertEquals("321", c02a.b.c);
    }
    @Test
    public void test_04() throws Exception{
        class C03b /*implements ITargetClass*/ {String c;}
        class C03a /*implements ITargetClass*/ {String a; C03b b=new C03b(); int d; }
        C03a c03a = new C03a();
        new JsonGineParser_04().parse("jsongine(version=0.1) {a : \"123\", b : {c: \"321\"}, d : 17 }", c03a, c03a.b);
        assertEquals("123", c03a.a);
        assertEquals("321", c03a.b.c);
        assertEquals(   17, c03a.d);
    }
    @Test
    public void test_expr01() throws Exception{
        class Cexpr01  {int a;}
        Cexpr01 cexpr01 = new Cexpr01();
        new JsonGineParser_04().parse("jsongine(version=0.1) {a : 1+2*3 }", cexpr01);
        assertEquals(7, cexpr01.a);
    }
    @Test
    public void test_expr02() throws Exception{
        class Cexpr01  {int a;}
        Cexpr01 cexpr01 = new Cexpr01();
        new JsonGineParser_04().parse("jsongine(version=0.1) {a : (1+2)*3 }", cexpr01);
        assertEquals(9, cexpr01.a);
    }
}