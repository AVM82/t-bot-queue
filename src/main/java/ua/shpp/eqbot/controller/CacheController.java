package ua.shpp.eqbot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/cache")
public class CacheController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheController.class);

    private final CacheManager cacheManager;

    public CacheController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }


    @GetMapping("/name")
    public Collection<String> getCacheNames() {
        return cacheManager.getCacheNames();
    }

    @GetMapping("/{cacheName}")
    public String getEntriesForCache(@PathVariable(name = "cacheName") String cacheName) {
        ConcurrentMap<String, Object> cache = (ConcurrentMap<String, Object>) cacheManager.getCache(cacheName).getNativeCache();
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(cache);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("return cache");
        return json;
    }
}