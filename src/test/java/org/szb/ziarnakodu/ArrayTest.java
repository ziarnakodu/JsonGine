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

public class ArrayTest {
    class CLSS{
        int a[];
    }
    @Test
    public void test_01() throws Exception{
        CLSS clss = new CLSS();
        JsonGineParser_04 parser = new JsonGineParser_04();
        parser.parse("jsongine(version=0.1){a:[1,2,3]}", clss);
        assertEquals(1, clss.a[0]);
        assertEquals(2, clss.a[1]);
        assertEquals(3, clss.a[2]);
    }
}
