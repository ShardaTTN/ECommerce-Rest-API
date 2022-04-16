package com.tothenew.sharda.Service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Component
public class FileUploaderForUsersUtil {

    public final static String UPLOAD_DIR = "/home/sharda/Documents/testproject2/src/main/resources/static/images/users/";

//    public boolean uploadFile(MultipartFile file) {
//        boolean result = false;
//
//        try {
//
//            InputStream inputStream = file.getInputStream();
//            byte[] data = new byte[inputStream.available()];
//            inputStream.read();
//
//        } catch (Exception e) {
//
//        }
//
//        return result;
//    }
}