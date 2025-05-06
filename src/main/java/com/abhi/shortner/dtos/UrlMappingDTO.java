package com.abhi.shortner.dtos;

import com.abhi.shortner.models.UrlMapping;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UrlMappingDTO {
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private int clickCount;
    private LocalDateTime createdAt;
    private String username;


    public static UrlMappingDTO convertToUrlMappingDTO(UrlMapping urlMapping){
        UrlMappingDTO dto = new UrlMappingDTO(
                urlMapping.getId(),
                urlMapping.getOriginalUrl(),
                urlMapping.getShortUrl(),
                urlMapping.getClickCount(),
                urlMapping.getCreatedDate(),
                urlMapping.getUser().getUsername()
        );
        return dto;
    }
}
