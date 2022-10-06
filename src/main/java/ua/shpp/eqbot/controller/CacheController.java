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
import ua.shpp.eqbot.cache.ServiceCache;
import ua.shpp.eqbot.dto.ServiceDTO;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
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
        ConcurrentMap<String, Object> cache = (ConcurrentMap<String, Object>) Objects.requireNonNull(cacheManager.getCache(cacheName)).getNativeCache();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(cache);
        } catch (JsonProcessingException e) {
            LOGGER.warn(e.getLocalizedMessage());
        }
        LOGGER.info("return cache");
        return json;
    }


    @GetMapping("/services")
    public Map<Long, ServiceDTO> getAllservices() {
        return ServiceCache.getAll();
    }
}
