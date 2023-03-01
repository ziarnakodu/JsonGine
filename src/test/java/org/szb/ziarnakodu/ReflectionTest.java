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
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

interface ITarget_Class {
}

class ABC implements ITarget_Class {
    public ABC() {
        
    }
    public String abc;
    public void setAbc(String v) {
        abc = v;
    }
}
class X implements ITarget_Class{
    private String a;
    public String b[];
    public String c;
    public void setA(String v) {
        a = v;
    }
    public String getA() {
        return a;
    }
    public List<String> li;
}
class Test_01 implements ITarget_Class{
    String name;
    int id;
    int nrList[];
    Map<String, String> myMap;
}
class Y implements ITarget_Class{
    public Y() {
        
    }
    ABC abc;
}
public class ReflectionTest {
//    @Test
//    public void test_02() throws Exception{
//        ABC x = new ABC();
//        Class<? extends ITarget_Class> target;
//        target = x.getClass();
//        Field abc = target.getDeclaredField("abc");
//        abc.setAccessible(true);
//        abc.set(target, "123");
//    }
    @Test
    public void test_03() throws Exception  {
        X x = new X();
        //setField(x, "a", "xyz");
        //Reflection
        Field a_of_x = X.class.getDeclaredField("a");
        a_of_x.setAccessible(true); // it overwrites private
        a_of_x.set(x, "xyz");
        assertEquals("xyz", x.getA());
        
        Field b_of_x = X.class.getDeclaredField("b");
        b_of_x.setAccessible(true); // it overwrites private
        b_of_x.set(x, new String[]{"xyz"});
        assertEquals("xyz", x.b[0]);
        
        Class<String> params[] = new Class[1];
        params[0]=String.class;
        Method setA = X.class.getDeclaredMethod("setA", params);
        System.out.println(setA.getName()); //java.lang.String[]

        Field li = X.class.getDeclaredField("li");
        System.out.println(li.getAnnotatedType()); //java.util.List<java.lang.String>
        
    }
    @Test
    public void test_04() throws Exception{
        byte bf[] = Files.readAllBytes(Paths.get("src/test/resources/org/szb/ziarnakodu/test_01.xjson"));
        JsonGineLex lex = new JsonGineLex(new String(bf));
        //Test_01 test_01 = lex.mapper(Test_01.class);
    }
    //
    @Test
    public void test_05() throws Exception{
        class C05b {String c;}
        class C05a {C05a(){b=new C05b();} C05b b; String a;}
        C05a c = new C05a(); 
        Class<?> ic = c.getClass();
        ic.getClass().cast(C05a.class);
        //C05a c2 = (C05a) ic.getClass().cast(C05a.class);
        //c2.b.c = "x";
    }
    //https://stackoverflow.com/questions/8918550/cast-via-reflection-and-use-of-class-cast
    public static <T> T getObject(Class<T> type){
        Object o = new ABC();
        if (type.isInstance(o)){
           return type.cast(o);
        } else {
           return null;
        }
    }
    @Test
    public void test_ABC() throws Exception{
        class Favorites {
            private Map<Class<?>, Object> map = new HashMap<>();

            public <T> T get(Class<T> clazz) {
                return clazz.cast(map.get(clazz));
            }

            public <T> void put(Class<T> clazz, T favorite) {
                map.put(clazz, favorite);
            }
        }
        Favorites favs = new Favorites();
        favs.put(String.class, "Hello");
        String favoriteString = favs.get(String.class);
        assertEquals("Hello", favoriteString);
        assertTrue(true);
    }
    @Test
    public void test_06() throws Exception{
        class C02b  implements ITarget_Class {String c;}
        class C02a  implements ITarget_Class {C02a(){b=new C02b();} C02b b; String a;}
        C02a c02a = new C02a();
        ITarget_Class ic = c02a;
        assertEquals(Class.forName("java.lang.String"), ic.getClass().getDeclaredField("a").getType());//"class org.szb.ziarnakodu.ReflectionTest$1C02b"
        Class<?> xxC02a = Class.forName("org.szb.ziarnakodu.ReflectionTest$1C02a");
        Field aField = xxC02a.getDeclaredField("a");
        aField.set(ic, "x");
        assertEquals("x", c02a.a);
        
        ITarget_Class ib = c02a.b;
        assertEquals(Class.forName("org.szb.ziarnakodu.ReflectionTest$1C02b"), ic.getClass().getDeclaredField("b").getType());//"class org.szb.ziarnakodu.ReflectionTest$1C02b"
        Class<? extends ITarget_Class> xxC02b = (Class<? extends ITarget_Class>) Class.forName("org.szb.ziarnakodu.ReflectionTest$1C02b");
        Field cField = xxC02b.getDeclaredField("c");
        cField.set(ib, "y");
        assertEquals("y", c02a.b.c);
    }
    @Test
    public void test_constructor() throws Exception{
        class C03b {public C03b(){} String c;}
        class C03a {public C03a(){} C03b b; String a;}
        
        Class<?> abcType = Class.forName("org.szb.ziarnakodu.ABC");
        assertEquals("org.szb.ziarnakodu.ABC", abcType.getConstructor().getName());
        assertEquals(1, abcType.getConstructor().getModifiers());//The sets of modifiers are represented as integers with distinct bit positions representing different modifiers
        ABC abc = (ABC)abcType.getConstructor().newInstance();
        
        // see https://stackoverflow.com/questions/18023870/java-lang-nosuchmethodexception-userauth-user-init
        //Class<?> c03aType = Class.forName("org.szb.ziarnakodu.ReflectionTest$1C03a");
        //c03aType.getConstructor().getName();
        //C03a c03a = (C03a)c03aType.getConstructor().newInstance();
    }
    private void passInterfaceAsParameter(ITarget_Class clss) {
        ITarget_Class cls [] = new ABC[2];
        cls[0]=clss;
    }
//    private void passClassAsParameter(Class clss) {
//        Class cls [] = new Class[2];
//        cls[0]=clss;
//    }
//    class Simple{
//        
//    }
    private void passObjectAsParameter(Object ao) {
        Object cls [] = new Object[2];
        cls[0]=ao;
    }
    class Simple{
      
    }
    @Test
    public void test_abc() throws Exception{
        Y yy = new Y(); 
        ITarget_Class ic = yy;
        assertEquals(Class.forName("org.szb.ziarnakodu.ABC"), ic.getClass().getDeclaredField("abc").getType());//"class org.szb.ziarnakodu.ReflectionTest$1C02b"
        Class<?> abcType = Class.forName("org.szb.ziarnakodu.ABC");
        assertEquals("org.szb.ziarnakodu.ABC", abcType.getConstructor().getName());
        
        ABC abc = (ABC)abcType.getConstructor().newInstance();
        //ic.getClass().getDeclaredField("abc").set(Class.forName("org.szb.ziarnakodu.Y"), abc);
        Simple simple = new Simple();
        //passClassAsParameter(simple); //The method passClassAsParameter(Class) in the type ReflectionTest is not applicable for the arguments (ReflectionTest.Simple)
        passInterfaceAsParameter(abc);
        passObjectAsParameter(simple);
    }
    @Test
    public void test_testPlugin() throws Exception{
        Class<?> obj = Class.forName("org.szb.ziarnakodu.plugins.TestPlugin");  //plugin1, plugin2
        Method plugin1 = obj.getMethod("plugin1", String.class, String.class);
        String ret = (String)plugin1.invoke(obj, "abc", "xy");
        System.out.println(ret);
        
//        Method plugin2 = obj.getMethod("plugin2", String.class, int.class);
//        Object var[] = {"abc", 7};
//        int reti = (int)plugin2.invoke(obj, var);
//        System.out.println(reti);
        
    }
}