package com.abhi.shortner.service;

import com.abhi.shortner.dtos.ClickEventDTO;
import com.abhi.shortner.dtos.UrlMappingDTO;
import com.abhi.shortner.models.ClickEvent;
import com.abhi.shortner.models.UrlMapping;
import com.abhi.shortner.models.User;
import com.abhi.shortner.repository.ClickEventRepository;
import com.abhi.shortner.repository.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UrlMappingService {

    private UrlMappingRepository urlMappingRepository;
    private ClickEventRepository clickEventRepository;

    public UrlMappingDTO createShortUrl(String originalUrl, User user) {
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping= new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());
        urlMapping.setClickCount(0);
        var savedMapping = urlMappingRepository.save(urlMapping);
        return UrlMappingDTO.convertToUrlMappingDTO(savedMapping);
    }

    private String generateShortUrl() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            shortUrl.append(chars.charAt(random.nextInt(chars.length()) ));
        }
        return shortUrl.toString();
    }

    public List<UrlMappingDTO> getUrlsByUser(User user) {
        return urlMappingRepository.findByUser(user)
                .stream()
                .map(
                        (urlMapping -> {
                            return UrlMappingDTO.convertToUrlMappingDTO(urlMapping);
                        })
                ).toList();
    }

    public List<ClickEventDTO> getClickEventsByDate(
            String shortUrl,
            LocalDateTime start,
            LocalDateTime end
    ){
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping!= null){
            return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end)
                    .stream()
                    .collect(Collectors.groupingBy(
                            click -> click.getClickDate().toLocalDate(), Collectors.counting()
                    )).entrySet().stream()
                    .map(
                            entry -> {
                                return new ClickEventDTO(entry.getKey(), entry.getValue());
                            }
                    ).collect(Collectors.toList());
        }else{
            return null;
        }
    }

    public Map<LocalDate, Long> getTotalClickByUserAndDate(User user, LocalDate start, LocalDate end) {
        List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
        return clickEvents.stream()
                .collect(Collectors.groupingBy(
                        click -> click.getClickDate().toLocalDate(), Collectors.counting()
                ));
    }



    public UrlMapping getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping != null) {
            urlMapping.setClickCount(urlMapping.getClickCount() + 1);
            urlMappingRepository.save(urlMapping);

            // Record Click Event
            ClickEvent clickEvent = new ClickEvent();
            clickEvent.setClickDate(LocalDateTime.now());
            clickEvent.setUrlMapping(urlMapping);
            clickEventRepository.save(clickEvent);
        }

        return urlMapping;
    }
}
