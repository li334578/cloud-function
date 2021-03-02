package com.example.cloudfunction.job;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.cloudfunction.common.Enums;
import com.example.cloudfunction.utils.DingMsgUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: liwenbo
 * @Date: 25/2/2021
 * @Description: 天气推送定时任务
 */
public class WeatherJob {

    public void sendWeatherDingShangHai() {
        sendWeatherDing(Enums.shang_hai_code, Collections.singletonList("18737843824"));
    }


    public void sendWeatherDingChangSha() {
        sendWeatherDing(Enums.chang_sha_code, Collections.singletonList("15173714200"));
    }

    public void sendWeatherDing(int cityCode, List<String> phoneNumberList) {
        System.out.println("定时任务执行了");
        String url = "http://t.weather.itboy.net/api/weather/city/";
        String result = HttpUtil.get(url + cityCode);
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
        DingMsgUtils.sendMsg(weatherResult.toString(), phoneNumberList);
    }
}
