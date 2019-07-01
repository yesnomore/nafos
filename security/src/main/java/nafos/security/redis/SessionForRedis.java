package nafos.security.redis;

import java.util.Set;

public interface SessionForRedis {

    boolean del(String key);

    boolean setex(String key, String value, int timeout);

    <T> T get(String key,Class<T> clazz );

    Set<String> keys(String partten);

    boolean exists(String key);

    long inc(String key);

    long expire(String key,int timeout);

    long llen(String key);

    boolean lpush(String key,String value);
}
