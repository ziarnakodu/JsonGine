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

import static org.szb.ziarnakodu.JsonGineLEXTOKEN.ENDMARKER;
import static org.szb.ziarnakodu.JsonGineLEXTOKEN.IDENT;
import static org.szb.ziarnakodu.JsonGineLEXTOKEN.NUMBER;
import static org.szb.ziarnakodu.JsonGineLEXTOKEN.OPERATOR;
import static org.szb.ziarnakodu.JsonGineLEXTOKEN.PROPERTY;
import static org.szb.ziarnakodu.JsonGineLEXTOKEN.STRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.szb.ziarnakodu.lex.ILex;

class D{
    JsonGineLEXTOKEN tk;
    String val;
    D(JsonGineLEXTOKEN tk0, String val0){
        tk= tk0;
        val = val0;
    }
}
public class JsonGineLexTest {
//    @Test
//    public void test_00() throws Exception{
//        for(int x=0; x<0xFF; ++x) {
//            //System.out.println("----- "+(char)x+" "+(Character.isJavaIdentifierPart(x)?"true":"false"));
//        }
//        
//    }
    private boolean check(final String src, JsonGineLEXTOKEN tk, final String ... va ) {
        JsonGineLex lex = new JsonGineLex(src);
        boolean b1, b2;
        b1 = b2 = true;
        for (String v : va) {
            b1 &= (tk == lex.next());
            if (!b1) {
                System.out.println("...tk="+tk);
            }
            String v1 =lex.getLastTokenStr();
            b2 &= v.equals(v1);
            if (!b2) {
                System.out.println("...v="+v+ ", "+v1);
            }
        }
        assertEquals(ENDMARKER, lex.next());
        return b1 && b2;
    }
    private boolean check(final String src, D ... d) {
        boolean ret = true;
        JsonGineLex lex = new JsonGineLex(src);
        for(D d0 : d) {
            boolean rt1 = (d0.tk == lex.next());
            ret &= rt1;
            String v2 = lex.getLastTokenStr();
            boolean rt2 = (d0.val.equals(v2));
            ret &= rt2;
        }
        assertEquals(ENDMARKER, lex.next());
        assertEquals("", lex.getLastTokenStr());
        return ret;
    }

