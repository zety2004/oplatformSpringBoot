package com.hklk.oplatform.comm.cache;

import com.hklk.oplatform.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisObjCache<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate<String, T> redisObjTemplate;

    public static final String KEY_SET_PREFIX = "_set:";
    public static final String KEY_LIST_PREFIX = "_list:";

    public T get(String key) {
        logger.debug("get key [{}]", key);
        try {
            if (key == null) {
                return null;
            } else {
                return redisObjTemplate.opsForValue().get(key);
            }
        } catch (Throwable t) {
            logger.error("get key [{}] exception!", key, t);
            throw new CacheException(t);
        }

    }

    public T set(String key, T value) {
        logger.debug("set key [{}]", key);
        try {
            redisObjTemplate.opsForValue().set(key, value);
            return value;
        } catch (Throwable t) {
            logger.error("set key [{}] exception!", key, t);
            throw new CacheException(t);
        }
    }

    public T set(String key, T value, long timeout) {
        logger.debug("set key [{}]", key);
        try {
            redisObjTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
            return value;
        } catch (Throwable t) {
            logger.error("set key [{}] exception!", key, t);
            throw new CacheException(t);
        }
    }

    public void delete(String key) {
        logger.debug("delete key [{}]", key);
        try {
            redisObjTemplate.delete(key);
        } catch (Throwable t) {
            logger.error("delete key [{}] exception!", key, t);
            throw new CacheException(t);
        }
    }

    @SuppressWarnings("unchecked")
    public void setSet(String k, T value, long time) {
        String key = KEY_SET_PREFIX + k;
        logger.debug("setSet key [{}]", key);
        try {
            SetOperations<String, T> valueOps = redisObjTemplate.opsForSet();
            valueOps.add(key, value);
            if (time > 0)
                redisObjTemplate.expire(key, time, TimeUnit.SECONDS);
        } catch (Throwable t) {
            logger.error("setSet key [{}] exception!", key, t);
            throw new CacheException(t);
        }
    }

    public void setSet(String k, T value) {
        setSet(k, value, -1);
    }

    @SuppressWarnings("unchecked")
    public void setSet(String k, Set<T> v, long time) {
        String key = KEY_SET_PREFIX + k;
        logger.debug("setSet key [{}]", key);
        try {
            SetOperations<String, T> setOps = redisObjTemplate.opsForSet();
            setOps.add(key, (T[]) v.toArray());
            if (time > 0)
                redisObjTemplate.expire(key, time, TimeUnit.SECONDS);
        } catch (Throwable t) {
            logger.error("setSet key [{}] exception!", key, t);
            throw new CacheException(t);
        }
    }

    public void setSet(String k, Set<T> v) {
        setSet(k, v, -1);
    }

    public Set<T> getSet(String k) {
        String key = KEY_SET_PREFIX + k;
        logger.debug("getSet key [{}]", key);
        try {
            SetOperations<String, T> setOps = redisObjTemplate.opsForSet();
            return setOps.members(key);
        } catch (Throwable t) {
            logger.error("getSet key [{}] exception!", key, t);
            throw new CacheException(t);
        }
    }

    public void setList(String k, T v, long time) {
        String key = KEY_LIST_PREFIX + k;
        logger.debug("setList key [{}]", key);
        try {
            ListOperations<String, T> listOps = redisObjTemplate.opsForList();
            listOps.rightPush(key, v);
            if (time > 0)
                redisObjTemplate.expire(key, time, TimeUnit.SECONDS);
        } catch (Throwable t) {
            logger.error("setList key [{}] exception!", key, t);
            throw new CacheException(t);
        }
    }

    public void setList(String k, List<T> v, long time) {
        String key = KEY_LIST_PREFIX + k;
        logger.debug("setList key [{}]", key);
        try {
            ListOperations<String, T> listOps = redisObjTemplate.opsForList();
            listOps.rightPushAll(key, v);
            if (time > 0)
                redisObjTemplate.expire(key, time, TimeUnit.SECONDS);
        } catch (Throwable t) {
            logger.error("setList key [{}] exception!", key, t);
            throw new CacheException(t);
        }
    }

    public void setList(String k, List<T> v) {
        setList(k, v, -1);
    }

    public List<T> getList(String k, long start, long end) {
        String key = KEY_LIST_PREFIX + k;
        logger.debug("setList key [{}]", key);
        try {
            ListOperations<String, T> listOps = redisObjTemplate.opsForList();
            return listOps.range(key, start, end);
        } catch (Throwable t) {
            logger.error("getList key [{}] exception!", key, t);
            throw new CacheException(t);
        }
    }

    public long getListSize(String k) {
        String key = KEY_LIST_PREFIX + k;
        logger.debug("setList key [{}]", key);
        try {
            ListOperations<String, T> listOps = redisObjTemplate.opsForList();
            return listOps.size(key);
        } catch (Throwable t) {
            logger.error("getListSize key [{}] exception!", key, t);
            throw new CacheException(t);
        }
    }

    public long getListSize(ListOperations<String, String> listOps, String k) {
        String key = KEY_LIST_PREFIX + k;
        logger.debug("getListSize key [{}]", key);
        try {
            return listOps.size(key);
        } catch (Throwable t) {
            logger.error("getListSize key [{}] exception!", key, t);
            throw new CacheException(t);
        }
    }

    public void setMap(String key, String mapkey, T mapValue) {
        HashOperations<String, String, T> hashOperations = redisObjTemplate.opsForHash();
        hashOperations.putIfAbsent(key, mapkey, mapValue);
    }

    public void deleteMap(String key, String mapkey) {
        HashOperations<String, String, T> hashOperations = redisObjTemplate.opsForHash();
        hashOperations.delete(key, mapkey);
    }

    public T getMap(String key, String mapkey) {
        HashOperations<String, String, T> hashOperations = redisObjTemplate.opsForHash();
        return hashOperations.get(key, mapkey);
    }

    public List<T> getMapValues(String key) {
        HashOperations<String, String, T> hashOperations = redisObjTemplate.opsForHash();
        return hashOperations.values(key);
    }

    public void setRedisObjTemplate(RedisTemplate<String, T> redisObjTemplate) {
        this.redisObjTemplate = redisObjTemplate;
    }
}