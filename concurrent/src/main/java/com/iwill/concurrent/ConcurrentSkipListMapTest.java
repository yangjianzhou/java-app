package com.iwill.concurrent;

import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentSkipListMapTest {

    public static void main(String[] args) {

        ConcurrentSkipListMap<String,String> map = new ConcurrentSkipListMap<String,String>();
        map.put("1","123");
        map.get("1");

    }
}
