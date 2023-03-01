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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapTest {
    class CLSS{
        Map<String,String> m;
        CLSS(){
            m = new HashMap<>();
        }
    }
    @Test
    public void test_01() throws Exception{
        CLSS clss = new CLSS();
        JsonGineParser_04 parser = new JsonGineParser_04();
        parser.parse("jsongine(version=0.1){m:{\"kab\":\"ab\", \"kcd\":\"cd\"}}", clss);
        assertEquals("ab", clss.m.get("kab"));
        assertEquals("cd", clss.m.get("kcd"));
    }

}
