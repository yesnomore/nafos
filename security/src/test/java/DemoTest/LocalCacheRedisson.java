package DemoTest;

import nafos.core.helper.SpringApplicationContextHolder;
import nafos.security.config.SecurityConfig;
import nafos.security.redis.RedisKey;
import nafos.security.redis.RedissonManager;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName (LocalCacheRedisson)
 * @Desc DOTO
 * @Author hxy
 * @Date 2019/7/1 18:58
 **/
public class LocalCacheRedisson {
    private static int sessTimeOut = 1000;
    private static RLocalCachedMap<String, Object> exmap = null;
    private static final String cacheKey = "NAFOS:SECURITY:CACHE";

    static {
//        sessTimeOut = SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getSessionTimeOut();
        LocalCachedMapOptions options = LocalCachedMapOptions.defaults()
                // 用于淘汰清除本地缓存内的元素
                // 共有以下几种选择:
                // LFU - 统计元素的使用频率，淘汰用得最少（最不常用）的。
                // LRU - 按元素使用时间排序比较，淘汰最早（最久远）的。
                // SOFT - 元素用Java的WeakReference来保存，缓存元素通过GC过程清除。
                // WEAK - 元素用Java的SoftReference来保存, 缓存元素通过GC过程清除。
                // NONE - 永不淘汰清除缓存元素。
                .evictionPolicy(LocalCachedMapOptions.EvictionPolicy.LRU)
                // 如果缓存容量值为0表示不限制本地缓存容量大小
                .cacheSize(2)
                // 以下选项适用于断线原因造成了未收到本地缓存更新消息的情况。
                // 断线重连的策略有以下几种：
                // CLEAR - 如果断线一段时间以后则在重新建立连接以后清空本地缓存
                // LOAD - 在服务端保存一份10分钟的作废日志
                //        如果10分钟内重新建立连接，则按照作废日志内的记录清空本地缓存的元素
                //        如果断线时间超过了这个时间，则将清空本地缓存中所有的内容
                // NONE - 默认值。断线重连时不做处理。
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.CLEAR)
                // 以下选项适用于不同本地缓存之间相互保持同步的情况
                // 缓存同步策略有以下几种：
                // INVALIDATE - 默认值。当本地缓存映射的某条元素发生变动时，同时驱逐所有相同本地缓存映射内的该元素
                // UPDATE - 当本地缓存映射的某条元素发生变动时，同时更新所有相同本地缓存映射内的该元素
                // NONE - 不做任何同步处理
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.INVALIDATE)
                // 每个Map本地缓存里元素的有效时间，默认毫秒为单位
                //.timeToLive(10000)
                // 或者
                .timeToLive(10000, TimeUnit.SECONDS)
                // 每个Map本地缓存里元素的最长闲置时间，默认毫秒为单位
                //.maxIdle(10000)
                // 或者
                .maxIdle(1, TimeUnit.HOURS);
        exmap = RedissonManager.getRedisson().getLocalCachedMap(cacheKey, options);
    }

    /**
     * 删除session和cache
     */
    public static void deleteCache(String sessionId) {
        exmap.fastRemoveAsync(RedisKey.CACHEKEY + sessionId);
    }

    /**
     * 获取存活的Cache
     */
    public static Set<Object> getActiveCache() {
        return (Set<Object>) exmap.values();
    }


    /**
     * 获取cache
     */
    public static Object doReadCache(String sessionId) {
        if (sessionId == null) {
            return null;
        }
        return exmap.get(RedisKey.CACHEKEY + sessionId);
    }

    /**
     * 保存cache,session并存储过期时间
     *
     * @param sessionId
     * @param obj
     */
    public static void saveCache(String sessionId, Object obj) {
        if (obj == null) {
            return;
        }
        exmap.fastPut(RedisKey.CACHEKEY + sessionId, obj);
    }



}
