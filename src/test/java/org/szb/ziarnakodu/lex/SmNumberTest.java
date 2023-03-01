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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SmNumberTest {
    private void numbers() {
        int i[] = {
                0b0101, //binary
                0x12fd, //hex
                   129, //decimal
                  0127, //octal
                1_123_456, // ignore any _ between digits
                //int id2 = _1; //not as first char
                //int id3 = 1_; // not as last char
                //int io_err01 = 0129; //non octal digits!
        };
        double d1[] = {
                1.2_2,
                //double d_err01 = .; //at least one digit
                //double d2 = 1._3; //wrong
                0b01010101, 
                09.129
        };
        float fl [] = {
                0x.5FP0F,
                0b01010101,
                123F,
                12.3F,
                .129F,
                0.129F,
                0x.5FP0F
        };
    }
    private boolean parse(final String numb) {
        final String terminated = numb + IStateMachine.TRMCHAR; 
        SmNumber sm = new SmNumber();
        assertTrue(sm.isStartChar(terminated.charAt(0)));
        for(int i=1; i<terminated.length(); ++i) {
            switch(sm.isComplete(terminated.charAt(i), false)) {
            case COMPLETED     : return true;
            case INPROGRESS    : break;
            case FAIL          : return true; //not possible
            }
        }
        return false; //still in progress?
    }
    @Test
    public void test_01() throws Exception{
        assertTrue(parse("1234"));
        assertTrue(parse("1.2"));
    }

}
