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

import java.util.Map;

public class MyClass{
    private String name;
    private int    id;
    private int    nrList[];
    private Map<String, String> myMap;
    private double circumference;
    private ConnectionData conn;
    public MyClass() {
        conn = new ConnectionData();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int[] getNrList() {
        return nrList;
    }
    public void setNrList(int[] list) {
        this.nrList = list;
    }
    public Map<String, String> getMyMap() {
        return myMap;
    }
    public void setMyMap(Map<String, String> myMap) {
        this.myMap = myMap;
    }
    public double getCircumference() {
        return circumference;
    }
    public void setCircumference(double circumf) {
        this.circumference = circumf;
    }
    public ConnectionData getConn() {
        return conn;
    }
    public void setConn(ConnectionData conn) {
        this.conn = conn;
    }
}
