package com.android.szh.agileframedemo.entry;

import android.text.TextUtils;


import com.android.szh.common.exception.DateParseException;
import com.android.szh.common.http.HttpManager;
import com.android.szh.common.utils.DateHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 城市和IP信息
 *
 */
public class CityInfo {

    private static final String URL = "http://pv.sohu.com/cityjson?ie=utf-8&qq-pf-to=pcqq.c2c";
    private static final String UNKNOWN = "未知";

    private String cityId;
    private String cityName;
    private String ipAddress;
    private String dateTime;

    public String getCityId() {
        if (TextUtils.isEmpty(cityId)) {
            return UNKNOWN;
        }
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        if (TextUtils.isEmpty(cityName)) {
            return UNKNOWN;
        }
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getIpAddress() {
        if (TextUtils.isEmpty(ipAddress)) {
            return UNKNOWN;
        }
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDateTime() {
        if (TextUtils.isEmpty(dateTime)) {
            return UNKNOWN;
        }
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "CityInfo{" +
                "cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
