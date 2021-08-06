package com.exerciseApp.exercise.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;

@Service
public class FileUpload {

    private final CreateIDService createIDService;
    private String CLASSPATH = "C:/APM/Apache24/htdocs";
    private String PATHNAME = "/avatar";


    public FileUpload(CreateIDService createIDService) {
        this.createIDService = createIDService;
    }

    // 파일 저장
    public String Save(MultipartFile file, String path){
        String rs = createIDService.CreateRandomString(30);
        // 현재시간 가져오기
        long date = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        // 확장자 가져오기
        String ext = this.takeExtension(Objects.requireNonNull(file.getOriginalFilename()));
        sb.append(CLASSPATH).append(path).append("/").append(date).append(rs).append(".").append(ext);
        String safe_pathname = sb.toString(); // 실제 저장될 위치
        sb = new StringBuilder();
        sb.append(path).append("/").append(date).append(rs).append(".").append(ext);

        try {
            String save_pathname = sb.toString(); // 가져올 위치
            file.transferTo(new File(safe_pathname)); // 파일저장
            return save_pathname;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 회원 아바타 폴더별 생성 및 관리
    public String saveAvatarByMember(MultipartFile file, String SSID){
        String ext = takeExtension(Objects.requireNonNull(file.getOriginalFilename()));
        StringBuilder sb = new StringBuilder();
        try {
            String path = CLASSPATH+PATHNAME+"/"+SSID;
            File Folder = new File(path);
            if (!Folder.exists()) Folder.mkdirs();
            sb.append(CLASSPATH).append(PATHNAME).append("/").append(SSID).append("/avatar.").append(ext);
            String safe_pathname = sb.toString();
            file.transferTo(new File(safe_pathname));

            sb = new StringBuilder();
            return sb.append(PATHNAME).append("/").append(SSID).append("/avatar.").append(ext).toString();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* [.]기준으로 확장자 추출 */
    public String takeExtension(String fileName){
        return fileName.split("[.]")[1];
    }

    // 저장위치 변경
    public void setPATHNAME(String pathname){
        this.PATHNAME = pathname;
    }
}
