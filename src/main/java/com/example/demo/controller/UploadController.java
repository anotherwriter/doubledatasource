package com.example.demo.controller;


import com.example.demo.model.db.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/upload")
@Slf4j
public class UploadController {


    @Value("${local_tmp_pic_dir:url_pic}")
    private String localTmpPicDir;


    @RequestMapping(method = RequestMethod.POST, value = "")
    @ResponseStatus(HttpStatus.OK)
    private ApiResponse upload_pic(@RequestParam("originalUniqId") String originalUniqId,
                                   @RequestParam("uploadType") String type,
                                   @RequestParam("uploadfile") MultipartFile uploadFile) {
        String finalCdnPicUrl = null;
        try {
            if(!type.equals("site") && !type.equals("app") && !type.equals("movie")  && !type.equals("glass") && !type.equals("hotword")) {
                return  new ApiResponse<>(-1, "wrong upload type: type must be site/app/movie/hotword");
            }

            if (!uploadFile.isEmpty()) {
                //竟然可以将png转为jpg
                String fileName = originalUniqId.replace(" ", "") + "_" + Math.abs(UUID.randomUUID().hashCode());
                BufferedOutputStream localTmpPic = new BufferedOutputStream(
                        new FileOutputStream(new File(localTmpPicDir + File.separator + fileName + ".jpg")));
                FileCopyUtils.copy(uploadFile.getInputStream(), localTmpPic);
                localTmpPic.close();

                // attention: we put all types' pic into movie/url_pic
                finalCdnPicUrl = processImage(localTmpPicDir, fileName);
            }
            else {
                log.info("file is null when upload to {}", originalUniqId);
                return new ApiResponse<>(-1, "upload file is null");
            }
        }
        catch (Exception e) {
            log.error("failed to upload", e);
        }
        return finalCdnPicUrl == null ?
                new ApiResponse<>(-1, "upload file fail") : new ApiResponse<>(finalCdnPicUrl);
    }

    // return: final cdn url
    private String processImage(String originalDir, String fileName) {
        log.info("originalDir={}, fileName={}, file separator={}", originalDir, fileName, File.separator);
        String originalPicPath = originalDir + File.separator + fileName + ".jpg";

        return originalPicPath;
    }

}
