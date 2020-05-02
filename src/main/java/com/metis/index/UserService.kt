package com.metis.index

import cn.hutool.core.util.StrUtil
import com.metis.common.model.ZUser
import com.jfinal.kit.JsonKit
import com.jfinal.kit.Kv
import com.jfinal.kit.StrKit
import com.jfinal.plugin.activerecord.Db
import com.jfinal.plugin.activerecord.Page

open class UserService {
    private val userDao = ZUser().dao()

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
