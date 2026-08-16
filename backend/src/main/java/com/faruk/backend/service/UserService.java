package com.faruk.backend.service;

import com.faruk.backend.entity.User;
import com.faruk.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private final String UPLOAD_DIR="uploads/profiles/";

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail)));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new RuntimeException("User not found");
        }else{
            userRepository.deleteById(id);
        }
    }

    public User updateUser( String currentUsername, String username ,String email, String phoneNumber, MultipartFile file){
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(email!=null && !email.equals(user.getEmail())){
            userRepository.findByEmail(email).ifPresent(exsistingUser -> {
                if(!exsistingUser.getId().equals(user.getId())){
                    throw new RuntimeException("Email already in use: "+email);
                }
            });
        }

        if(username!=null && !username.equals(user.getUsername())){
            userRepository.findByUsername(username).ifPresent(exsistingUser -> {
                if(!exsistingUser.getId().equals(user.getId())){
                    throw new RuntimeException("Username already in use: "+username);
                }
            });
        }
        user.setEmail(email);
        user.setUsername(username);
        user.setPhoneNumber(phoneNumber);

        if(file != null && !file.isEmpty()){
            try{
                File directory = new File(UPLOAD_DIR);
                if(!directory.exists()){
                    directory.mkdirs();
                }

                String fileName=System.currentTimeMillis()+"_"+file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR+fileName);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                user.setProfileImageUrl("/uploads/profiles/"+fileName);
            }catch(IOException e){
                throw new RuntimeException("Error while trying to save a file "+e);
            }
        }

        return userRepository.save(user);
    }
}
