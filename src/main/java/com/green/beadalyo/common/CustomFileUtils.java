package com.green.beadalyo.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Configuration
@Getter
public class CustomFileUtils {

    public final String uploadPath;

    public CustomFileUtils(@Value("${file.dir}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String makeFolder(String target) {
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
