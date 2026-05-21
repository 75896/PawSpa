package com.example.spapet.registro.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CaptchaService {

    @Value("${recaptcha.secret}")
    private String secretKey;

    @Value("${recaptcha.url}")
    private String recaptchaUrl;

    public boolean verificar(String captchaToken) {
        if (captchaToken == null || captchaToken.isBlank())
            return false;

        RestTemplate restTemplate = new RestTemplate();
        String url = recaptchaUrl + "?secret=" + secretKey + "&response=" + captchaToken;

        Map response = restTemplate.postForObject(url, null, Map.class);
        return response != null && Boolean.TRUE.equals(response.get("success"));
    }
}