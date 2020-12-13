package com.hl.learning.springweb.utils;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class fileUtilTest {
    String[] printStr = {"kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk", "ttttttttttttttttttttttttttttttttttttttttttttttttttt",
            "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww", "mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm", "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn", "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj"};

    private static CountDownLatch latch=new CountDownLatch(10);
    @Test
    public void saveString2TxtFileTest() throws InterruptedException {
        MutiThread mutiThread=new MutiThread();
        for (int i = 0; i < 10; i++) {
            Thread thread=new Thread(mutiThread);
            thread.start();
        }
        latch.await();
    }

    private class MutiThread implements Runnable{
        @Override
        public void run() {
            FileUtil fileUtil = new FileUtil();
            for (int i = 0; i < 100; i++) {
                String fileName = "test" + (i / 10);
                Date localDate = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
                String currentDateStr = format.format(localDate);
                Random random = new Random();

                String logStr = i +"-"+ Thread.currentThread().getName()+"-"+"-" + fileName + "-" + currentDateStr + "-" + printStr[random.nextInt(5)];
                System.out.println(logStr);
                assert fileUtil.saveString2TxtFile(fileName, logStr);
            }
            latch.countDown();
        }
    }
}
