package com.faruk.backend.service;

import com.faruk.backend.dto.UserResponseDto;
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
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final FileService fileService;

    private final String USER_UPLOAD_DIR="uploads/profiles/";

    public UserService(UserRepository userRepository,
                       FileService fileService) {
        this.userRepository = userRepository;
        this.fileService=fileService;
    }

    public UserResponseDto getUserProfile(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return mapUserToDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail)));
    }

    public List<UserResponseDto> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapUserToDto).collect(Collectors.toList());
    }

    public UserResponseDto getUsersResponseById(Long id){
        User user=userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id " + id));
        return mapUserToDto(user);
    }

    @Transactional
    public void deleteUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getRoles().clear();

        if(user.getProfileImageUrl()!=null && !user.getProfileImageUrl().isEmpty()){
            fileService.deleteFile(user.getProfileImageUrl());
        }

        userRepository.delete(user);
    }

    public UserResponseDto updateUser( String currentUsername, String username ,String email, String phoneNumber, MultipartFile file){
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

        // logika za snimanje profilnih u folder profiles i brisanje stare
        if(file != null && !file.isEmpty()){
            try{
                if(user.getProfileImageUrl()!=null && !user.getProfileImageUrl().isEmpty()){
                    fileService.deleteFile(user.getProfileImageUrl());
                }

                File directory = new File(USER_UPLOAD_DIR);
                if(!directory.exists()){
                    directory.mkdirs();
                }

                String fileName=System.currentTimeMillis()+"_"+file.getOriginalFilename();
                Path filePath = Paths.get(USER_UPLOAD_DIR+fileName);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                user.setProfileImageUrl("/uploads/profiles/"+fileName);
            }catch(IOException e){
                throw new RuntimeException("Error while trying to save a file "+e);
            }
        }

        User savedUser=userRepository.save(user);
        return mapUserToDto(user);
    }

    public UserResponseDto mapUserToDto(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dateCreated(user.getDateCreated())
                .profileImageUrl(user.getProfileImageUrl())
                .products(user.getProducts())
                .roles(user.getRoles())
                .build();
    }
}
