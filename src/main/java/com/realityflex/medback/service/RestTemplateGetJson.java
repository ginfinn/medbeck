package com.realityflex.medback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class RestTemplateGetJson {
    private final RestTemplate restTemplate;

//    public Poster getJson(String page) {
//        ResponseEntity<Poster> response = restTemplate.exchange("https://www.mos.ru/api/newsfeed/v4/frontend/json/ru/afisha?expand=spots,spheres,themes,auditories,foundation,districts&page=" + page,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<Poster>() {
//                });
//        return response.getBody();
//    }
}