package org.inlighting.database;

import java.util.HashMap;
import java.util.Map;

public class DataSource {
    private static Map<String, Map<String, String>> data = new HashMap<>();

    static {
    	//设置smith用户的密码、角色、权限信息
        Map<String, String> data1 = new HashMap<>();
        data1.put("password", "smith123");
        data1.put("role", "user");// 设置角色
        data1.put("permission", "view");// 设置权限

      //设置danny用户的密码、角色、权限信息
        Map<String, String> data2 = new HashMap<>();
        data2.put("password", "danny123");
        data2.put("role", "admin");
        data2.put("permission", "view,edit"); //设置权限

        data.put("smith", data1);//设置smith用户
        data.put("danny", data2);//设置danny用户
    }

    public static Map<String, Map<String, String>> getData() {
        return data;
    }
}
