package com.example.bfhl.controller;

import com.example.bfhl.service.BfhlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ApiController {

    @Value("${official.email}")
    private String officialEmail;

    private final BfhlService bfhlService;

    public ApiController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(
                Map.of(
                        "is_success", true,
                        "official_email", officialEmail
                )
        );
    }

    @PostMapping("/bfhl")
    public ResponseEntity<Map<String, Object>> bfhl(@RequestBody Map<String, Object> body) {
        try {
            // Validation: exactly one key
            if (body == null || body.size() != 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        Map.of(
                                "is_success", false
                        )
                );
            }

            Object data = bfhlService.process(body);

            return ResponseEntity.ok(
                    Map.of(
                            "is_success", true,
                            "official_email", officialEmail,
                            "data", data
                    )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of(
                            "is_success", false
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "is_success", false
                    )
            );
        }
    }
}
