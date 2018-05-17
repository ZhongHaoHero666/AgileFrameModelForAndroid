package com.android.szh.common.http.model;

import java.io.File;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.MediaType;

/**
 * 请求参数
 *
 * @author liyunlong
 * @date 2017/8/31 17:32
 */
public class HttpParams {

    /**
     * 普通的键值对参数
     */
    public LinkedHashMap<String, String> urlParamsMap;
    /**
     * 文件的键值对参数
     */
    public LinkedHashMap<String, List<FileWrapper>> fileParamsMap;

    public HttpParams() {
        init();
    }

    public HttpParams(String key, String value) {
        init();
        put(key, value);
    }

    private void init() {
        urlParamsMap = new LinkedHashMap<>();
        fileParamsMap = new LinkedHashMap<>();
    }

    public void put(HttpParams params) {
        if (params != null) {
            if (params.urlParamsMap != null && !params.urlParamsMap.isEmpty())
                urlParamsMap.putAll(params.urlParamsMap);

            if (params.fileParamsMap != null && !params.fileParamsMap.isEmpty()) {
                fileParamsMap.putAll(params.fileParamsMap);
            }
        }
    }

    public void put(Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            urlParamsMap.putAll(params);
        }
    }

    public void put(String key, String value) {
        urlParamsMap.put(key, value);
    }

    public <T extends File> void put(String key, T file) {
        put(key, file, file.getName());
    }

    public <T extends File> void put(String key, T file, String fileName) {
        put(key, file, fileName, guessMimeType(fileName));
    }

    public <T extends InputStream> void put(String key, T file, String fileName) {
        put(key, file, fileName, guessMimeType(fileName));
    }

    public void put(String key, byte[] bytes, String fileName) {
        put(key, bytes, fileName, guessMimeType(fileName));
    }

    public void put(String key, FileWrapper fileWrapper) {
        if (key != null && fileWrapper != null) {
            put(key, fileWrapper.file, fileWrapper.fileName, fileWrapper.contentType);
        }
    }

    public <T> void put(String key, T countent, String fileName, MediaType contentType) {
        if (key != null) {
            List<FileWrapper> fileWrappers = fileParamsMap.get(key);
            if (fileWrappers == null) {
                fileWrappers = new ArrayList<>();
                fileParamsMap.put(key, fileWrappers);
            }
            fileWrappers.add(new FileWrapper(countent, fileName, contentType));
        }
    }

    public <T extends File> void putFileParams(String key, List<T> files) {
        if (key != null && files != null && !files.isEmpty()) {
            for (File file : files) {
                put(key, file);
            }
        }
    }

    public void putFileWrapperParams(String key, List<FileWrapper> fileWrappers) {
        if (key != null && fileWrappers != null && !fileWrappers.isEmpty()) {
            for (FileWrapper fileWrapper : fileWrappers) {
                put(key, fileWrapper);
            }
        }
    }

    public void removeUrl(String key) {
        urlParamsMap.remove(key);
    }

    public void removeFile(String key) {
        fileParamsMap.remove(key);
    }

    public void remove(String key) {
        removeUrl(key);
        removeFile(key);
    }

    public void clear() {
        urlParamsMap.clear();
        fileParamsMap.clear();
    }

    public boolean isUrlEmpty() {
        return urlParamsMap.isEmpty();
    }

    public boolean isFileEmpty() {
        return fileParamsMap.isEmpty();
    }

    private MediaType guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        path = path.replace("#", "");   //解决文件名中含有#号异常的问题
        String contentType = fileNameMap.getContentTypeFor(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return MediaType.parse(contentType);
    }

    /**
     * 文件类型的包装类
     */
    public static class FileWrapper<T> {
        public T file;//可以是
        public String fileName;
        public MediaType contentType;
        public long fileSize;

        public FileWrapper(T file, String fileName, MediaType contentType) {
            this.file = file;
            this.fileName = fileName;
            this.contentType = contentType;
            if (file instanceof File) {
                this.fileSize = ((File) file).length();
            } else if (file instanceof byte[]) {
                this.fileSize = ((byte[]) file).length;
            }
        }

        @Override
        public String toString() {
            return "FileWrapper{" + "countent=" + file + ", fileName='" + fileName + ", contentType=" + contentType + ", fileSize=" + fileSize + '}';
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParamsMap.entrySet()) {
            if (result.length() > 0) result.append("&");
            result.append(entry.getKey()).append("=").append(entry.getValue());
        }
        for (ConcurrentHashMap.Entry<String, List<FileWrapper>> entry : fileParamsMap.entrySet()) {
            if (result.length() > 0) result.append("&");
            result.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return result.toString();
    }

}
