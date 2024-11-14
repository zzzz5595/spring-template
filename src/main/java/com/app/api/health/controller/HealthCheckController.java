package com.app.api.health.controller;

import com.app.api.health.dto.HealthCheckResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthCheckController {

    private final Environment env;

    @GetMapping("/health")
    public ResponseEntity<HealthCheckResponseDto> healthCheck() {

        try{
            Thread.sleep(6000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        HealthCheckResponseDto healthCheckResponseDto = HealthCheckResponseDto.builder()
                .health("ok")
                .activeProfiles(Arrays.asList(env.getActiveProfiles()))
                .build();

        return ResponseEntity.ok(healthCheckResponseDto);
    }
}
