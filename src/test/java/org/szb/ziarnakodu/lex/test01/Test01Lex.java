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

import static org.szb.ziarnakodu.lex.IStateMachine.TRMCHAR;

import java.util.ArrayList;
import java.util.List;

import org.szb.ziarnakodu.lex.ILEXTOKEN;
import org.szb.ziarnakodu.lex.IStateMachine;

public class Test01Lex implements Test01LexInterface/*ILex*/ {
	private String bbf;
	private int idx, idx0;
	private IStateMachine sma[];
	private List<IStateMachine> sml;
	private Test01Token lastTk;
    
    public Test01Lex(final String src, IStateMachine ... stateMachines ) {
    	idx = idx0 = 0;
    	bbf = src + TRMCHAR;
    	sma = stateMachines; //new IStateMachine[] {new SmAAAA(), new SmAAAB()};
    	lastTk = Test01Token.ENDMARKER;
    }
    private enum STATE {NEUTRAL, SM};
    	
    public Test01Token getLastToken() {
        return lastTk;
    }
    public String getLastTokenStr() {
        return bbf.substring(idx0, idx);
    }
    public ILEXTOKEN next() {
        STATE state = STATE.NEUTRAL;
    	char ch;
    	idx0 = idx;
        for(; idx<bbf.length(); ++idx) {
            ch = bbf.charAt(idx);
            switch(state) {
            case NEUTRAL:
                if (idx==bbf.length()-1) { //EOF reached, not completed ( /*EOF )
                    return ret(Test01Token.ENDMARKER);
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
                return ret(Test01Token.TK_SIGLECHAR); // []{}, ...
            case SM:
                boolean eof = (idx==bbf.length()-1); //EOF reached, not completed ( /*EOF )

                //try all state machines
                if (sml.size()>0) {
                    IStateMachine sm = sml.get(0);
                    switch (sm.isComplete(ch, eof)) {
                    case COMPLETED:
                        idx+=sm.getAdvance(); //return doesn't advance idx
                        return ret((Test01Token)sm.getToken()); 
                    case INPROGRESS:
                        break;
                    case FAIL:
                        idx = idx0;
                        sml.remove(0); //try next one state machine if any
                        break;
                    }
                }else {
                    idx = idx0+1;
                    return ret(Test01Token.TK_SIGLECHAR);
                }
                break;
            }

        }
        return ret(Test01Token.ENDMARKER);
    }
    private Test01Token ret(Test01Token tk) {
        lastTk = tk;
        return tk;
    }
}
