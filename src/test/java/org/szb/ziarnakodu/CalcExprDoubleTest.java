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

public class CalcExprDoubleTest {
    @Test
    public void test_01() {
        ICalcExpr expr = new CalcExprDouble();
        expr.collect(1.2d);
        expr.collect((String)"+");
        expr.collect(2.3d);
        Double result = (Double)expr.getResult();
        assertEquals(3.5d, result.doubleValue(), 0.001);
    }
    @Test
    public void test_02() {
        ICalcExpr expr = new CalcExprDouble();
        expr.collect(3.0d);
        expr.collect("*");
        expr.collect(2.0d);
        expr.collect("+");
        expr.collect(1.0d);
        Double result = (Double)expr.getResult();
        assertEquals(9d, result.intValue(), 0.001);
    }
}
