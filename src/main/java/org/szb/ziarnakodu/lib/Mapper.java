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

package org.szb.ziarnakodu.lib;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.szb.ziarnakodu.ICalcExpr;

import static org.szb.ziarnakodu.ICalcExpr.EXPRTYPE.STR;

public class Mapper {
    
    public static void setValue(Object target, String ident, Class<?> clss, Object val) { //isNum? int.class : String.class
        Method mthd=null;
        try {
            mthd = target.getClass().getDeclaredMethod(mkSet(ident), clss);
            if (mthd!=null) {
                mthd.invoke(target, val);
                return;
            }
        }catch (NoSuchMethodException ex1) {
            // its OK
        }catch (SecurityException | InvocationTargetException | IllegalAccessException e1 ) {
              e1.printStackTrace(); //TODO
        }
        Field field=null;
        try {
            field = target.getClass().getDeclaredField(ident);
            field.setAccessible(true);
            //System.out.println("----->"+field.getGenericType().getTypeName());
            switch(field.getGenericType().getTypeName()) {
                case "boolean"          : field.set(target, (Boolean)val);break;
                case "short"            : field.set(target, (Short  )val);break;
                case "int"              : field.set(target, (Integer)val);break;
                case "long"             : field.set(target, (Long   )val);break;
                case "float"            : field.set(target, (Float  )val);break;
                case "double"           : field.set(target, (Double )val);break;
                case "java.lang.String" : field.set(target, (String )val);break;
                case "java.lang.Integer": field.set(target, (Integer)val);break;
                case "java.lang.Double" : field.set(target, (Double )val);break;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace(); //TODO
        } catch (SecurityException e) {
            e.printStackTrace(); //TODO
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); //TODO
        } catch (IllegalAccessException e) {
            e.printStackTrace(); //TODO
        }
    }
    public static void setValue(Object target, String ident, ICalcExpr calcExpr, boolean isNum) {
        setValue(target, ident, calcExpr.getType()==STR? String.class : int.class, calcExpr.getResult());
    }
      
    public static String mkSet(String ident) { //a -> setA, ab -> setAb
        StringBuffer bbf = new StringBuffer();
        bbf.append("set");
        char ch = ident.charAt(0);
        if (Character.isLowerCase(ch)) {
            bbf.append(Character.toUpperCase(ch));
        }
        bbf.append(ident.substring(1));
        return bbf.toString();
    }
}
