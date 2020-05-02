package com.metis.index

import cn.hutool.core.util.StrUtil
import com.jfinal.kit.JsonKit
import com.jfinal.kit.Kv
import com.jfinal.kit.StrKit
import com.jfinal.plugin.activerecord.Db
import com.jfinal.plugin.activerecord.Page
import com.metis.common.model.ZCompany

open class CompanyService {
    private val dao = ZCompany().dao()

    fun paginate(pageNumber: Int, pageSize: Int, params: String, keyword: String): Page<ZCompany> {
        var conditions = Kv()
        if (StrUtil.isNotBlank(params)) {
            conditions = JsonKit.parse(params, Kv::class.java)
        }
        if (StrUtil.isNotBlank(keyword)) {
            conditions["keyword"] = keyword
        }

        return dao.template("conditions.findCompany", conditions).paginate(pageNumber, pageSize)
    }

    fun findById(id: Int): ZCompany {
        return dao.findById(id)
    }

    fun deleteById(id: Int): Boolean {
        return dao.deleteById(id)
    }

    fun deleteByBatch(ids: Array<String>): Int {
        val str: String = StrKit.join(ids, ",")
        return Db.update("delete from t_z_company where id in($str)");
    }
}
