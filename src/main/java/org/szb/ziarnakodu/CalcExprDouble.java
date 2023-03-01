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

import java.util.Stack;

import static org.szb.ziarnakodu.ICalcExpr.EXPRTYPE.DBL;

public class CalcExprDouble implements ICalcExpr {
    private Stack<Object> vov = new Stack<>(); //value, op, value
    
    public CalcExprDouble() {
        vov = new Stack<>();
    }
    
    @Override
    public void collect(Object ov) { //lex.getLastTokenStr()
        vov.add(ov);
    }
    
    @Override
    public void calc() {
        while (vov.size()>1) {
            Double v = (Double)vov.pop();
            switch((String)vov.pop()) {
                case  "+": v+= (Double)vov.pop(); break; 
                case  "-": v-= (Double)vov.pop(); break; 
                case  "*": v*= (Double)vov.pop(); break; 
                case  "/": v/= (Double)vov.pop(); break; 
                case  "%": v%= (Double)vov.pop(); break; 
                //case  "|": v|= (Double)vov.pop(); break; 
                //case  "&": v&= (Double)vov.pop(); break; 
                //case  "^": v^= (Double)vov.pop(); break; 
                //case "<<": v<<=(Double)vov.pop(); break; 
                //case ">>": v>>=(Double)vov.pop(); break; 
            }
            vov.push(v);
        }
    }
    @Override
    public Object getResult() {
        calc();
        return vov.pop();
    }

    @Override
    public EXPRTYPE getType() {
        return DBL;
    }
}
