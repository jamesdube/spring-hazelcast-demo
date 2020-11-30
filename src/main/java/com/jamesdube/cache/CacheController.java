package com.jamesdube.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("cache")
public class CacheController {

    private static final Logger logger = LogManager.getLogger(CacheController.class);

    CacheManager cacheManager;

    public CacheController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @GetMapping
    public Object index(){
        return cacheManager.getCacheNames();
    }

    @GetMapping("{cacheName}")
    public Object find(@PathVariable String cacheName){
        return  cacheManager.getCache(cacheName);
    }

    @DeleteMapping
    public Object deleteAll(){
        Collection<String> cacheNames = cacheManager.getCacheNames();
        cacheNames.forEach(this::deleteCache);
        return "all cache cleared";
    }

    @DeleteMapping("{cacheName}")
    public Object delete(@PathVariable String cacheName){
        Cache cache = cacheManager.getCache(cacheName);
        if(cache != null){
            cache.clear();
            return "cache " +cacheName+ " evicted";
        }
        return "cache " +cacheName+ " does not exist";
    }

    private void deleteCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        assert cache != null;
        cache.clear();
    }

}
