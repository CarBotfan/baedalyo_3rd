package com.green.beadalyo.lhn.common;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Component
@Getter
public class CustomFileUtils {
    private final Logger log =LoggerFactory.getLogger(CustomFileUtils.class);
    public final String uploadPath;

    public CustomFileUtils(@Value("${file.dir}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String makeFolder(String Path) {
        File folder = new File(uploadPath, Path);
        folder.mkdirs();
        return folder.getAbsolutePath();
    }

    public String getExt(String filename) {
        int index = filename.lastIndexOf(".");

        if (index == -1) {
            return null;
        } else return filename.substring(index);
    }

    public String getExtOnlyAlpha(String filename) {
        int index = filename.lastIndexOf(".");

        if (index == -1) {
            return null;
        } else return filename.substring(index+1);
    }

    public String makeUUIDName() {
        return UUID.randomUUID().toString();
    }

    public String makeRandomFileName(String fileName) {
        return makeUUIDName() + getExt(fileName);
    }

    public String makeRandomFileName(MultipartFile file) {
        return file == null ? null : makeRandomFileName(file.getOriginalFilename());
    }

    public void transferTo(MultipartFile file, String target) throws Exception{
        File saveFile = new File(uploadPath, target);
        file.transferTo(saveFile);
    }

    public void deleteFolder(String absoluteFolderPath) { //D:\2024-01\download\greengram_ver3 상대 주소
        File folder = new File(absoluteFolderPath);
        if(folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            for(File f : files) {
                if(f.isDirectory()) {
                    deleteFolder(f.getAbsolutePath());
                } else {
                    f.delete();
                }
            }
            folder.delete();
        }
    }

    public void deleteFiles(List<String> fileNames, String absoluteFolderPath){
        for(String filename : fileNames)
        {
            File file = new File(absoluteFolderPath,filename);
            file.delete();

        }
    }

    /*------------------여기서 부터 필요해서 만든거임------------------*/
    public void cleanupFiles(List<String> files) {
        for (String filePath : files) {
            try {
                deleteFolder(filePath);
            } catch (Exception e) {
                e.printStackTrace();  // 파일 삭제 중 오류가 발생해도 계속 진행
            }
        }
    }
}