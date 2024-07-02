package com.green.beadalyo.jhw.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Configuration
public class CustomFileUtils {

    public final String uploadPath;

    public CustomFileUtils(@Value("${file.dir}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String makeFolders(String target) {
        File folder = new File(uploadPath, target);
        folder.mkdirs();
        return folder.getAbsolutePath();
    }

    public String makeRandomFileName() {
        return UUID.randomUUID().toString();
    }

    public String makeRandomFileName(String fileName) {
        return makeRandomFileName() + getExt(fileName);
    }

    public String getExt(String fileNm) {
        int idx = fileNm.lastIndexOf(".");
        return fileNm.substring(idx);
    }
    public String makeRandomFileName(MultipartFile mf) {
        return mf == null ? null : makeRandomFileName(mf.getOriginalFilename());
    }

    public void transferTo(MultipartFile mf, String target) throws Exception{
        File saveFile = new File(uploadPath, target);
        mf.transferTo(saveFile);
    }

    public void deleteFolder(String absoluteFolderPath) {
        File folder = new File(absoluteFolderPath);
        if(folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            for(File file : files) {
                if(file.isDirectory()) {
                    deleteFolder(file.getAbsolutePath());
                }
                else {
                    file.delete();
                }
            }
            folder.delete();
        }
    }

}
