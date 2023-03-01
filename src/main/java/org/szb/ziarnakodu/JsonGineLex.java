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

import static org.szb.ziarnakodu.JsonGineLEXTOKEN.COMMENT;
import static org.szb.ziarnakodu.JsonGineLEXTOKEN.ENDMARKER;
import static org.szb.ziarnakodu.JsonGineLEXTOKEN.NUMBER;
import static org.szb.ziarnakodu.JsonGineLEXTOKEN.OPERATOR;
import static org.szb.ziarnakodu.JsonGineLEXTOKEN.PROPERTY;
import static org.szb.ziarnakodu.JsonGineLEXTOKEN.STRING;
import static org.szb.ziarnakodu.JsonGineLEXTOKEN.WS;
import static org.szb.ziarnakodu.lex.IStateMachine.TRMCHAR;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.szb.ziarnakodu.lex.ILEXTOKEN;
import org.szb.ziarnakodu.lex.ILex;
import org.szb.ziarnakodu.lex.IStateMachine;
import org.szb.ziarnakodu.lex.SmComment;
import org.szb.ziarnakodu.lex.SmIdent;
import org.szb.ziarnakodu.lex.SmNumber;
import org.szb.ziarnakodu.lex.SmOperator;
import org.szb.ziarnakodu.lex.SmProperty;
import org.szb.ziarnakodu.lex.SmString;
import org.szb.ziarnakodu.lex.SmWS;
/** 
 * Extended JSON -jsongine
 * 
 * @author Szymon Blachuta
 * 
 */

public class JsonGineLex implements ILex {
	String bbf;
	int idx, idx0;
	Properties properties; 
    IStateMachine sma[];
    List<IStateMachine> sml;
    JsonGineLEXTOKEN lastTk = ENDMARKER;
    String lastTkStr;
    
    public JsonGineLex(final String src) {
    	idx = idx0 = 0;
    	bbf = src + TRMCHAR;
    	sma = new IStateMachine[] {
    	        new SmComment() , // /*...*/, //...EOL
    	        new SmIdent()   , // Abc_X, $XY, ...
    	        new SmNumber()  , // 123, 1.23f, ...
    	        new SmString()  , // "abc"
    	        new SmWS()      , // White Space
    	        new SmProperty(), // #Name
                new SmOperator()  // '>>', '<<'
    	        };
    }
    public void setProperties(Properties p){
        properties = p;
    }
    private enum STATE {NEUTRAL, SM};
    	
    public JsonGineLEXTOKEN next() {
        JsonGineLEXTOKEN rto;
        do {
            rto = (JsonGineLEXTOKEN)next0();
        }while(rto==COMMENT || rto== WS);
        if (rto==PROPERTY && properties!=null) {
            String ret = (String)properties.get(bbf.substring(idx0+1, idx));
            if (ret!=null) {
                lastTkStr = ret;
                lastTk = Character.isDigit(ret.charAt(0))? NUMBER : STRING; //TODO .12?
                return lastTk; 
            }
        }
        lastTkStr = bbf.substring(idx0, idx);
        lastTk = rto;
        return rto;
    }
    public JsonGineLEXTOKEN getLastToken() {
        return lastTk;
    }
    public String getLastTokenStr() {
        return lastTkStr;
    }
    public String getLastEntity() {
        return bbf.substring(idx0, idx);
    }
    protected ILEXTOKEN next0() {
        STATE state = STATE.NEUTRAL;
    	char ch;
    	boolean eof;
    	idx0 = idx;
        for(; idx<bbf.length(); ++idx) {
            ch = bbf.charAt(idx);
            eof = (idx==bbf.length()-1); //EOF reached
            switch(state) {
            case NEUTRAL:
                if (eof) { //EOF reached, not completed ( /*EOF )
                    return ret(ENDMARKER);
                }
                sml = new ArrayList<>(); 
                for(IStateMachine ism : sma) {
                    if (ism.isStartChar(ch)) {
                        sml.add(ism);
                    }
                }
                if (sml.size()>0) {
                    state = STATE.SM;
                    break;
                }
                idx++;
                return ret(OPERATOR); // []{}, ...
            case SM:
                //try all state machines
                //if (sml.size()==0) {
                //    return ret(ENDMARKER);
                //}
                IStateMachine sm = sml.get(0);
                switch (sm.isComplete(ch, eof)) {
                case COMPLETED:
                    idx+=sm.getAdvance(); //return doesn't advance idx
                    return ret((JsonGineLEXTOKEN)sm.getToken()); 
                case INPROGRESS:
                    break;
                case FAIL:
                    sml.remove(0); //try next one state machine if any
                    if(sml.size()==0) {
                        return ret(OPERATOR);
                    }
                    // start again with next sm
                    idx = idx0;
                    break;
                }
                break;
            }

        }
        return ret(ENDMARKER);
    }
    private JsonGineLEXTOKEN ret(JsonGineLEXTOKEN tk) {
        lastTk = tk;
        return tk;
    }
}
