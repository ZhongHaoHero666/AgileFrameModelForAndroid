package com.android.szh.common.permission;

import android.Manifest;

/**
 * 常用权限
 *
 * Created by sunzhonghao
 * @date 2017/9/12 14:05
 */
public final class Permission {

    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String VIBRATE = Manifest.permission.VIBRATE;
    public static final String READ_SMS = Manifest.permission.READ_SMS;
    public static final String MICROPHONE = Manifest.permission.RECORD_AUDIO;
    public static final String READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;

    public static final String[] GROUP_STORAGE = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final String[] GROUP_LOCATION = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static final String[] GROUP_CALENDAR = new String[]{
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };

    public static final String[] GROUP_CAMERA = new String[]{
            Manifest.permission.CAMERA
    };

    public static final String[] GROUP_CONTACTS = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS
    };

    public static final String[] GROUP_MICROPHONE = new String[]{
            Manifest.permission.RECORD_AUDIO
    };

    public static final String[] GROUP_PHONE = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.USE_SIP,
            Manifest.permission.PROCESS_OUTGOING_CALLS
    };

    public static final String[] GROUP_SENSORS = new String[]{
            Manifest.permission.BODY_SENSORS
    };

    public static final String[] GROUP_SMS = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.RECEIVE_MMS
    };

}
