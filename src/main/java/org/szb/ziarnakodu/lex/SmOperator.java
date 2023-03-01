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

public class SmOperator implements IStateMachine {
    private enum STATE {NEUTRAL, GT, LT}
    private STATE state;
    public SmOperator(){
        state = STATE.NEUTRAL;
    }
    @Override
    public ILEXTOKEN getToken() {
        return JsonGineLEXTOKEN.STRING;
    }
    @Override
    public boolean isStartChar(char ch) {
        return isComplete(ch, false)!=smState.FAIL;
    }
    @Override
    public int getAdvance() {
        return 1;
    }
    @Override
    public smState isComplete(char ch, boolean ... isEof) {
        if (isEof[0]) {
            return smState.FAIL;
        }
        switch(state) {
        case NEUTRAL:
            switch(ch) {
            case '>': state = STATE.GT; break;
            case '<': state = STATE.LT; break;
            default: return smState.FAIL;
            }
            break;
        case GT: // after: > 
            switch (ch) {
            case '>' : state=STATE.NEUTRAL    ; return smState.COMPLETED;
            default  : state=STATE.NEUTRAL    ; return smState.FAIL;
            }
        case LT: // after: <
            switch (ch) {
            case '<' : state=STATE.NEUTRAL    ; return smState.COMPLETED;
            default  : state=STATE.NEUTRAL    ; return smState.FAIL;
            }
        }
        return smState.INPROGRESS;
    }
}
