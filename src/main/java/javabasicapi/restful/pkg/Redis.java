package javabasicapi.restful.pkg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class Redis {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * Save data to Redis
     * 
     * @param key Redis key
     * @param value Data to save
     * @param expiration Expiration time in seconds (0 for no expiration)
     * @return true if successful, false otherwise
     */
    public boolean save(String key, Object value, long expiration) {
        try {
            if (expiration > 0) {
                redisTemplate.opsForValue().set(key, value, expiration, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error saving data to Redis: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get data from Redis
     * 
     * @param key Redis key
     * @return The data associated with the key, or null if not found
     */
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            System.err.println("Error getting data from Redis: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Delete data from Redis
     * 
     * @param key Redis key
     * @return true if successful, false otherwise
     */
    public boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            System.err.println("Error deleting data from Redis: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if a key exists in Redis
     * 
     * @param key Redis key
     * @return true if the key exists, false otherwise
     */
    public boolean exists(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            System.err.println("Error checking key existence in Redis: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Set expiration time for a key
     * 
     * @param key Redis key
     * @param expiration Expiration time in seconds
     * @return true if successful, false otherwise
     */
    public boolean expire(String key, long expiration) {
        try {
            return redisTemplate.expire(key, expiration, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.println("Error setting expiration for key in Redis: " + e.getMessage());
            return false;
        }
    }
}
