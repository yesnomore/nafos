package nafos.security.cache;

import nafos.core.helper.SpringApplicationContextHolder;
import nafos.core.util.ProtoUtil;
import nafos.security.config.SecurityConfig;
import nafos.security.redis.RedisKey;
import nafos.security.redis.RedisUtil;
import nafos.security.redis.SessionForRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @author huangxinyu
 * @version 创建时间：2018年1月29日 下午1:38:35
 * 登录的二级缓存session-redis 做分布式
 */
public class RedisSessionDao {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);

    private static int sessionTimeout;

    private static SessionForRedis sessionForRedis;


    static {
        sessionTimeout = SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getSessionTimeOut();
        sessionForRedis = SpringApplicationContextHolder.getSpringBeanForClass(SessionForRedis.class);
    }


    public static void update(String sessionId) {
        //通知其他服务器，修改session时间
        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis();
            if (jedis != null) {
                jedis.expire(RedisKey.SESSIONKEY + sessionId, sessionTimeout);//修改redis的过期时间
                jedis.publish("LoginSessionUpdate", sessionId);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            RedisUtil.returnResource(jedis);
        }
    }

    /**
     * 删除session
     */
    public static void delete(String sessionId) {
        sessionForRedis.del(RedisKey.SESSIONKEY + sessionId);
        //通知其他服务器，删除session
        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis();
            if (jedis != null) {
                jedis.publish("LoginSessionDelete", sessionId);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            RedisUtil.returnResource(jedis);
        }
    }

    /**
     * 判断是否存在
     *
     * @param sessionId
     * @return
     */
    public static boolean existsSession(String sessionId) {
        return sessionForRedis.exists(RedisKey.SESSIONKEY + sessionId);
    }

    /**
     * 获取存活的sessions
     */
    public static Set<String> getActiveSessions() {
        return sessionForRedis.keys(RedisKey.SESSIONKEY + "*");
    }


    /**
     * 获取session
     */
    public static <T> T doReadSession(String sessionId, Class<T> clazz) {
        if (sessionId == null) {
            return null;
        }
        T t = sessionForRedis.get(RedisKey.SESSIONKEY + sessionId, clazz);
        if (t == null) {
            return null;
        }
        return t;
    }


    /**
     * 保存session并存储过期时间
     *
     * @param sessionId
     * @param obj
     */
    public static void saveSession(String sessionId, Object obj) {
        if (obj == null) {
            logger.error("要存入的session-value为空");
            return;
        }
        //设置过期时间
        int expireTime = sessionTimeout;
        String uiui = ProtoUtil.serializeToString(obj);
        sessionForRedis.setex(RedisKey.SESSIONKEY + sessionId, uiui, expireTime);
    }


}
