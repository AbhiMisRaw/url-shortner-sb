package com.abhi.shortner.repository;

import com.abhi.shortner.models.UrlMapping;
import com.abhi.shortner.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    UrlMapping findByShortUrl(String shortUrl);

    List<UrlMapping> findByUser(User user);

}
