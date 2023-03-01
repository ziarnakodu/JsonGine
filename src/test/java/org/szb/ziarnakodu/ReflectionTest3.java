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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ReflectionTest3 {
    
    private class First {
        public Map<String, String> MAP = new HashMap<>();
        First(){
            MAP.put("A", "1");
            MAP.put("B", "2");
        }
    }
     
    @Test
    public void test01() throws Exception {
        Class<?> clazz = Class.forName("org.szb.ziarnakodu.First");
        Field field = clazz.getField("MAP");
        Map<String, String> newMap = (HashMap<String, String>) field.get(clazz);
        assertEquals("{A=1, B=2}", newMap.toString());
    }
}
