package com.metis.common.model.mock;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin
import org.junit.jupiter.api.*
import kotlin.random.Random

import com.metis.common.StartConfig
import com.metis.common.model.*

@Disabled("生成数据库中的模拟数据，只需要手工执行，不需要自动化测试")
@DisplayName("生成模拟数据")
class DbSeed {
    lateinit var mockData: MockData;

    @BeforeEach
    @DisplayName("初始化数据库连接")
    fun start() {
        // 初始化数据库连接
        val druidPlugin = StartConfig.createDruidPlugin()
        druidPlugin.start()
        val arp = ActiveRecordPlugin(druidPlugin)

        // 所有映射在 MappingKit 中自动化搞定
        _MappingKit.mapping(arp)

        // 与 jfinal web 环境唯一的不同是要手动调用一次相关插件的start()方法
        druidPlugin.start();
        arp.start();

        mockData = MockData.loadMockData()

        println("ARP ready")
    }

    @AfterEach
    @DisplayName("执行完毕")
    fun finish() {
        println("All Done.")
    }

    @Test
    @DisplayName("读取 yaml 对象")
    fun yamlReadOne() {
        val mock = MockData.loadMockData()
        println(mock)

        println(mock.getCategory("department"));
    }

    @Test
    @DisplayName("Mock lead")
    fun seedLead() {
        val total = 500

        for(i in 1..total) {
            ZLeadInfo().set("leadName", "商机-${padPre(i, 3)}")
                .set("clientName","客户-${padPre(Random.nextInt(0, 100), 3)}")
                .set("leadNum", "L-${padPre(i, 3)}")
                .set("leadLocation", getRandom(mockData.getCategory("location")))
                .set("leadOwner", getRandom(mockData.getCategory("person")))
                .set("leadPhase", getRandom(mockData.getCategory("phase")))
                .save();
        }
    }

    @Test
    @DisplayName("Mock company")
    fun seedCompany() {
        val total = 50

        for(i in 1..total) {
            ZCompany().set("companyName", "公司-${padPre(i, 3)}")
                .set("companyCode","C-${padPre(i, 3)}")
                .set("address", getRandom(mockData.getCategory("location")))
                .set("bizUnit", getRandom(mockData.getCategory("department")))
                .save();
        }
    }

    @Test
    @DisplayName("Mock department")
    fun seedDepartment() {
        val total = 50

        for(i in 1..total) {
            ZDepartment().set("departmentName", "部门-${padPre(i, 3)}")
                .set("departmentCode","D-${padPre(i, 3)}")
                .set("companyId", Random.nextInt(0, 20))
                .set("headId", Random.nextInt(0, 30))
                .save();
        }
    }
}
