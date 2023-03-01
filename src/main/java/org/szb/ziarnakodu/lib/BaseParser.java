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

package org.szb.ziarnakodu.lib;

import org.szb.ziarnakodu.lex.ILEXTOKEN;
import org.szb.ziarnakodu.lex.ILex;

public class BaseParser{
    protected ILex lex;
    public void init(ILex aLex) {
        lex = aLex;
        lex.next();
    }
    public String getLastTokenStr() {
        return lex.getLastTokenStr();
    }
    public void  expectCONST(final String s) throws SyntaxInvalid {
        if (!lex.getLastTokenStr().equals(s)) {
            throw new SyntaxInvalid("TODO 1 "+lex.getLastTokenStr()+"  "+s);
        }
        lex.next();
    }
    public void  expectCONST(ILEXTOKEN tk) throws SyntaxInvalid {
        if (lex.getLastToken()!=tk) {
            throw new SyntaxInvalid("TODO 2");
        }
        lex.next();
    }
    public void  expect(ILEXTOKEN tk, final String s) throws SyntaxInvalid {
        if (lex.getLastToken()!=tk || !lex.getLastTokenStr().equals(s)) {
            throw new SyntaxInvalid("TODO 3 "+lex.getLastTokenStr()+"  "+s);
        }
        lex.next();
    }
    public void expectTOKEN(ILEXTOKEN tk) throws SyntaxInvalid {
        ILEXTOKEN aTk = lex.getLastToken();
        if (aTk!=tk) {
            throw new SyntaxInvalid("Invalid Syntax TODO 4");
        }
        lex.next();
    }
    public boolean isEOF() {
        return "".equals(lex.getLastTokenStr());
    }
    public boolean isOneOf(final ILEXTOKEN ... tks   ) throws SyntaxInvalid  {
        ILEXTOKEN act = lex.getLastToken();
        for(ILEXTOKEN tk : tks) {
            if (tk.equals(act)) {
                return true;
            }
        }
        return false;
    }
    public boolean isOneOf(final String ... strings ) throws SyntaxInvalid  {
        String act = lex.getLastTokenStr();
        for(String av : strings) {
            if (av.equals(act)) {
                return true;
            }
        }
        return false;
    }
    public void expectOneOf(final String ... strings ) throws SyntaxInvalid  {
        isOneOf(strings); //TODO false => throw Exception
        lex.next();
    }
    public static String processString(String s) {
        // TODO resolve \r\n\t 
        return s.substring(1, s.length()-1); //strip quotes
    }
}
