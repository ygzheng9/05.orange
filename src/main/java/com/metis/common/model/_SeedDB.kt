package com.metis.common.model

import com.metis.common.StartConfig.Companion.createDruidPlugin
import com.jfinal.plugin.activerecord.ActiveRecordPlugin
import com.jfinal.plugin.activerecord.Db


fun main() {
    // 初始化数据库连接
    val druidPlugin = createDruidPlugin()
    druidPlugin.start()
    val arp = ActiveRecordPlugin(druidPlugin)

    // 所有映射在 MappingKit 中自动化搞定
    _MappingKit.mapping(arp)

    // 与 jfinal web 环境唯一的不同是要手动调用一次相关插件的start()方法
    druidPlugin.start();
    arp.start();

    println("executing.....")

    // 通过上面简单的几行代码，即可立即开始使用
//    seedOpp()
//    seedClientContact()

//    saveZUser()

    println("done.")
}



fun seedZUser() {
    // 模拟数据的总条数
    val total = 300

    // 模拟数据的可选项
    val departments = listOf("营销", "市场", "财务", "人力", "总裁办", "生产", "采购", "仓库")
    val emails = listOf("@163.com", "@gmail.net", "@cn.ibm.com", "@qq.com")
    val sex = listOf("男", "女")
    val cities = listOf("上海", "北京", "西安", "重庆", "甘肃", "天津", "云南")
    val companies = listOf("集团", "能源公司", "科技公司", "工程公司", "财务公司", "供应链公司")

    // 删除整张表
    Db.delete("truncate table t_zuser")

    // 插入模拟数据
    for(i in  1..total) {
        ZUser().set("userName", "用户-$i")
            .set("email", "$i${getRandom(emails)}")
            .set("company", getRandom(companies))
            .set("department", getRandom(departments))
            .set("city", getRandom(cities))
            .set("sex", getRandom(sex))
            .save();
    }
}

fun saveZUser() {
    ZUser().set("userName", "用户 1")
        .set("email", "wowo@gmail.com")
        .set("company", "getRandom(companies)")
        .set("department", "getRandom(departments)")
        .set("city", "getRandom(cities)")
        .set("sex", "getRandom(sex)")
        .save();
}


fun seedZClient() {
    // 模拟数据的总条数
    val total = 300

    // 模拟数据的可选项
    val industries = listOf("电力", "制造", "外贸", "房地产", "政府")
    val channelTypes = listOf("内销", "外销")
    val priorities = listOf("低", "中", "高")
    val addresses = listOf("杭州", "宁波", "绍兴", "台州")

    // 删除整张表
    Db.delete("truncate table t_zclient")

    // 插入模拟数据
    for(i in  1..total) {
        ZClient().set("clientName", "客户-${padPre(i, 3)}")
            .set("clientCode", "CC-${padPre(i, 3)}")
            .set("industry", "$i${getRandom(industries)}")
            .set("channelType", getRandom(channelTypes))
            .set("priority", getRandom(priorities))
            .set("address", getRandom(addresses))
            .save();
    }
}


fun seedOpp() {
    val total = 100

    val locations = listOf("杭州", "宁波", "绍兴", "台州")
    val clientIds = listOf("1","3", "5", "6", "9", "57")
    val bizTypes = listOf("opp", "project")
    val status = listOf("签约", "开工", "验收", "清款")
    val followUps = listOf("初步", "跟进中", "丢单", "赢")

    for(i in 1..total) {
        ZClientOpp().set("clientId", getRandom(clientIds))
            .set("bizType", getRandom(bizTypes))
            .set("projectName", "项目-${padPre(i, 3)}")
            .set("location", getRandom(locations))
            .set("clientOwner", "负责人-${padPre(1,2)}")
            .set("status", getRandom(status))
            .set("followUp", getRandom(followUps))
            .set("proposalLead", "PPTL-${padPre(i, 2)}")
            .save();
    }
}

fun seedClientContact() {
    val total = 100

    val clientIds = listOf("1","3", "5", "6", "9", "57")
    val departments = listOf("营销", "市场", "财务", "人力", "总裁办", "生产", "采购", "仓库")
    val titles = listOf("总裁", "总经理", "CIO", "VP", "CFO", "经理")

    for(i in 1..total) {
        ZClientContact().set("clientId", getRandom(clientIds))
            .set("personName", "联系人-${padPre(1,2)}")
            .set("department", getRandom(departments))
            .set("title", getRandom(titles))
            .set("coveredBy", "PPTL-${padPre(i, 2)}")
            .save();
    }
}



