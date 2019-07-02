package DemoTest;

import nafos.security.redis.RedissonManager;
import org.redisson.config.Config;

/**
 * @ClassName (Test)
 * @Desc DOTO
 * @Author hxy
 * @Date 2019/7/1 19:19
 **/
public class Test {

    public static void main(String[] args) {
//        System.out.println("==="+LocalCacheRedisson.class.getClass().getResource("/").getPath() );
        Config config = new Config();
        config.useMasterSlaveServers()
                .setMasterAddress("redis://193.112.111.80:6379")
                .addSlaveAddress("redis://193.112.111.80:6379")
                .setDatabase(5).setPassword("yunhai2018jiayou");

        RedissonManager.init(config);
        LocalCacheRedisson.saveCache("3","8");
        LocalCacheRedisson.saveCache("4","8");
        LocalCacheRedisson.saveCache("1","8");
        LocalCacheRedisson.saveCache("2","8");
        System.out.println(LocalCacheRedisson.doReadCache("3"));
        RedissonManager.getRedisson().shutdown();
    }


}
