package com.aye10032.danmuutilforandroid.util;

import java.io.*;

public class FileUtil {
    private String FilePath;

    public FileUtil(String path){
        FilePath = path;
    }

    public void save(String body){
        try {
            File file = new File(FilePath);
            OutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream,"UTF-8");

            writer.append(body);
            writer.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
