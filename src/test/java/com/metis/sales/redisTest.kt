package com.metis.sales

import com.jfinal.plugin.redis.Redis
import com.jfinal.plugin.redis.RedisPlugin
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test


class RedisTest {
    @Test
    @DisplayName("测试 redis ")
    @Disabled
    fun foo() {
        val rp = RedisPlugin("myRedis", "localhost")
        // 与web下唯一区别是需要这里调用一次start()方法
        rp.start()

        Redis.use()["key"] = "value"
        val v = Redis.use().get<String>("key")

        Assertions.assertEquals(v, "value")
    }
}
