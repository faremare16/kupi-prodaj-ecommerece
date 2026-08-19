package com.faruk.backend.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageController {

    private final Path rootLocation= Paths.get("uploads/product_pictures");

    @GetMapping("/product_pictures/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename){
        try{
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() || resource.isReadable()){
                String contentType="application/octet-stream";
                if(filename.endsWith(".png")) contentType = "image/png";
                else if(filename.endsWith(".jpg") || filename.endsWith(".jpeg")) contentType = "image/jpeg";
                else if(filename.endsWith(".gif")) contentType = "image/gif";
                else if(filename.endsWith(".bmp")) contentType = "image/bmp";
                else if(filename.endsWith("webp")) contentType = "image/webp";

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            }else{
                return ResponseEntity.notFound().build();
            }
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
