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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalcExprIntegerTest {
    @Test
    public void test_01() {
        ICalcExpr expr = new CalcExprInteger();
        expr.collect((Integer)1);
        expr.collect("+");
        expr.collect((Integer)2);
        Integer result = (Integer)expr.getResult();
        assertEquals(3, result.intValue());
    }
    @Test
    public void test_02() {
        ICalcExpr expr = new CalcExprInteger();
        expr.collect((Integer)3);
        expr.collect("*");
        expr.collect((Integer)2);
        expr.collect("+");
        expr.collect((Integer)1);
        Integer result = (Integer)expr.getResult();
        assertEquals(9, result.intValue());
    }
}
