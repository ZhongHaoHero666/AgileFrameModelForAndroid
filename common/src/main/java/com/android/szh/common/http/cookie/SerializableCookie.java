package com.android.szh.common.http.cookie;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import okhttp3.Cookie;

/**
 * Cookie的序列化和反序列化实现
 */
public class SerializableCookie implements Serializable {

    private static final String TAG = SerializableCookie.class.getSimpleName();
    private static final long serialVersionUID = -8594045714036645534L;
    private static final long COOKIE_VALID_EXPIRES_AT = -1L;
    private transient Cookie cookie;

    /**
     * 序列化Cookie
     *
     * @param cookie 需要序列化的Cookie对象
     */
    public String encode(Cookie cookie) {
        this.cookie = cookie;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
        } catch (IOException e) {
            Log.e(TAG, "IOException in encodeCookie", e);
            return null;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                Log.i(TAG, "Stream not closed in encodeCookie", e);
            }
        }
        return byteArrayToHexString(baos.toByteArray());
    }

    /**
     * 二进制数组转换为十六进制字符串
     *
     * @param bytes 需要被转换的二进制数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }

    /**
     * 发序列化Cookie
     *
     * @param encodedCookie 需要反序列化的字符串对象
     */
    public Cookie decode(String encodedCookie) {
        Cookie cookie = null;
        byte[] bytes = hexStringToByteArray(encodedCookie);
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            cookie = ((SerializableCookie) ois.readObject()).cookie;
        } catch (IOException e) {
            Log.i(TAG, "IOException in decodeCookie", e);
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "ClassNotFoundException in decodeCookie", e);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException e) {
                Log.i(TAG, "Stream not closed in decodeCookie", e);
            }
        }
        return cookie;
    }

    /**
     * 十六进制字符串转换为二进制数组
     *
     * @param hexString 需要被转换的十六进制字符串
     * @return 二进制数组
     */
    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(cookie.name());
        out.writeObject(cookie.value());
        out.writeLong(cookie.persistent() ? cookie.expiresAt() : COOKIE_VALID_EXPIRES_AT);
        out.writeObject(cookie.domain());
        out.writeObject(cookie.path());
        out.writeBoolean(cookie.secure());
        out.writeBoolean(cookie.httpOnly());
        out.writeBoolean(cookie.hostOnly());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Cookie.Builder builder = new Cookie.Builder();
        builder.name((String) in.readObject());
        builder.value((String) in.readObject());
        long expiresAt = in.readLong();
        if (expiresAt != COOKIE_VALID_EXPIRES_AT) {
            builder.expiresAt(expiresAt);
        }
        final String domain = (String) in.readObject();
        builder.domain(domain);
        builder.path((String) in.readObject());
        if (in.readBoolean()) {
            builder.secure();
        }
        if (in.readBoolean()) {
            builder.httpOnly();
        }
        if (in.readBoolean()) {
            builder.hostOnlyDomain(domain);
        }
        cookie = builder.build();
    }

}
