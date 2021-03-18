package com.example.cloudfunction.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: liwenbo
 * @Date: 18/3/2021
 * @Description:
 */
public class ReadExcelByManyThread {
    public static void main(String[] args) {
        // 多线程读取excel
        ExecutorService myThreadPool = Executors.newFixedThreadPool(10);
        List<MyUser> myUserList = new ArrayList<>();
        // 十个线程读取
        for (int i = 0; i < 10; i++) {
            MyExcelReader myExcelReader = new MyExcelReader(i * 5000 + 1, (i + 1) * 5000);
            Future<List<MyUser>> submit = myThreadPool.submit(myExcelReader);
            try {
                myUserList.addAll(submit.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        myThreadPool.shutdown();
        System.out.println(myUserList.size());
        MyUser myUser = myUserList.get(myUserList.size() - 1);
        System.out.println(myUser);
    }

    static class MyExcelReader implements Callable<List<MyUser>> {

        private final int startRow;
        private final int endRow;


        public MyExcelReader(int startRow, int endRow) {
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        public List<MyUser> call() {
            System.out.println(Thread.currentThread().getName() + " reading excel ...");
            File file = FileUtil.file("E:\\sources\\wb_test2\\src\\test\\java\\com\\li\\wb_test2\\data\\testExcel.xlsx");
            ExcelReader excelReader = new ExcelReader(file, 0);
            excelReader.addHeaderAlias("id", "id");
            excelReader.addHeaderAlias("name", "name");
            excelReader.addHeaderAlias("age", "age");
            List<MyUser> result = excelReader.read(0, startRow, endRow, MyUser.class);
            excelReader.close();
            System.out.println(Thread.currentThread().getName() + " reading excel end ...");
            return result;
        }
    }

    @Data
    static class MyUser implements Serializable {
        private Long id;
        private String name;
        private Long age;
    }
}
