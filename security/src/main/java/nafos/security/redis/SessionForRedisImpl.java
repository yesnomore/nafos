package nafos.security.redis;

import nafos.core.util.JsonUtil;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SessionForRedisImpl implements  SessionForRedis {
    @Override
    public boolean del(String key) {
        return RedisUtil.del(key);
    }

    @Override
    public boolean setex(String key, String value, int timeout) {
        return RedisUtil.setex(key,timeout,value);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        return JsonUtil.json2Object((String)RedisUtil.get(key),clazz);
    }

    @Override
    public Set<String> keys(String partten) {
        return RedisUtil.keys(partten);
    }

    @Override
    public boolean exists(String key) {
        return RedisUtil.exists(key);
    }

    @Override
    public long inc(String key) {
        return RedisUtil.incr(key);
    }

    @Override
    public long expire(String key, int timeout) {
        return RedisUtil.expire(key,timeout);
    }

    @Override
    public long llen(String key) {
        return RedisUtil.llen(key);
    }

    @Override
    public boolean lpush(String key,String value) {
        return RedisUtil.LpushOne(key,value);
    }
}
