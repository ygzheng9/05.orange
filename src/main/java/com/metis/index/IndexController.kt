package com.metis.index

import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.StrUtil

import com.metis.common.LayerTable
import com.metis.common.model.ZUser

import com.jfinal.aop.Inject
import com.jfinal.core.Controller
import com.jfinal.kit.JsonKit
import com.jfinal.kit.Kv
import com.jfinal.kit.Ret
import com.jfinal.template.Engine
import com.metis.common.buildLayuiTable

class IndexController : Controller() {
    @Inject
    lateinit var userSvc: UserService

    fun index() {
        render("index.html")
    }

    fun welcomePage1() {
        render("welcomePage1.html")
    }

    fun welcomePage2() {
        render("welcomePage2.html")
    }

    fun welcomePage3() {
        render("welcomePage3.html")
    }

    fun formDemo() {
        render("form.html");
    }

    fun userList() {
        render("user/list.html")
    }

    fun userFind() {
        // layUI 分页默认是这两个参数名
        val pageNum = getInt("page", 1)
        val pageSize = getInt("limit", 20)

        // layui table 上面的查询条件传入
        val params = get("params", "")

        val users = userSvc.paginate(pageNum, pageSize, params)

        val results = buildLayuiTable(users)

        renderJson(results)
    }

    fun userDelete() {
        val id = getInt("id", 0)
        userSvc.deleteById(id)
        renderJson(Ret.ok())
    }

    fun userDeleteBatch() {
        val param = get("param", "")
        val ids = JsonKit.parse(param, Array<String>::class.java)
        userSvc.deleteByBatch(ids)
        renderJson(Ret.ok())
    }

    fun userEntry() {
        var user = ZUser()
        user.id = 0
        user.sex = "未知"

        val id = getInt("id", 0)
        if (id > 0) {
            user = userSvc.findById(id)
        }

        fun <T> getOptions(items : List<T>) : List<Kv> {
            return items.map { Kv().set("value", it).set("label", it) }
        }

        val cities = getOptions(listOf("上海", "北京", "西安", "重庆", "甘肃", "天津", "云南"))
        val departments = getOptions(listOf("营销", "市场", "财务", "人力", "总裁办", "生产", "采购", "仓库"))
        val companies = getOptions(listOf("集团", "能源公司", "科技公司", "工程公司", "财务公司", "供应链公司"));
        val genders = getOptions(listOf("男", "女", "未知"))

        val roleList = ArrayList<String>()
        for (i in 1..30) {
            roleList.add("角色-$i")
        }
        val roles = getOptions(roleList)
        val checkedRoles = StrUtil.split(user.roles, ' ', true, true)
//        println(checkedRoles)

        set("user", user)
        set("cities", cities)
        set("departments", departments)
        set("companies", companies)
        set("genders", genders)
        set("roles", roles)
        set("checkedRoles", checkedRoles)
        render("user/entry.html")
    }

    fun userCreateOrUpdate() {
        val param = get("param", "")
        val user = JsonKit.parse(param, ZUser::class.java)

        // 根据 id 判断是 更新 还是 新增
        val op = if (user.id > 0) user::update else user::save
        if ( op.invoke() ) {
            renderJson(Ret.ok())
        } else {
            renderJson(Ret.fail())
        }
    }
}

