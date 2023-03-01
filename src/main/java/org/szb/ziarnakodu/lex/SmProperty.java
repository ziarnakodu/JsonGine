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

public class SmProperty implements IStateMachine {
    private enum STATE {NEUTRAL, NEUTRAL_1, IDENT};
    private STATE state;
    public SmProperty() {
        state = STATE.NEUTRAL;
    }
    @Override
    public ILEXTOKEN getToken() {
        return JsonGineLEXTOKEN.PROPERTY;
    }
    @Override
    public boolean isStartChar(char ch) {
        return isComplete(ch, false)!=smState.FAIL;
    }
    @Override
    public int getAdvance() {
        return 0;
    }
	@Override
	public smState isComplete(char ch, boolean ... isEof) {
	    switch(state) {
	    case NEUTRAL:
	        if (ch!='#') {
	            return smState.FAIL;
	        }
	        state = STATE.NEUTRAL_1;
	        break;
	    case NEUTRAL_1: //after #
	        if (!Character.isJavaIdentifierStart(ch)) {
                state = STATE.NEUTRAL;
	            return smState.FAIL;
	        }
	        state = STATE.IDENT;
	        break;
	    case IDENT:
	        if (!Character.isJavaIdentifierPart(ch) || isEof[0]) {
	            state = STATE.NEUTRAL;
	            return smState.COMPLETED;
	        }
	        break;
	    }
		return smState.INPROGRESS;
	}
}
