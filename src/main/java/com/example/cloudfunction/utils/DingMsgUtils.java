package com.example.cloudfunction.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: liwenbo
 * @Date: 25/2/2021
 * @Description:
 */
public class DingMsgUtils {

    //    private static final String url = "https://oapi.dingtalk.com/robot/send?access_token=fca0c93008cf2f22d2c36335e5c42f1e5188f346a7ea66a8269d7a77704226ef";
    private static final String url = "https://oapi.dingtalk.com/robot/send?access_token=d6445d16a7615bf91cf2a0e22460e6c622ea46b3ff4f3a907604ea37ab354411";
    private static final String keyword = "小钉";

    public static void sendMsg(String myContent) {
        List<String> mobiles = new ArrayList<>();
        mobiles.add("18737843824");
        sendMsg(myContent, mobiles);
    }

    public static void sendMsg(String myContent, List<String> phoneList) {
        sendMsg(myContent, phoneList, url);
    }

    public static void sendMsg(String myContent, List<String> phoneList, String myUrl) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("msgtype", "text");
        Map<String, Object> content = new HashMap<>();
        content.put("content", myContent + "\t" + keyword);
        jsonObject.set("text", content);
        Map<String, Object> at = new HashMap<>();
        at.put("atMobiles", phoneList);
        at.put("isAtAll", false);
        jsonObject.set("at", at);
        String result = HttpUtil.post(url, jsonObject.toString());
    }
}
