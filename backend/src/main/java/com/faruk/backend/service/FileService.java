package com.faruk.backend.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    public boolean deleteFile(String fileUrlOrName){
        if(fileUrlOrName==null || fileUrlOrName.isEmpty()){
            return false;
        }

        try{
            String cleanPath=fileUrlOrName;
            if(fileUrlOrName.contains("uploads")){
                cleanPath=fileUrlOrName.substring(fileUrlOrName.indexOf("uploads"));
            }

            Path filePath = Paths.get(cleanPath);

            if(Files.exists(filePath)) {
                Files.deleteIfExists(filePath);
                return true;
            }
        }catch(IOException e){
            System.err.println("Error while deleting file: "+e.getMessage());
        }
        return false;
    }
}
