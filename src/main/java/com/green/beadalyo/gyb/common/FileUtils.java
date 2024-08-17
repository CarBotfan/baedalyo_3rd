package com.green.beadalyo.gyb.common;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class FileUtils
{

    private final String SITE = "https://zumuniyo.shop" ;
    private final String absolutePath  ;

    public FileUtils(@Value("${file.dir}") String dir)
    {
        absolutePath = dir ;
    }

    public String getRandomName()
    {
        String time = String.valueOf(System.currentTimeMillis()) ;
        UUID uuid = UUID.randomUUID();
        return time + "_" + uuid ;

    }

    public boolean checksumExt(List<Ext> exts, MultipartFile file)
    {
        if (file == null || file.isEmpty()) return false ;
        String ext = getExt(file) ;

        if (ext == null || ext.isEmpty()) return false ;

        for (Ext checksum : exts)
        {
            if(checksum.toString().equals(ext.toLowerCase())) {
                return true;
            }
        }
        return false ;
    }

    public String getExt(MultipartFile file)
    {
        if (file == null || file.isEmpty()) return null ;

        String fileName = file.getOriginalFilename();
        try {
            return Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
        } catch (NullPointerException e) {
            return null ;
        }
    }

    public String getExt(String fileName)
    {
        if (fileName == null || fileName.lastIndexOf(".") < 0) return null ;

        try {
            return Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
        } catch (NullPointerException e) {
            return null ;
        }
    }

    @SuppressWarnings("all")
    public String fileInput(String path, MultipartFile file) throws IOException
    {

        String filename = getRandomName();
        filename += getExt(file);
        System.out.println(absolutePath + "/" + filename);

        Path directoryPath = Paths.get(absolutePath, path);
        File directory = directoryPath.toFile();

        // 디렉토리 생성
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 파일 저장 경로 설정
        Path filePath = directoryPath.resolve(filename);
        File newFile = filePath.toFile();
        try {
            // 파일을 지정된 경로로 복사
            file.transferTo(newFile);
            return SITE +"/pic/" + path + "/" + filename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("all")
    public Boolean fileDelete(String fileName) throws Exception
    {
        try {
            // 경로 조합
            String sub = fileName.replace(SITE+"/pic/" , "") ;
            Path filePath = Paths.get(absolutePath , sub);
            File fileToDelete = filePath.toFile();

            // 파일 존재 여부 확인
            if (!fileToDelete.exists()) {
                System.out.println("파일이 존재하지 않습니다: " + filePath);
                return false;
            }

            // 파일 삭제
            boolean isDeleted = fileToDelete.delete();
            if (!isDeleted) {
                System.out.println("파일 삭제에 실패했습니다: " + filePath);
            }
            return isDeleted;
        } catch (SecurityException e) {
            System.out.println("파일 삭제 권한이 없습니다: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("파일 삭제 중 예기치 않은 오류가 발생했습니다: " + e.getMessage());
            return false;
        }
    }

    public Integer fileDeleteList(String path, List<File> files) throws Exception
    {
        int result = 0;

        for (File f : files) {
            try {
                // 경로 조합
                Path filePath = Paths.get(absolutePath, path, f.getName());
                File fileToDelete = filePath.toFile();

                // 파일 존재 여부 확인
                if (!fileToDelete.exists()) {
                    System.out.println("파일이 존재하지 않습니다: " + filePath);
                    continue;
                }

                // 파일 삭제
                if (fileToDelete.delete()) {
                    result++;
                } else {
                    System.out.println("파일 삭제에 실패했습니다: " + filePath);
                }
            } catch (SecurityException e) {
                System.out.println("파일 삭제 권한이 없습니다: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("파일 삭제 중 예기치 않은 오류가 발생했습니다: " + e.getMessage());
            }
        }

        return result;
    }

    //    @SuppressWarnings("all")
    public Boolean deleteDirectory(String forder) throws Exception
    {
        Path paths = Paths.get(absolutePath, forder);
        File directory = paths.toFile() ;

        if (!directory.exists()) {
            System.out.println("디렉토리가 존재하지 않습니다: " + directory.getAbsolutePath());
            return false;
        }

        if (!directory.isDirectory()) {
            System.out.println("디렉토리가 아닙니다: " + directory.getAbsolutePath());
            return false;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file.getName()); // 재귀 호출하여 하위 디렉토리 삭제
                } else {
                    if (!file.delete()) {
                        System.out.println("파일 삭제에 실패했습니다: " + file.getAbsolutePath());
                    }
                }
            }
        }

        return directory.delete(); // 폴더 자체 삭제
    }
}
