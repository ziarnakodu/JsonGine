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

package org.szb.ziarnakodu.lex;

import org.szb.ziarnakodu.JsonGineLEXTOKEN;

public class SmNumber implements IStateMachine{
	private enum STATE { NEUTRAL, INTEGER, DECIMAL} ;
    STATE state;
	
	public SmNumber(){
		state = STATE.NEUTRAL;
	}
    @Override
	public boolean isStartChar(char ch) {
        return isComplete(ch)!=smState.FAIL;
	}
    @Override
	public ILEXTOKEN getToken() {
	    return JsonGineLEXTOKEN.NUMBER;
	}
    @Override
    public int getAdvance() {
        return 0;
    }
	@Override
	public smState isComplete(char ch, boolean ... isEof) {
		switch(state) {
		case NEUTRAL:
	        if (ch=='.') {
	            state = STATE.DECIMAL;
	        }else
			if (Character.isDigit(ch)) {
			    if(ch=='0') {
			        // octal  01234567 octal number will be collected as integral and 0129 will be 'out of range'
			        // hex    0x12fd, x.5FP0F
			        // binary 0b0101
			    }
                state = STATE.INTEGER;
			}else {
                return smState.FAIL;
			}
			break;
		case INTEGER: // after 0..9
            if (!Character.isDigit(ch)) {
                switch (ch) {
                    case '.': state = STATE.DECIMAL; break;
                    case '_': break;
                    case 'e': 
                    case 'E': /*TODO*/break;
                    default:
                    state = STATE.NEUTRAL;
                    return smState.COMPLETED; // integer | octal
                }
            }
            break;
        case DECIMAL:
			if (!Character.isDigit(ch)) {
		        state = STATE.NEUTRAL;
			    return smState.COMPLETED;	
			}
			break;
		}
		return smState.INPROGRESS;
	}
//    public void examples(char charAt) {
//        double i[] = {0, 01, 1e1, 1e+1, 1E-1, 0129.0e1};
//    }
}
