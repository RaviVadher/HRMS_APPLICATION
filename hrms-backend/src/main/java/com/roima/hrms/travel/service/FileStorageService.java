package com.roima.hrms.travel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path rootLocation;

    public FileStorageService(
        @Value("${file.uploads-dir}") String uploadDir ){
        this.rootLocation = Paths.get(uploadDir);
    }

    //store the file in uploads folder
    public String store(MultipartFile file,Long travelId,Long userId, String docName) {

        try{
            if(file.isEmpty()){
                throw new RuntimeException("Empty file");
            }

            String extension = getExtension(file.getOriginalFilename());
            String fileName = docName+"_"+ System.currentTimeMillis()+ extension;

            Path dir = rootLocation
                    .resolve("travels")
                    .resolve("travel_"+travelId)
                    .resolve("user_"+userId);
            Files.createDirectories(dir);

            Path destination = dir.resolve(fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return destination.toString();
        } catch(IOException e){
             throw new RuntimeException("failed to store fil",e);
        }

    }


    public Resource load(String filePath){
        try{
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }
            throw new RuntimeException("file not found");

        }
        catch(MalformedURLException e)
        {
            throw new RuntimeException("file loading errors");
        }
    }

    //extract extension from file
    private String getExtension(String fileName){
         return fileName.substring(fileName.lastIndexOf("."));
    }
}
