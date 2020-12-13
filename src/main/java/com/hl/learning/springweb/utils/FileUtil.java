package com.hl.learning.springweb.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//将指定内容写入指定文件，同时支持内容缓存，减少文件读写操作
public class FileUtil {
    public final static String filePath = "D:/TestFile/";
    private volatile Map<String, StringBuffer> bufferMap = new ConcurrentHashMap<>();


    private final static int BUFFER_MAX_LENGTH = 50;

    /**
     * 保存指定字符串到打指定文件中，通过缓存机制优化性能，只有超过缓存区大小才写入文件
     *
     * @param fileName
     * @param source
     * @return
     */
    public synchronized boolean saveString2TxtFile(String fileName, String source) {
        boolean result = false;
        StringBuffer currentContent;
        if (bufferMap.containsKey(fileName)) {
            if (bufferMap.get(fileName).toString().endsWith(System.lineSeparator())) {
                currentContent = bufferMap.get(fileName).append(source);
            } else {
                currentContent = bufferMap.get(fileName).append(System.lineSeparator() + source);
            }

        } else {
            currentContent = new StringBuffer(System.lineSeparator() + source);
            bufferMap.put(fileName, currentContent);
        }
        if (currentContent.length() > BUFFER_MAX_LENGTH) {//只有当前缓存区内容大于缓存区最大存储空间才将内容写入log文件中
            result = writeStr2File(fileName, currentContent.toString());
            if (result) {//如果保存失败，则不清除缓存区内存，用于再次清除
                bufferMap.put(fileName, new StringBuffer());
            }
        }
        return result;
    }

    /**
     * 将字符串拼接写入txt文件最后 -通过RandomAccessFile
     *
     * @param fileName
     * @param source
     * @return
     */
    @Deprecated
    private synchronized boolean writeStr2File1(String fileName, String source) {
        String absolutePath = filePath + fileName;

        File file = new File(absolutePath);
        File parentFile = new File(filePath);
        if (!file.exists()) {
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(absolutePath, "rw")) {
            long fileLength = randomAccessFile.length();//文件长度
            randomAccessFile.seek(fileLength);
            randomAccessFile.writeBytes(source);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 将字符串拼接写入txt文件最后 -通过FileOutputStream
     * 优选方法
     * @param fileName 文件名称
     * @param source   待写入源文件字符串
     * @return
     */
    private boolean writeStr2File(String fileName, String source) {
        String absolutePath = filePath + fileName;

        File file = new File(absolutePath);
        File parentFile = new File(filePath);
        if (!file.exists()) {
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        FileLock lock = null;
        try (FileChannel channel = new FileOutputStream(absolutePath, true).getChannel()) {
            while (true) {
                try {
                    lock = channel.lock();
                    if (lock != null && lock.isValid()) {
                        break;
                    }
                } catch (Exception e) {
                    Thread.sleep(10);
                }
            }
            channel.write(ByteBuffer.wrap(source.getBytes()));
            lock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


}