    @Test
    public void test_EOF() throws Exception{
        JsonGineLex lex;
        assertTrue(check(""     , ENDMARKER));
        assertTrue(check("1"    , NUMBER, "1"));
        assertTrue(check("\"\"" , STRING, "\"\""));
        assertTrue(check("\"a\"", STRING, "\"a\""));
        
        lex = new JsonGineLex("a");
        assertEquals(IDENT, lex.next());
        assertEquals(ENDMARKER, lex.next());
        assertEquals("", lex.getLastTokenStr());
        
        lex = new JsonGineLex("(");
        assertEquals(OPERATOR, lex.next());
        assertEquals(ENDMARKER, lex.next());
        assertEquals("", lex.getLastTokenStr());
    }
    @Test
    public void test_IDENT() throws Exception{
        assertTrue(check(""      , ENDMARKER, ""));
        assertTrue(check("a"     , IDENT    , "a"));
        assertTrue(check(" b"    , IDENT    , "b"));
        assertTrue(check(" b "   , IDENT    , "b"));
        assertTrue(check("bc"    , IDENT    , "bc"));
        assertTrue(check("bäc"   , IDENT    , "bäc"));
        assertTrue(check("/*a*/bc//d\r\ne"     , new D(IDENT, "bc"), new D(IDENT, "e")));
        assertTrue(check(" /*a*/ bc //d \r\n e", new D(IDENT, "bc"), new D(IDENT, "e")));
    }
    @Test
    public void test_NUMBER() throws Exception{
        JsonGineLex lex;
        lex = new JsonGineLex("1");
        assertEquals(NUMBER    , lex.next());
        assertEquals(ENDMARKER , lex.next());
        
        lex = new JsonGineLex("12");
        assertEquals(NUMBER    , lex.next());
        assertEquals(ENDMARKER , lex.next());
        
        lex = new JsonGineLex("1.2");
        assertEquals(NUMBER    , lex.next());
        assertEquals(ENDMARKER , lex.next());
        
        lex = new JsonGineLex("1.32");
        assertEquals(NUMBER    , lex.next());
        assertEquals(ENDMARKER , lex.next());
        
        lex = new JsonGineLex("1.32 4");
        assertEquals(NUMBER    , lex.next());
        assertEquals(NUMBER    , lex.next());
        assertEquals(ENDMARKER , lex.next());
        
        lex = new JsonGineLex("0.1)");
        assertEquals(NUMBER    , lex.next());
        assertEquals(OPERATOR  , lex.next());
        assertEquals(ENDMARKER , lex.next());
    }
    @Test
    public void test_String() throws Exception{
        JsonGineLex lex = new JsonGineLex("\"\"");
        assertEquals(STRING    , lex.next());
        assertEquals(ENDMARKER , lex.next());
        
        lex = new JsonGineLex("\"1\"");
        assertEquals(STRING    , lex.next());
        assertEquals(ENDMARKER , lex.next());
        
        lex = new JsonGineLex("\"12\"");
        assertEquals(STRING    , lex.next());
        assertEquals(ENDMARKER , lex.next());
        
        lex = new JsonGineLex(  "\"12\"  //abc \r");
        assertEquals(STRING    , lex.next());
        assertEquals(ENDMARKER , lex.next());
        
        lex = new JsonGineLex("\"1\\\"2\"");
        assertEquals(STRING    , lex.next());
        assertEquals(ENDMARKER , lex.next());
        
    }
    @Test
    public void test_Operator() throws Exception{
        assertTrue(check(") " , OPERATOR, ")"));
        assertTrue(check(" )" , OPERATOR, ")"));
        assertTrue(check(" ( ", OPERATOR, "("));
        assertTrue(check(" { ", OPERATOR, "{"));
        assertTrue(check(" [ ", OPERATOR, "["));
        assertTrue(check(" / ", OPERATOR, "/"));
        assertTrue(check(" * ", OPERATOR, "*"));

        assertTrue(check("/-" , new D(OPERATOR, "/"), new D(OPERATOR, "-")));
        assertTrue(check(" /-", new D(OPERATOR, "/"), new D(OPERATOR, "-")));
        assertTrue(check("/- ", new D(OPERATOR, "/"), new D(OPERATOR, "-")));
    }
    @Test
    public void test_Comment() throws Exception{
        JsonGineLex lex = new JsonGineLex("(/*a*/");
        assertEquals(OPERATOR  , lex.next());
        assertEquals(ENDMARKER , lex.next());
        
        lex = new JsonGineLex(" x //a\r");
        assertEquals(IDENT     , lex.next());
        assertEquals(ENDMARKER , lex.next());
        
        lex = new JsonGineLex(" [ //");
        assertEquals(OPERATOR  , lex.next());
        assertEquals(ENDMARKER , lex.next());
    }
    private void test_next0(final String src) {
        JsonGineLex lex = new JsonGineLex(src);
        JsonGineLEXTOKEN tk;
        StringBuffer bf = new StringBuffer();
        do {
            tk = (JsonGineLEXTOKEN)lex.next0();
            bf.append(lex.getLastEntity());
        }while(tk!=ENDMARKER);
        assertEquals(bf.toString(), src);
    }
    @Test
    public void test_text0() throws Exception{
        test_next0("a/") ; // slash not completed
        test_next0("a\\"); //ESC not completed
        test_next0("");
        test_next0("a");
        test_next0(" a");
        test_next0("a ");
        test_next0(" a ");
        test_next0("/*a");
        test_next0("(/*a*/");
        test_next0("(/*a*/ v(v 1.0){a:\"ab\",b:\"bc\"}\r\n {a:1, b:[1,2,3]}\r\n");
    }
    @Test
    public void test_getLastToken() throws Exception{
        String strng = "\"ab\"";
        ILex lex = new JsonGineLex(strng);
        lex.next();
        assertEquals(strng, lex.getLastTokenStr());
    }
    @Test
    public void test_property() throws Exception{
        String strng = "#A";
        JsonGineLex lex = new JsonGineLex(strng);
        assertEquals(lex.next0(), PROPERTY);
        
        strng = "#A\"\"";
        lex = new JsonGineLex(strng);
        assertEquals(lex.next0(), PROPERTY);
        assertEquals("#A", lex.getLastEntity());
        assertEquals(lex.next0(), STRING);
        assertEquals("\"\"", lex.getLastEntity());
    }
    @Test
    public void test_characterIs() throws Exception{
        assertFalse(Character.isSpaceChar('\r'));
        assertFalse(Character.isSpaceChar('\n'));
        assertTrue(Character.isWhitespace('\r'));
        assertTrue(Character.isWhitespace('\n'));
        assertTrue(Character.isWhitespace(' '));
        assertTrue(Character.isWhitespace('\t'));
    }
}
