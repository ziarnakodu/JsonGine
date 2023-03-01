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
import static org.szb.ziarnakodu.JsonGineLEXTOKEN.STRING;

import java.util.ArrayList;

import org.szb.ziarnakodu.lib.SyntaxInvalid;
/**
 *
 * GeneratedParser_GrammarTest_jsonGine_05
 */
public class JsonGineParser_04 extends JsonGineBase {
    
    public void parse(String src, Object ... aObj)  throws SyntaxInvalid {
        /*-->*/init(src, aObj);
        start_def();
        expectTOKEN(ENDMARKER);
    }

    /**
     * dotted_name:  IDENT   (  '.'   IDENT   )*  
     */
    protected void dotted_name() throws SyntaxInvalid {
        /*-->*/dottedName.add();
        expectTOKEN(IDENT);
        while(!isEOF() && isOneOf(".")){
            expectCONST(".");
            /*-->*/dottedName.add();
            expectTOKEN(IDENT);
        }
    }
    /**
     * constant:  NUMBER  |  STRING  |  plugin_call  
     */
    protected void constant() throws SyntaxInvalid {
        if (isOneOf(NUMBER)){
            /*-->*/setProp();
            expectTOKEN(NUMBER);
        }
        else
        if (isOneOf(STRING)){
            /*-->*/setProp();
            expectTOKEN(STRING);
        }
        else
        if (isOneOf(IDENT)){
            plugin_call();
        }
        else throw new SyntaxInvalid("in constant");
        /*-->*/dottedName.init(); //reset after use
    }
    /**
     * start_def:  'jsongine'   '('   'version'   '='   NUMBER   (  ','   prop   )*   ')'   value  
     */
    protected void start_def() throws SyntaxInvalid {
        expectCONST("jsongine");
        expectCONST("(");
        /*-->*/isPropertiesMode = true;
        expectCONST("version");
        expectCONST("=");
        expectTOKEN(NUMBER);
        while(!isEOF() && isOneOf(",")){
            expectCONST(",");
            prop();
        }
        /*-->*/setProperties(getProperties());
        /*-->*/isPropertiesMode = false;
        expectCONST(")");
        value();
    }
    /**
     * xor_expr:  and_expr   (  '^'   and_expr   )*  
     */
    protected void xor_expr() throws SyntaxInvalid {
        and_expr();
        while(!isEOF() && isOneOf("^")){
            /*-->*/collectStr();
            expectCONST("^");
            and_expr();
        }
    }
    /**
     * num_expr:  xor_expr   (  '|'   xor_expr   )*  
     */
    protected void num_expr() throws SyntaxInvalid {
        xor_expr();
        while(!isEOF() && isOneOf("|")){
            /*-->*/collectStr();
            expectCONST("|");
            xor_expr();
        }
    }
    /**
     * shift_expr:  arith_expr   (  (  '<<'  |  '>>'   )   arith_expr   )*  
     */
    protected void shift_expr() throws SyntaxInvalid {
        arith_expr();
        while(!isEOF() && isOneOf("<<",">>")){ //TODO SmOperator
            /*-->*/collectStr();
            expectOneOf("<<",">>");
            arith_expr();
        }
    }
    /**
     * string_expr:  STRING   (  '+'   STRING   )*  
     */
    protected void string_expr() throws SyntaxInvalid {
        /*-->*/collectStr();
        expectTOKEN(STRING);
        while(!isEOF() && isOneOf("+")){
            expectCONST("+");
            /*-->*/collectStr();
            expectTOKEN(STRING);
        }
    }
    /**
     * val_expr:  num_expr  |  string_expr  
     */
    protected void val_expr() throws SyntaxInvalid {
        if (isOneOf(NUMBER) || isOneOf("(","+","-")){
            /*-->*/newNumExpr();
            num_expr();
        }
        else
        if (isOneOf(STRING)){
            /*-->*/newStrExpr();
            string_expr();
        }
        else throw new SyntaxInvalid("in val_expr");
        /*-->*/setValue();
    }
    /**
     * and_expr:  shift_expr   (  '&'   shift_expr   )*  
     */
    protected void and_expr() throws SyntaxInvalid {
        shift_expr();
        while(!isEOF() && isOneOf("&")){
            /*-->*/collectStr();
            expectCONST("&");
            shift_expr();
        }
    }
    /**
     * array:  '['   [  val_expr   (  ','   val_expr   )*   ]   ']'  
     */
    protected void array() throws SyntaxInvalid {
        expectCONST("[");
        /*-->*/arrResults = new ArrayList<>();
        if (isOneOf(NUMBER,STRING) || isOneOf("(","+","-")){
            val_expr();
            while(!isEOF() && isOneOf(",")){
                expectCONST(",");
                val_expr();
            }
        }
        /*-->*/saveArr();
        expectCONST("]");
    }
    /**
     * arith_expr:  term   (  (  '+'  |  '-'   )   term   )*  
     */
    protected void arith_expr() throws SyntaxInvalid {
        term();
        while(!isEOF() && isOneOf("+","-")){
            /*-->*/collectStr();
            expectOneOf("+","-");
            term();
        }
    }
    /**
     * prop:  dotted_name   '='   constant  
     */
    protected void prop() throws SyntaxInvalid {
        dotted_name();
        expectCONST("=");
        constant();
    }
    /**
     * member:  STRING  |  IDENT   ':'   value  
     */
    protected void member() throws SyntaxInvalid {
        if (isOneOf(STRING)){
            /*-->*/setIdent();
            expectTOKEN(STRING);
        }
        else
        if (isOneOf(IDENT)){
            /*-->*/setIdent();
            expectTOKEN(IDENT);
        }
        else throw new SyntaxInvalid("in member");
        expectCONST(":");
        value();
    }
    /**
     * expr:  val_expr  |  plugin_call  
     */
    protected void expr() throws SyntaxInvalid {
        if (isOneOf(NUMBER,STRING) || isOneOf("(","+","-")){
            val_expr();
        }
        else
        if (isOneOf(IDENT)){
            plugin_call();
        }
        else throw new SyntaxInvalid("in expr");
    }
    /**
     * term:  factor   (  (  '*'  |  '/'  |  '%'   )   factor   )*  
     */
    protected void term() throws SyntaxInvalid {
        factor();
        while(!isEOF() && isOneOf("%","*","/")){
            /*-->*/collectStr();
            expectOneOf("%","*","/");
            factor();
        }
    }
    /**
     * factor:  [  '+'  |  '-'   ]   NUMBER  |  '('   num_expr   ')'  
     */
    protected void factor() throws SyntaxInvalid {
        if (isOneOf(NUMBER) || isOneOf("+","-")){
            if (isOneOf("+","-")){
                expectOneOf("+","-");
            }
            /*-->*/collect();
            expectTOKEN(NUMBER);
        }else
        if (isOneOf("(")){
            expectCONST("(");
            num_expr();
            /*-->*/calcExpr.calc();
            expectCONST(")");
        }else {
            throw new SyntaxInvalid("in factor");
        }
    }
    /**
     * plugin_call:  dotted_name   '('   [  val_expr   (  ','   val_expr   )*   ]   ')'  
     */
    protected void plugin_call() throws SyntaxInvalid {
        if (/*-->*/isPropertiesMode) {
            /*-->*/ident = /*-->*/dottedName.getFullName();
        }
        /*-->*/dottedName.init();
        dotted_name();
        expectCONST("(");
        if (isOneOf(NUMBER,STRING) || isOneOf("(","+","-")){
            val_expr();
            while(!isEOF() && isOneOf(",")){
                expectCONST(",");
                val_expr();
            }
        }
        expectCONST(")");
        /*-->*/callPlugin();
    }
    /**
     * value:  object  |  array  |  expr  |  'true'  |  'false'  |  'null'  
     */
    protected void value() throws SyntaxInvalid {
        if (isOneOf("{")){
            object();
        }
        else
        if (isOneOf("[")){
            array();
        }
        else
        if (isOneOf(NUMBER,STRING,IDENT) || isOneOf("(","+","-")){
            expr();
        }
        else
        if (isOneOf("true")){
            expectCONST("true");
        }
        else
        if (isOneOf("false")){
            expectCONST("false");
        }
        else
        if (isOneOf("null")){
            expectCONST("null");
        }
        else throw new SyntaxInvalid("in value");
    }
    /**
     * object:  '{'   [  member   (  ','   member   )*   ]   '}'  
     */
    protected void object() throws SyntaxInvalid {
        expectCONST("{");
        if (isOneOf(IDENT,STRING)){
            member();
            while(!isEOF() && isOneOf(",")){
                expectCONST(",");
                member();
            }
        }
        expectCONST("}");
        /*-->*/setMap();
        /*-->*/objType = null; //reset
    }
}
