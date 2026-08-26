package com.faruk.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Service
public class ImgBbService {

    @Value("${imgbb.api.key:${IMGBB_API_KEY:}}")
    private String apiKey;

    private final String IMGBB_URL = "https://api.imgbb.com/1/upload?key=";

    public String uploadImage(MultipartFile file){
        try {
            System.out.println("KORISTIM IMGBB KLJUČ: [" + apiKey + "]");

            RestTemplate restTemplate = new RestTemplate();

            String url = IMGBB_URL + apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            org.springframework.core.io.ByteArrayResource contentsAsResource =
                    new org.springframework.core.io.ByteArrayResource(file.getBytes()) {
                        @Override
                        public String getFilename() {
                            return file.getOriginalFilename() != null ? file.getOriginalFilename() : "image.png";
                        }
                    };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", contentsAsResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                if (data != null && data.containsKey("url")) {
                    return (String) data.get("url");
                }
            }

            throw new RuntimeException("Greška pri parsiranju odgovora sa ImgBB-a.");
        } catch (Exception e) {
            throw new RuntimeException("Greška prilikom slanja slike na ImgBB: " + e.getMessage(), e);
        }
    }
}
