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

package org.szb.ziarnakodu.example;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import org.szb.ziarnakodu.JsonGineParser_04;

public class MyClassTest{
    private String rd() throws Exception {
        URL resource = getClass().getClassLoader().getResource("org/szb/ziarnakodu/example/example.jsongine");
        return new String(Files.readAllBytes(Paths.get(resource.toURI())));
        //java 11: return Files.readString(Paths.get(resource.toURI()), Charset.forName("utf-8"));
    }
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
        MyClass myClass = new MyClass();
        JsonGineParser_04 parser = new JsonGineParser_04();
        System.setProperty("MY_USER" , "");
        System.setProperty("MY_PASSW", "");
        parser.parse(rd(), myClass, myClass.getConn());
        assertEquals("myvalue01", myClass.getName());
        assertEquals(       1234, myClass.getId());
        assertEquals(         12, myClass.getNrList()[0]);
        assertEquals(          5, myClass.getNrList()[1]);
        assertEquals(          4, myClass.getNrList()[2]);
        assertEquals("https://mydomain.com/home\\r\\n"   , myClass.getMyMap().get("home"));
        assertEquals("https://mydomain.com/contact\\r\\n", myClass.getMyMap().get("contact"));
        assertEquals(     628.0d, myClass.getCircumference(), 0.001);
        assertEquals(""         , myClass.getConn().getUser());
        assertEquals(""         , myClass.getConn().getPassword());
    }
}
