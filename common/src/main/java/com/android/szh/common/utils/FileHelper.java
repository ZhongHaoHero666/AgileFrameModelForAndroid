package com.android.szh.common.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类(可用于读写文件及对文件进行操作)
 */
public class FileHelper {

    private static final int BUFFER_SIZE = 1024 * 4;
    public final static String FILE_EXTENSION_SEPARATOR = ".";
    /**
     * 1KB的字节数
     */
    public static final long ONE_KB = 1024;
    /**
     * 1KB的字节数
     */
    public static final BigInteger ONE_KB_BI = BigInteger.valueOf(ONE_KB);
    /**
     * 1MB的字节数
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;
    /**
     * 1MB的字节数
     */
    public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
    /**
     * 1GB的字节数
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;
    /**
     * 1GB的字节数
     */
    public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
    /**
     * 1TB的字节数
     */
    public static final long ONE_TB = ONE_KB * ONE_GB;
    /**
     * 1TB的字节数
     */
    public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
    /**
     * 1PB的字节数
     */
    public static final long ONE_PB = ONE_KB * ONE_TB;
    /**
     * 1PB的字节数
     */
    public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
    /**
     * 1EB的字节数
     */
    public static final long ONE_EB = ONE_KB * ONE_PB;
    /**
     * 1EB的字节数
     */
    public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);
    /**
     * 1ZB的字节数
     */
    public static final BigInteger ONE_ZB = BigInteger.valueOf(ONE_KB).multiply(BigInteger.valueOf(ONE_EB));
    /**
     * 1YB的字节数
     */
    public static final BigInteger ONE_YB = ONE_KB_BI.multiply(ONE_ZB);

    private FileHelper() {
        throw new AssertionError();
    }

    /**
     * 获取系统临时目录的路径
     */
    public static String getTempDirectoryPath() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 获取用户的主目录的路径
     */
    public static String getUserDirectoryPath() {
        return System.getProperty("user.home");
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(String filePath) {
        return TextUtils.isEmpty(filePath) ? null : new File(filePath);
    }

    /**
     * 读文件
     *
     * @param filePath    目标文件路径
     * @param charsetName 编码方式
     * @return 如果文件不存在则返回null，反之返回文件内容
     */
    public static String readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder builder = new StringBuilder("");
        if (!file.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                if (!builder.toString().equals("")) {
                    builder.append("\r\n");
                }
                builder.append(line);
            }
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * 写文件
     *
     * @param filePath 目标文件路径
     * @param content  需要写入的内容
     * @param append   如果append为true，则追加内容到文件末尾，反之则清空文件内容然后再写入
     * @return 如果content为空则返回false，反之返回true
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (isEmpty(content)) {
            return false;
        }
        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * 写文件，清空文件内容后将contentList写入文件
     *
     * @param filePath    目标文件路径
     * @param contentList 需要写入的内容
     * @return
     */
    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);
    }

    /**
     * 写文件
     *
     * @param filePath    目标文件路径
     * @param contentList 需要写入的内容
     * @param append      如果append为true，则追加内容到文件末尾，反之则清空文件内容然后再写入
     * @return return 如果contentList为空则返回false，反之返回true
     */
    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if (contentList == null || contentList.size() == 0) {
            return false;
        }
        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * 写文件，清空文件内容后将stream写入文件
     *
     * @param filePath 目标文件路径
     * @param stream   需要写入的流
     * @return
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * 写文件
     *
     * @param filePath 目标文件路径
     * @param stream   需要写入的流
     * @param append   如果append为true，则追加内容到文件末尾，反之则清空文件内容然后再写入
     * @return
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * 写文件，清空文件内容后将stream写入文件
     *
     * @param file   目标文件
     * @param stream 需要写入的流
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * 写文件
     *
     * @param file   目标文件
     * @param stream 需要写入的流
     * @param append 如果append为true，则追加内容到文件末尾，反之则清空文件内容然后再写入
     * @return
     * @throws RuntimeException
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream os = null;
        try {
            makeDirs(file.getAbsolutePath());
            os = new FileOutputStream(file, append);
            byte data[] = new byte[BUFFER_SIZE];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                os.write(data, 0, length);
            }
            os.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * 移动文件
     *
     * @param sourceFilePath 源文件路径
     * @param destFilePath   目标文件路径
     */
    public static void moveFile(String sourceFilePath, String destFilePath) {
        if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(destFilePath)) {
            throw new RuntimeException("Both sourceFilePath and destFilePath cannot be null.");
        }
        moveFile(new File(sourceFilePath), new File(destFilePath));
    }

    /**
     * 重命名
     *
     * @param srcFile  源文件
     * @param destName 目标文件
     * @return
     */
    public static boolean renameTo(File srcFile, String destName) {
        if (TextUtils.isEmpty(destName)) {
            return false;
        }
        return renameTo(srcFile, new File(srcFile.getParent() + File.separator + destName));
    }

    /**
     * 重命名
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @return
     */
    public static boolean renameTo(File srcFile, File destFile) {
        if (!srcFile.exists()) {
            return false;
        }
        if (srcFile.getName().equals(destFile.getName())) {
            return false;
        }
        if (destFile.exists()) {
            return false;
        }
        return srcFile.renameTo(destFile);
    }

    /**
     * 移动文件
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     */
    public static void moveFile(File srcFile, File destFile) {
        boolean rename = renameTo(srcFile, destFile);
        if (!rename) {
            copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            deleteFile(srcFile.getAbsolutePath());
        }
    }

    /**
     * 复制文件
     *
     * @param sourceFilePath 源文件路径
     * @param destFilePath   目标文件路径
     * @return
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }

    /**
     * 将文件读入一个“List<String>”，每一行都是一个元素
     *
     * @param filePath
     * @param charsetName
     * @return 如果路径不存在则返回null，反之返回List形式的文件内容
     * @throws RuntimeException
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file == null || !file.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(
                    file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * 得到路径所在的文件名（不包含后缀）
     * <p/>
     * <ul>
     * <li>getFileNameWithoutExtension(null) = null
     * <li>getFileNameWithoutExtension("") = ""
     * <li>getFileNameWithoutExtension(" ") = " "
     * <li>getFileNameWithoutExtension("abc") = "abc"
     * <li>getFileNameWithoutExtension("a.mp3") = "a"
     * <li>getFileNameWithoutExtension("a.b.rmvb") = "a.b"
     * <li>getFileNameWithoutExtension("c:\\") = ""
     * <li>getFileNameWithoutExtension("c:\\a") = "a"
     * <li>getFileNameWithoutExtension("c:\\a.b") = "a"
     * <li>getFileNameWithoutExtension("c:a.txt\\a") = "a"
     * <li>getFileNameWithoutExtension("/home/admin") = "admin"
     * <li>getFileNameWithoutExtension("/home/admin/a.txt/b.mp3") = "b"
     * </ul>
     *
     * @param filePath
     * @return 返回路径所在的文件名（不包含后缀）
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (isEmpty(filePath)) {
            return filePath;
        }
        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0,
                    extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1,
                extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * 得到路径所在的文件名（包含后缀）
     * <p/>
     * <ul>
     * <li>getFileName(null) = null
     * <li>getFileName("") = ""
     * <li>getFileName(" ") = " "
     * <li>getFileName("a.mp3") = "a.mp3"
     * <li>getFileName("a.b.rmvb") = "a.b.rmvb"
     * <li>getFileName("abc") = "abc"
     * <li>getFileName("c:\\") = ""
     * <li>getFileName("c:\\a") = "a"
     * <li>getFileName("c:\\a.b") = "a.b"
     * <li>getFileName("c:a.txt\\a") = "a"
     * <li>getFileName("/home/admin") = "admin"
     * <li>getFileName("/home/admin/a.txt/b.mp3") = "b.mp3"
     * </ul>
     *
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * 得到路径所在的父文件夹名
     * <p/>
     * <ul>
     * <li>getFolderName(null) = null
     * <li>getFolderName("") = ""
     * <li>getFolderName(" ") = ""
     * <li>getFolderName("a.mp3") = ""
     * <li>getFolderName("a.b.rmvb") = ""
     * <li>getFolderName("abc") = ""
     * <li>getFolderName("c:\\") = "c:"
     * <li>getFolderName("c:\\a") = "c:"
     * <li>getFolderName("c:\\a.b") = "c:"
     * <li>getFolderName("c:a.txt\\a") = "c:a.txt"
     * <li>getFolderName("c:a\\b\\c\\d.txt") = "c:a\\b\\c"
     * <li>getFolderName("/home/admin") = "/home"
     * <li>getFolderName("/home/admin/a.txt/b.mp3") = "/home/admin/a.txt"
     * </ul>
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {
        if (isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * 得到路径所在的文件后缀名
     * <p/>
     * <ul>
     * <li>getFileExtension(null) = ""
     * <li>getFileExtension("") = ""
     * <li>getFileExtension(" ") = " "
     * <li>getFileExtension("a.mp3") = "mp3"
     * <li>getFileExtension("a.b.rmvb") = "rmvb"
     * <li>getFileExtension("abc") = ""
     * <li>getFileExtension("c:\\") = ""
     * <li>getFileExtension("c:\\a") = ""
     * <li>getFileExtension("c:\\a.b") = "b"
     * <li>getFileExtension("c:a.txt\\a") = ""
     * <li>getFileExtension("/home/admin") = ""
     * <li>getFileExtension("/home/admin/a.txt/b") = ""
     * <li>getFileExtension("/home/admin/a.txt/b.mp3") = "mp3"
     * </ul>
     *
     * @param filePath 目标路径
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (isBlank(filePath)) {
            return filePath;
        }
        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * 创建该目录包含的完整的目录路径
     *
     * @param filePath 目标路径
     * @return 如果需要的目录已创建或目标目录已存在则返回true，反之返回false
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (isEmpty(folderName)) {
            return false;
        }
        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param dirPath 文件路径
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(File file) {
        if (file == null) return false;
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param filePath
     * @return
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    /**
     * 判断路径是否文件系统上存在的一个文件
     *
     * @param filePath 文件路径
     * @return
     */
    public static boolean isFileExists(String filePath) {
        if (isBlank(filePath)) {
            return false;
        }
        return isFileExists(new File(filePath));
    }

    /**
     * 判断路径是否文件系统上存在的一个文件
     *
     * @param file 文件
     * @return
     */
    public static boolean isFileExists(File file) {
        return (file.exists() && file.isFile());
    }

    /**
     * 判断路径是否文件系统上存在的一个目录
     *
     * @param directoryPath 目录路径
     * @return
     */
    public static boolean isFolderExists(String directoryPath) {
        if (isBlank(directoryPath)) {
            return false;
        }
        return isFolderExists(new File(directoryPath));
    }

    /**
     * 判断路径是否文件系统上存在的一个目录
     *
     * @param dir 目录路径
     * @return
     */
    public static boolean isFolderExists(File dir) {
        return (dir.exists() && dir.isDirectory());
    }

    /**
     * 删除文件或目录
     *
     * @param path 目标路径
     * @return 返回值情况如下：
     * <ul>
     * <li>如果路径为null或空则返回true
     * <li>如果路径不存在则返回true
     * <li>如果路径存在则递归删除后返回true
     * <ul>
     */
    public static boolean deleteFile(String path) {
        if (isBlank(path)) {
            return true;
        }
        return deleteFile(new File(path));
    }

    /**
     * 删除文件或目录
     *
     * @param file 文件
     * @return 返回值情况如下：
     * <ul>
     * <li>如果路径为null或空则返回true
     * <li>如果路径不存在则返回true
     * <li>如果路径存在则递归删除后返回true
     * <ul>
     */
    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * 得到文件大小
     *
     * @param file
     * @return 返回值情况如下：
     * <ul>
     * <li>如果路径为null或空或不存在，则返回0
     * <li>如果路径存在且是文件，则返回文件大小
     * <li>如果路径存在且是目录，则返回所有文件大小的和
     * <ul>
     */
    public static long getFileSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        long size = 0;
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (File f : fileList) {
                    if (f.isDirectory()) {
                        size += getFileSize(f);
                    } else {
                        size += f.length();
                    }
                }
            }
        } else if (file.isFile()) {
            size += file.length();
        }
        return size;
    }

    /**
     * 获取可读的文件大小
     */
    public static String byteCountToDisplaySize(final long size) {
        return byteCountToDisplaySize(BigInteger.valueOf(size));
    }

    /**
     * 获取可读的文件大小
     */
    public static String byteCountToDisplaySize(final BigInteger size) {
        String displaySize;
        if (size.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_EB_BI)) + " EB";
        } else if (size.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_PB_BI)) + " PB";
        } else if (size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_TB_BI)) + " TB";
        } else if (size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_GB_BI)) + " GB";
        } else if (size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_MB_BI)) + " MB";
        } else if (size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_KB_BI)) + " KB";
        } else {
            displaySize = String.valueOf(size) + " bytes";
        }
        return displaySize;
    }

    /**
     * 更新文件的最近修改日期
     */
    public static void setLastModifiedTime(final File file) throws IOException {
        if (!file.exists()) {
            final OutputStream out = new FileOutputStream(file, false);
            out.close();
        }
        final boolean success = file.setLastModified(System.currentTimeMillis());
        if (!success) {
            throw new IOException("Unable to set the last modification time for " + file);
        }
    }


    /**
     * 获取文件MimeType
     *
     * @param path 文件路径
     * @return 文件MimeType
     */
    public static String getFileMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(path);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        return mimeType;
    }

    public static boolean isEmpty(String path) {
        return (path == null || path.length() == 0);
    }

    public static boolean isBlank(String filePath) {
        return (filePath == null || filePath.trim().length() == 0);
    }

    /**
     * 获取缓存目录
     *
     * @param diskCacheName
     */
    public static File getCacheDirectory(Context context, String diskCacheName) {
        File cacheDirectory = context.getExternalCacheDir();
        if (cacheDirectory == null) {
            cacheDirectory = context.getCacheDir();
        }
        if (diskCacheName != null) {
            return new File(cacheDirectory, diskCacheName);
        }
        return cacheDirectory;
    }

}
