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

public class SmComment implements IStateMachine {
    // strategy: /*...EOF => completed
    private enum STATE {NEUTRAL, COMMENT_SL, COMMENT_INLINE, COMMENT_EOF, COMMENT_INL_ASTERIX}
    private STATE state;
    private int advance;
	public SmComment(){
		state = STATE.NEUTRAL;
		advance = 0;
	}
    @Override
    public ILEXTOKEN getToken() {
        return JsonGineLEXTOKEN.COMMENT;
    }
    @Override
    public boolean isStartChar(char ch) {
        return isComplete(ch, false)!=smState.FAIL;
    }
    @Override
    public int getAdvance() {
        return advance;
    }
	@Override
	public smState isComplete(char ch, boolean ... isEOF) {
		switch(state) {
        case NEUTRAL: // after: /
            if (ch=='/') {
                state = STATE.COMMENT_SL;
            }else {
                return smState.FAIL;
            }
            break;
        case COMMENT_SL: // after: /
            switch(ch) {
            case '*': state = STATE.COMMENT_INLINE; advance = 1; break;
            case '/': state = STATE.COMMENT_EOF   ; advance = 0; break;
            default : state = STATE.NEUTRAL       ;return smState.FAIL;
            }
            break;
        case COMMENT_INLINE: // after: /*
            if (isEOF[0]) {
                advance=0;
                state = STATE.NEUTRAL;
                return smState.COMPLETED;
            }
            if (ch=='*') {
                state=STATE.COMMENT_INL_ASTERIX;
            }
            break;
        case COMMENT_INL_ASTERIX: // after: /*....*
            if (isEOF[0]) {
                advance=0;
                state = STATE.NEUTRAL;
                return smState.COMPLETED;
            }
            switch(ch) {
            case '/': state = STATE.NEUTRAL            ; return smState.COMPLETED;
            case '*': state = STATE.COMMENT_INL_ASTERIX; break;
            default : state = STATE.COMMENT_INLINE     ; break;
            }
            break;
        case COMMENT_EOF: // after: //
            if (ch==0x0A || ch==0x0D || isEOF[0]) {
                state = STATE.NEUTRAL;
                return smState.COMPLETED;
            }
            break;
		}
		return smState.INPROGRESS;
	}

}
