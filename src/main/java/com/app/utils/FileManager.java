package com.app.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FileManager {

    @Value("${img.path}")
    private String imgPath;

    private String createFilename(MultipartFile multipartFile) {
        final String originalFilename = multipartFile.getOriginalFilename();
        final String[] arr = originalFilename.split("\\.");
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) + "." + arr[arr.length - 1];
    }

    public String addFile(MultipartFile multipartFile) throws IllegalAccessException, IOException {
        if (multipartFile == null || multipartFile.getBytes().length == 0) {
            throw new IllegalAccessException("MULTIPARTFILE IS NOT CORRECT");
        }

        final String filename = createFilename(multipartFile);
        final String fullPath = imgPath + filename;
        FileCopyUtils.copy(multipartFile.getBytes(), new File(fullPath));
        return fullPath;
    }

    public void updateFile(MultipartFile multipartFile, String fullPath) throws IOException {
        if (multipartFile == null || multipartFile.getBytes().length == 0) {
            return;
        }
        removeFile(fullPath);
        FileCopyUtils.copy(multipartFile.getBytes(), new File(fullPath));
    }

    public String removeFile(String fullPath) {
        new File(fullPath).delete();
        return fullPath;
    }
}
