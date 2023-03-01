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

import org.szb.ziarnakodu.lex.ILEXTOKEN;
import org.szb.ziarnakodu.lex.IStateMachine;

public class SmCplus implements IStateMachine {
    private enum STATE {ST0, ST1};
    private STATE state;
	public SmCplus(){
	    state = STATE.ST0;
	}
    @Override
    public ILEXTOKEN getToken() {
        return Test01Token.TK_Cplus;
    }
    @Override
    public boolean isStartChar(char ch) {
        return isComplete(ch)!=smState.FAIL; //must be 'a'
    }
    @Override
    public int getAdvance() {
        return 0;
    }
	@Override
	public smState isComplete(char ch, boolean ... isEof) {
	    switch(state) {
	    case ST0: 
	        if (ch!='c' || (isEof.length>0 && isEof[0])) {
	            return smState.FAIL;
	        } 
	        state = STATE.ST1; 
	        return smState.INPROGRESS;
        case ST1: 
            if (ch!='c'|| isEof[0]) {
                state = STATE.ST0; 
                return smState.COMPLETED;
            }else {
                return smState.INPROGRESS;
            }
	    }
	    //unreachable
		return null;
	}
}
