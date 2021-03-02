package com.example.cloudfunction.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.cloudfunction.common.Enums;
import com.example.cloudfunction.utils.DingMsgUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: liwenbo
 * @Date: 25/2/2021
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {


    @RequestMapping("/hello")
    public String test() {
        log.info("hello world");
        return "success";
    }


    @RequestMapping("/sendMessageDing/{content}")
    public String sendMessageDing(@PathVariable("content") String content) {
        DingMsgUtils.sendMsg(content);
        return "success";
    }

    @RequestMapping("/sendWeatherDing")
    public String sendWeatherDing() {
        String url = "http://t.weather.itboy.net/api/weather/city/";
        String result = HttpUtil.get(url + Enums.chang_sha_code);
        JSONObject jsonObject = new JSONObject(result);
        JSONObject jsonObjectCity = new JSONObject(jsonObject.get("cityInfo"));
        JSONObject jsonObjectData = new JSONObject(jsonObject.get("data"));
        JSONArray jsonArray = (JSONArray) jsonObjectData.get("forecast");
        List<JSONObject> jsonObjects = jsonArray.stream().map(JSONObject::new).collect(Collectors.toList());
        String template = "{} {}的天气，更新时间为今天{}。\n湿度: {}\npm2.5: {}\npm10: {}\n空气质量: {}\n温度: {}\n感冒指数: {}\n具体天气信息如下:\n";
        String templateDetails = " 日期: {}\t{}\n 天气: {}\n 风向: {}{}\n 气温: {}\t{}\n 空气质量指数: {}\n 日出时间: {}\n日落时间: {}\n温馨提示: {}\n";
        String weatherInfo1 = StrUtil.format(template, jsonObjectCity.get("city"), jsonObject.get("time"), jsonObjectCity.get("updateTime"),
                jsonObjectData.get("shidu"), jsonObjectData.get("pm25"), jsonObjectData.get("pm10"), jsonObjectData.get("quality"),
                jsonObjectData.get("wendu"), jsonObjectData.get("ganmao"));
        StringBuilder weatherResult = new StringBuilder(weatherInfo1);
        for (JSONObject object : jsonObjects) {
            String weatherInfoTemp = StrUtil.format(templateDetails, object.get("ymd"), object.get("week"), object.get("type"),
                    object.get("fx"), object.get("fl"), object.get("low"), object.get("high"), object.get("aqi"),
                    object.get("sunrise"), object.get("sunset"), object.get("notice"));
            weatherResult.append(weatherInfoTemp);
        }
        System.out.println(weatherResult.toString());
        List<String> phoneList = new ArrayList<>();
        phoneList.add("15173714200");
        phoneList.add("18737843824");
        DingMsgUtils.sendMsg(weatherResult.toString(), phoneList);
        return "success";
    }
}
