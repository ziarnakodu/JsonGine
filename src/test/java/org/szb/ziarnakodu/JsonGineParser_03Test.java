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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Test;

class Staff { //https://mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
    private String name;
    private int age;
    private String[] position;              // array
    private List<String> skills;            // list
    private Map<String, BigDecimal> salary; // map
    //getters and setters
}
//{ //product
//    id: 321,
//    name: "The Best Product",
//    brand: {
//        id: 4533,
//        name: "Oxmon Products",
//        owner: {
//            id: 533,
//            name: "Oxmon Corp, Inc."
//        }
//    }  
//}
class Owner{
    int id;
    String name;
}
class Brand{
    int id;
    String name;
    Owner owner;
    Brand(){owner = new Owner();}
}
class Product{
    int id;
    String name;
    Brand brand;
    Product(){brand = new Brand();}
}

public class JsonGineParser_03Test {
    @Test
    public void test_00() throws Exception{
        assertEquals("a", "a");
    }
//    //@Test
//    public void test_01() throws Exception{
//        class C01   {String a;}
//        XJsonLex lex = new XJsonLex("xJson(version=1.0) {a : \"123\"}");
//        C01 c01 = new C01();
//        new XJsonParser_03().parse(lex, c01);
//        assertEquals("123", c01.a);
//    }
//    
//    //@Test
//    public void test_02() throws Exception{
//        class C02b   {String c;}
//        class C02a   {String a; C02b b=new C02b();}
//        XJsonLex lex = new XJsonLex("xJson(version=1.0) {a : \"123\", b : {c: \"321\"} }");
//        C02a c02a = new C02a();
//        new XJsonParser_03().parse(lex, c02a, c02a.b);
//        assertEquals("123", c02a.a);
//        assertEquals("321", c02a.b.c);
//    }
//    
//    //@Test
//    public void test_03() throws Exception{
//        class C03b   {String c;}
//        class C03a   {String a; C03b b=new C03b(); int d; }
//        XJsonLex lex = new XJsonLex("xJson(version=1.0) {a : \"123\", b : {c: \"321\"}, d : 17 }");
//        C03a c03a = new C03a();
//        new XJsonParser_03().parse(lex, c03a, c03a.b);
//        assertEquals("123", c03a.a);
//        assertEquals("321", c03a.b.c);
//        assertEquals(   17, c03a.d);
//    }
//    
//    //@Test
//    public void test_expr01() throws Exception{
//        class Cexpr01  {int a;}
//        Cexpr01 cexpr01 = new Cexpr01();
//        final String src = "xJson(version=1.0) {a : 1+2*3 }";
//        new XJsonParser_03().parse(src, cexpr01);
//        assertEquals(7, cexpr01.a);
//    }
//    @Test
//    public void test_expr02() throws Exception{
//        class Cexpr01  {int a;}
//        Cexpr01 cexpr01 = new Cexpr01();
//        final String src = "xJson(version=1.0) {a : (1+2)*3 }";
//        new XJsonParser_03().parse(src, cexpr01);
//        assertEquals(9, cexpr01.a);
//    }
}