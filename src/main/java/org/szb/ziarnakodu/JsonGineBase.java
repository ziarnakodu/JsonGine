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

import static org.szb.ziarnakodu.ICalcExpr.EXPRTYPE.DBL;
import static org.szb.ziarnakodu.ICalcExpr.EXPRTYPE.INT;
import static org.szb.ziarnakodu.JsonGineLEXTOKEN.STRING;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

import org.szb.ziarnakodu.lib.BaseParser;
import org.szb.ziarnakodu.lib.Mapper;
import org.szb.ziarnakodu.lib.SyntaxInvalid;

public class JsonGineBase extends BaseParser {
    class DottedName{
        DottedName(){
            init();
        }
        List<String> parts;
        void init() {
            parts = new ArrayList<>();
        }
        void add() {
            parts.add(lex.getLastTokenStr());
        }
        private String getName(int d) { //0==whole, 1==without last
            StringBuffer ret = new StringBuffer();
            for(int i=0; i<parts.size()-d; ++i) {
                ret.append(parts.get(i));
                ret.append('.');
            }
            ret.setLength(ret.length()-1); //remove trailing .
            return ret.toString();
        }
        public String getFullName() { //a.b.c.Class.method
            return  getName(0);
        }
        public String getClssName() { //a.b.c.Class
            return  getName(1);
        }
        int size() {
            return parts.size();
        }
        String mkMethode() {
            // org.szb.ziarnakodu.plugins.Crypto.get -> get
            return parts.get(parts.size()-1);
        }
    }
    Stack<Object> target;  //to evaluate num_expr
    ICalcExpr calcExpr;
    String ident=null; //property name or class member name to set
    String objType;
    String key;
    Map<String, Object> mapValues;
    List<Object> arrResults;
    Object classes[];
    JsonGineLex lex;
    //List<String> dottedNameLi;
    DottedName dottedName;
    boolean isPropertiesMode;
    protected Properties props;
    List<Object> pluginParams;
    private void init(Object ... aClasses ) {
        props = new Properties();
        target = new Stack<>();
        target.push(aClasses[0]);
        classes = aClasses;
        //dottedNameLi = new ArrayList<>();
        dottedName = new DottedName();
        pluginParams = new ArrayList<>();
        isPropertiesMode = false;
    }
  
