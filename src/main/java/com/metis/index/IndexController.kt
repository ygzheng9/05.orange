package com.metis.index

import cn.hutool.core.util.StrUtil
import com.jfinal.aop.Clear
import com.jfinal.aop.Inject
import com.jfinal.core.Controller
import com.jfinal.kit.JsonKit
import com.jfinal.kit.Kv
import com.jfinal.kit.Ret
import com.metis.common.buildLayuiTable
import com.metis.common.model.ZCompany
import com.metis.common.model.ZDepartment
import com.metis.common.model.ZUser

class IndexController : Controller() {
    @Inject
    lateinit var userSvc: UserService

    @Inject
    lateinit var companySvc: CompanyService

    @Inject
    lateinit var departmentSvc: DepartmentService

    @Inject
    lateinit  var loginSvc: LoginService

    // 演示专用
    fun rxjs() {
        render("rxjs.html")
    }

    fun demoWelcome() {
        render("demo/welcome.html");
    }

    fun demoUpload() {
        val f = file
        println(f)

        renderJson(Ret.ok())
    }

    // 正式开始
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

    @Clear(LoginInterceptor::class, AuthInterceptor::class)
    fun login() {
        render("login.html")
    }

    @Clear(LoginInterceptor::class, AuthInterceptor::class)
    fun doLogin() {
        val param = get("param", "")
        val kv = JsonKit.parse(param, Kv::class.java)

        val ret: Ret = loginSvc.login(kv.getStr("email"), kv.getStr("password"), true)
        if (ret.isOk) {
            val sessionId = ret.getStr(LoginService.sessionIdName)
            val maxAgeInSeconds = ret.getInt("maxAgeInSeconds")
            setCookie(LoginService.sessionIdName, sessionId, maxAgeInSeconds, true)
            set(LoginService.loginAccountCacheName, ret[LoginService.loginAccountCacheName])

            // 如果 returnUrl 存在则跳过去，否则跳去首页
            ret["returnUrl"] = getPara("returnUrl", "/")
        }
        renderJson(ret)
    }

    @Clear(LoginInterceptor::class, AuthInterceptor::class)
    fun page404() {
        render("404.html")
    }

    fun logout() {
        // 退出后，删除 cookie
        removeCookie(LoginService.sessionIdName)
        forwardAction("/login")
    }


    // 业务逻辑
    // user 管理
    fun userList() {
        render("user/list.html")
    }

    fun userFind() {
        // layUI 分页默认是这两个参数名
        val pageNum = getInt("page", 1)
        val pageSize = getInt("limit", 20)

        // layui table 上面的查询条件传入
        val params = get("params", "")

        // 下拉选择时，查询条件
        val keyword = get("keyword", "")

        val users = userSvc.paginate(pageNum, pageSize, params, keyword)

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
        if (id > 0 ) {
            user = userSvc.findById(id)
        }

        fun <T> getOptions(items : List<T>) : List<Kv> {
            return items.map { Kv().set("value", it).set("label", it) }
        }

        val cities = getOptions(listOf("上海", "北京", "西安", "重庆", "甘肃", "天津", "云南"))
        val departments = getOptions(listOf("营销", "市场", "财务", "人力", "总裁办", "生产", "采购", "仓库"))
        val companies = getOptions(listOf("集团", "能源公司", "科技公司", "工程公司", "财务公司", "供应链公司"));
        val genders = getOptions(listOf("男", "女", "未知"))

        val roleList = listOf("admin", "root", "user")

        val roles = getOptions(roleList)
        val checkedRoles = StrUtil.split(user.roles, ' ', true, true)

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
        val op = if (user.id != 0) user::update else user::save
        if ( op.invoke() ) {
            renderJson(Ret.ok())
        } else {
            renderJson(Ret.fail())
        }
    }

    ///////////// company
    fun companyList() {
        render("company_list.html")
    }

    fun companyFind() {
        // layUI 分页默认是这两个参数名
        val pageNum = getInt("page", 1)
        val pageSize = getInt("limit", 20)

        // layui table 上面的查询条件传入
        val params = get("params", "")
        val keyword = get("keyword", "")

        val items = companySvc.paginate(pageNum, pageSize, params, keyword)

        val results = buildLayuiTable(items)

        renderJson(results)
    }

    fun companyDelete() {
        val id = getInt("id", 0)
        companySvc.deleteById(id)
        renderJson(Ret.ok())
    }

    fun companyDeleteBatch() {
        val param = get("param", "")
        val ids = JsonKit.parse(param, Array<String>::class.java)
        companySvc.deleteByBatch(ids)
        renderJson(Ret.ok())
    }

    fun companyEntry() {
        var entry = ZCompany()
        entry.id = 0

        val id = getInt("id", 0)
        if (id > 0) {
            entry = companySvc.findById(id)
        }

        set("entry", entry)
        render("company_entry.html")
    }

    fun companyCreateOrUpdate() {
        val param = get("param", "")
        val entry = JsonKit.parse(param, ZCompany::class.java)

        // 根据 id 判断是 更新 还是 新增
        val op = if (entry.id > 0) entry::update else entry::save
        if ( op.invoke() ) {
            renderJson(Ret.ok())
        } else {
            renderJson(Ret.fail())
        }
    }

    ///////////// department
    fun departmentList() {
        render("department_list.html")
    }

    fun departmentFind() {
        // layUI 分页默认是这两个参数名
        val pageNum = getInt("page", 1)
        val pageSize = getInt("limit", 20)

        // layui table 上面的查询条件传入
        val params = get("params", "")

        val items = departmentSvc.paginate(pageNum, pageSize, params)

        val results = buildLayuiTable(items)

        renderJson(results)
    }

    fun departmentDelete() {
        val id = getInt("id", 0)
        departmentSvc.deleteById(id)
        renderJson(Ret.ok())
    }

    fun departmentDeleteBatch() {
        val param = get("param", "")
        val ids = JsonKit.parse(param, Array<String>::class.java)
        departmentSvc.deleteByBatch(ids)
        renderJson(Ret.ok())
    }

    fun departmentEntry() {
        var entry = ZDepartment()
        entry.id = 0

        val id = getInt("id", 0)
        if (id > 0) {
            entry = departmentSvc.findById(id)
        }

        set("entry", entry)
        render("department_entry.html")
    }

    fun departmentCreateOrUpdate() {
        val param = get("param", "")
        val entry = JsonKit.parse(param, ZDepartment::class.java)

        // 根据 id 判断是 更新 还是 新增
        val op = if (entry.id > 0) entry::update else entry::save
        if ( op.invoke() ) {
            renderJson(Ret.ok())
        } else {
            renderJson(Ret.fail())
        }
    }
}

