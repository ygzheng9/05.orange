package com.metis.index

import cn.hutool.core.util.StrUtil
import com.google.common.base.Splitter
import com.jfinal.kit.JsonKit
import com.jfinal.kit.Kv
import com.jfinal.kit.StrKit
import com.jfinal.log.Log
import com.jfinal.plugin.activerecord.Db
import com.jfinal.plugin.activerecord.Page
import com.metis.common.model.Session
import com.metis.common.model.ZUser
import java.util.*


open class UserService {
    private val logger = Log.getLog(UserService::class.java)

    private val userDao = ZUser().dao()
    private val sessionDao = Session().dao()

    private var allUsers : List<ZUser>? = null;
    private var allSession : List<Session>? = null;
    private var allResources: HashMap<String, List<String>>? = null;

    private fun loadAllUsers() {
        allUsers = userDao.findAll()
    }

    fun loadAllSession() {
        allSession = sessionDao.findAll()
    }

    fun fromUserPool(userID : Int): ZUser {
        // 从全局变量中，返回一个对象
        if (allUsers == null) {
            loadAllUsers()
        }

        for (u in allUsers!!) {
            if (u.id == userID) {
                return ZUser().put(u)
            }
        }

        return ZUser()
    }

    fun fromSessionPool(sessionID: String): Session? {
        if (allSession == null) {
            loadAllSession()
        }
        for (a in allSession!!) {
            if (a.id == sessionID) {
                return Session().put(a)
            }
        }
        return null
    }

    private fun loadAllResources() {
        val resources =
            Db.template("auth.allResources").find()
        allResources = java.util.HashMap()

        // 固定格式：z_resources.permissions 空格 分割
        val splter = Splitter.on(" ")
        for (r in resources) {
            val key = r.get<String>("key")
            val s = r.get<String>("permissions")
            val fields = splter.splitToList(s)
            allResources!![key] = fields
        }
    }

    fun hasPermission(u: ZUser, actionKey: String?): Boolean  {
        if (allResources == null) {
            loadAllResources()
        }
        val perms = allResources!![actionKey]
            ?: // 约定：没有配置表示不需要做权限控制
            return true

        // 固定格式：user.role 中 空格 分割；
        val splitter = Splitter.on(" ")
        val roles: String = u.roles
        val fields = splitter.splitToList(roles)
        for (p in perms) {
            for (r in fields) {
                if (p.compareTo(r!!, ignoreCase = true) == 0) {
                    return true
                }
            }
        }
        return false
    }

    fun checkLogin(email: String, password: String): ZUser? {
        return userDao.template("auth.checkLogin", email, password).findFirst()
    }


    // CRUD
    fun paginate(pageNumber: Int, pageSize: Int, params: String, keyword: String): Page<ZUser> {
        var conditions = Kv()
        if (params.isNotEmpty()) {
            conditions = JsonKit.parse(params, Kv::class.java)
        }
        if (StrUtil.isNotBlank(keyword)) {
            conditions["keyword"] = keyword
        }

        return userDao.template("conditions.findUser", conditions).paginate(pageNumber, pageSize)
  }

    fun findById(id: Int): ZUser {
        return userDao.findById(id)
    }

    fun deleteById(id: Int): Boolean {
        return userDao.deleteById(id)
    }

    fun deleteByBatch(ids: Array<String>): Int {
        val str: String = StrKit.join(ids, ",")
        return Db.update("delete from t_z_user where id in($str)");
    }
}