    public void init(String src, Object ... tClasses ) throws SyntaxInvalid {
        JsonGineLex lex = new JsonGineLex(src);
        super.init(lex);
        this.lex = lex;
        init(tClasses);
        //parse(lex);
    }
    public Properties getProperties() {
        return props;
    }
    protected void setProperties(Properties p) {
        lex.setProperties(p);
    }
    protected void saveArr() {
        Class<?> clss = target.peek().getClass();
        Method mthd=null;
        try {
            mthd = clss.getDeclaredMethod(Mapper.mkSet(ident), int[].class);
            if (mthd!=null) {
                int a[] = new int[arrResults.size()];
                //Array.set(a, 0, arrResults);
                for(int x=0; x<arrResults.size(); ++x) {
                    a[x] = ((Integer)arrResults.get(x)).intValue();
                }
                mthd.invoke(target.peek(), a);
                arrResults = null;
                objType=null; //reset
                return;
            }
        }catch (NoSuchMethodException ex1) {
            // its OK
        }catch (SecurityException | InvocationTargetException | IllegalAccessException e1 ) {
              e1.printStackTrace(); //TODO
        }
        Field f=null;
        try {
            f = clss.getDeclaredField(ident);
            int lg = arrResults.size();
            int ax[] = (int[])Array.newInstance(int.class, lg);
            for(int ii=0; ii<lg; ++ii) {
                ax[ii]=(int)arrResults.get(ii);
            }
            f.set(target.peek(), ax);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        arrResults = null;
        objType=null; //reset
    }
    public void setValue() {
        if (dottedName.size()>0) {
            //plugin
            pluginParams.add(calcExpr.getResult());
        }else {
            if (objType==null) {
                Class<?> clzz = int.class;
                switch(getGenType()) {
                case "int"              : clzz = int.class              ; break;
                case "double"           : clzz = double.class           ; break;
                case "java.lang.String" : clzz = java.lang.String.class ; break;
                case "java.lang.Integer": clzz = java.lang.Integer.class; break;
                case "java.lang.Double" : clzz = java.lang.Double.class ; break;
                }
                Mapper.setValue(target.peek(),ident, clzz, calcExpr.getResult());
            }else
            switch(objType) {
            case "java.util.Map<java.lang.String, java.lang.String>":
                mapValues.put(key, (String)calcExpr.getResult());
                break;
            case "int[]":
                arrResults.add(calcExpr.getResult());
                break;
            default:
                break;
            }
        }
    }
    public void setIdent() {
        String lastTokenStr = lex.getLastTokenStr();
        if (lex.lastTk==STRING) {
            lastTokenStr = JsonGineBase.stripQuotes(lastTokenStr);
        }
        if (objType!=null) {
            switch (objType) { //already set
            case "java.util.Map<java.lang.String, java.lang.String>":
                key = lastTokenStr;
                return;
            }
        }else {
            ident = lastTokenStr;
            try {
                String gt = getGenType();
                switch(gt) {
                case "java.util.Map<java.lang.String, java.lang.String>": 
                    mapValues = new HashMap<>();
                    objType = gt;
                    break;
                case "int[]":
                    objType = gt;
                    break;
                case "java.lang.String":
                case "int":
                case "long":
                case "float":
                case "double":
                    //do nothing
                    break;
                default:
                    //lookup classes
                    for(Object x : classes) {
                        if (x.getClass().getName().equals(gt)) {
                            target.push(x);
                            break;
                        }
                    }
                    break;
                }
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private String getGenType() {
        Field f=null;
        try {
            f = target.peek().getClass().getDeclaredField(ident);
        } catch (NoSuchFieldException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return f.getGenericType().getTypeName();
    }
    public void newNumExpr() {
        switch(getGenType()) {
        case "int":
        case "Integer":
        case "int[]":
        case "Integer[]":
            calcExpr = new CalcExprInteger();
            break;
        case "float":
        case "Float":
        case "double":
        case "Double":
        case "float[]":
        case "Float[]":
        case "double[]":
        case "Double[]":
            calcExpr = new CalcExprDouble();
            break;
        }
    }
    public void newStrExpr() {
        calcExpr = new CalcExprStr();
    }

    public void callPlugin() {
        String ret=null;
        try {
            Class<?> obj = Class.forName(dottedName.getClssName()); //"org.szb.ziarnakodu.plugins.Crypto");
            Method method = null;
            for(Method mi : obj.getMethods()) {
                if (mi.getName().equals(dottedName.mkMethode())) {
                    method = mi;
                    break;
                }
            }
            ret = (String)method.invoke(obj, pluginParams.toArray());
        } catch (ClassNotFoundException | SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(ret);
        if (isPropertiesMode) {
            props.setProperty(ident, ret);
        }else {
            Mapper.setValue(target.peek(),ident, String.class, ret);
            pluginParams = new ArrayList<>();
        }
        //dottedNameLi = new ArrayList<>();
        dottedName.init();
    }

    public void setMap() {
        if (mapValues!=null) {
            Class<?> clss = target.peek().getClass();
            Method mthd=null;
            try {
                mthd = clss.getDeclaredMethod(Mapper.mkSet(ident), java.util.Map.class);
                if (mthd!=null) {
                    mthd.invoke(target.peek(), mapValues);
                    arrResults = null;
                    objType=null; //reset
                    mapValues = null; //reset
                    return;
                }
            }catch (NoSuchMethodException ex1) {
                // its OK
            }catch (SecurityException | InvocationTargetException | IllegalAccessException e1 ) {
                  e1.printStackTrace(); //TODO
            }
            Field f=null;
            try {
                f = clss.getDeclaredField(ident);
                Map<String,String> mp = (Map<String,String>)f.get(target.peek());
                for(String s : mapValues.keySet()) {
                    mp.put(s, (String)mapValues.get(s));
                }
            } catch (NoSuchFieldException | SecurityException  | IllegalAccessException | IllegalArgumentException  e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mapValues = null; //reset
        }else {
            target.pop();
        }
    }
    protected static String stripQuotes(String s) {
        return s.substring(1, s.length()-1); //"abc" -> abc
    }
    protected void collect() {
        if (calcExpr.getType()==DBL) {
            calcExpr.collect(Double.parseDouble(lex.getLastTokenStr()));
        }else 
        if (calcExpr.getType()==INT) {
            calcExpr.collect(Integer.parseInt(lex.getLastTokenStr()));
        }else {
            calcExpr.collect(lex.getLastTokenStr());
        }
    }
    protected void collectStr() {
        calcExpr.collect(lex.getLastTokenStr());
    }
    protected void setProp() {
        props.setProperty(dottedName.getFullName(), getLastTokenStr());
    }
}
