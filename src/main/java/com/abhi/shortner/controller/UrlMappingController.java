package com.abhi.shortner.controller;


import com.abhi.shortner.dtos.ClickEventDTO;
import com.abhi.shortner.models.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.List;

import com.abhi.shortner.dtos.UrlMappingDTO;

import com.abhi.shortner.service.UrlMappingService;
import com.abhi.shortner.service.UserService;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController {
    private UrlMappingService urlMappingService;
    private UserService userService;

    @PostMapping("/short")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDTO> createShortUrl(
            @RequestBody Map<String, String> request,
            Principal principal){ //
        String originalUrl = request.get("originalUrl");
        var user = userService.findByUsername(principal.getName());
        UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(urlMappingDTO);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDTO>> createShortUrl(Principal principal){
            var user = userService.findByUsername(principal.getName());
            List<UrlMappingDTO> urls = urlMappingService.getUrlsByUser(user);
            return ResponseEntity.ok(urls);
        }



    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDTO>> getAnalyticsOfUrl(
            @PathVariable String shortUrl,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            Principal principal
    ){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        List<ClickEventDTO> results =  urlMappingService.getClickEventsByDate(shortUrl, start, end);

        return ResponseEntity.ok(results);
    }

    @GetMapping("/total-clicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate, Long>> getUrlsAnalytics(
            Principal principal,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    ){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        User user = userService.findByUsername(principal.getName());
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        Map<LocalDate, Long> res = urlMappingService.getTotalClickByUserAndDate(user, start, end);
        return ResponseEntity.ok(res);
    }
}
