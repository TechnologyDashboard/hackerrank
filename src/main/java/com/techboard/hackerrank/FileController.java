package com.techboard.hackerrank;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@RestController
public class FileController {

    public static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/uploader")
    public ResponseEntity upload(@RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile file) {
        try{

            String filePath = UPLOAD_DIR + fileName;
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            return new ResponseEntity("", HttpStatus.CREATED);

        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/downloader")
    public ResponseEntity download(@RequestParam("fileName") String fileName) {
        try {

            File file = new File(UPLOAD_DIR + fileName);
            if(!file.exists() || file.isDirectory() ){
                return new ResponseEntity("", HttpStatus.NOT_FOUND);
            }
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
