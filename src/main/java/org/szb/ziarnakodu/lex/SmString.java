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

public class SmString implements IStateMachine {
    private enum STATE {NEUTRAL, STRING, STRING_ESC}
    private STATE state;
    public SmString(){
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
            if (ch=='"') {
                state = STATE.STRING;
            }else {
                return smState.FAIL;
            }
            break;
        case STRING: // after: " 
            switch (ch) {
            case '"' : state=STATE.NEUTRAL    ; return smState.COMPLETED;
            case '\\': state=STATE.STRING_ESC ; break;
            }
            break;
        case STRING_ESC: // after: "...\
            state = STATE.STRING;
            break;
        }
        return smState.INPROGRESS;
    }
}
