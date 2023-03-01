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

package org.szb.ziarnakodu.plugins;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

public class CryptoTest {
    
    private void setEnv() {
        System.setProperty("MY_USER" , "2cRKUkRRHV3lhfi2BSnS8A=="); //export MY_USER=2cRKUkRRHV3lhfi2BSnS8A==
        System.setProperty("MY_PASSW", "ZLAXx+9I2S6qCPi8y7xGhw=="); //export MY_PASSW=ZLAXx+9I2S6qCPi8y7xGhw==
    }
    @Before
    public void init() {
        setEnv();
    }
    @Test
    public void test_01() throws Exception{
        String val = Crypto.decryptAES("MY_USER");
        assertEquals("myUsrName", val);
    }

    @Test
    public void test_02() throws Exception{
        Crypto obj = new org.szb.ziarnakodu.plugins.Crypto();
        java.lang.reflect.Method method;
        method = obj.getClass().getMethod("decryptAES", String.class);
        String ret = (String)method.invoke(obj, "MY_PASSW");
        System.out.println(ret);
        assertEquals("861aB#82", ret);
    }
    
    @Test
    public void test_03() throws Exception{
        Class<?> obj = Class.forName("org.szb.ziarnakodu.plugins.Crypto");
        Method method = obj.getMethod("decryptAES", String.class);
        String ret = (String)method.invoke(obj, "MY_USER");
        System.out.println(ret);
        assertEquals("myUsrName", ret);
    }
    
    @Test
    public void test_04() throws Exception{
        String val = Crypto.encrypt("myUsrName"); //MY_USER
        System.out.println("myUsrName="+val);
    }
    
    @Test
    public void test_05() throws Exception{
        String val = Crypto.encrypt("861aB#82");
        System.out.println("861aB#82="+val);
    }
    
}
