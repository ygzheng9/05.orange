package com.metis.sales

import cn.hutool.core.io.resource.ClassPathResource
import cn.hutool.crypto.SecureUtil
import cn.hutool.crypto.asymmetric.SignAlgorithm
import cn.hutool.setting.dialect.Props
import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable
import org.yaml.snakeyaml.Yaml


@DisplayName("单元测试示例")
class SimpleTest {
    @BeforeEach
    @DisplayName("每条用例开始时执行")
    fun start() {
    }

    @AfterEach
    @DisplayName("每条用例结束时执行")
    fun end() {
    }

    @Test
    @DisplayName("描述测试用例名称")
    fun myFirstTest() {
        Assertions.assertEquals(2, 1 + 1)
    }

    @Test
    @Disabled("这条用例暂时跑不过，忽略!")
    fun skipTest() {
        Assertions.assertEquals(1, 2)
    }

    @Test
    @DisplayName("运行一组断言")
    fun assertAllCase() {
        Assertions.assertAll(
            "groupAssert",
            Executable { Assertions.assertEquals(2, 1 + 1) },
            Executable { Assertions.assertTrue(1 > 0) }
        )
    }

    @Test
    @DisplayName("MD5 加密")
    fun testMD5() {
        val data = "我是一段测试字符串".toByteArray()
        val sign = SecureUtil.sign(SignAlgorithm.MD5withRSA)
        val signed = sign.sign(data)

        // 验证原始数据
        val verify = sign.verify(data, signed)
        Assertions.assertEquals(verify, true)


        // 验证修改后的数据
        val data2 = "我是一段测试字符串1".toByteArray()
        val verify2 = sign.verify(data2, signed)
        Assertions.assertEquals(verify2, false)
    }

    @Test
    @DisplayName("读取 properties 文件")
    fun readPropertyFile() {
        val props = Props()

        val resource = ClassPathResource("/demo-config-dev.txt")
        props.load(resource.stream)

        println(props)

        val user = props.getStr("user")

        Assertions.assertEquals(user, "dangerUser")
    }

    @Test
    @DisplayName("读取 yaml 对象")
    fun yamlReadOne() {
        val steam = ClassPathResource("/test.yaml").stream

        val yaml = Yaml()
        val ret = yaml.loadAs(steam, Person::class.java)
        println(ret)
    }
}
