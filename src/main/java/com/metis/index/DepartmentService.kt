package com.metis.index

import com.jfinal.kit.JsonKit
import com.jfinal.kit.Kv
import com.jfinal.kit.StrKit
import com.jfinal.plugin.activerecord.Db
import com.jfinal.plugin.activerecord.Page

import com.metis.common.model.ZDepartment

open class DepartmentService {
    private val dao = ZDepartment().dao()

    fun paginate(pageNumber: Int, pageSize: Int, params: String): Page<ZDepartment> {
        var conditions = Kv()
        if (params.isNotEmpty()) {
            conditions = JsonKit.parse(params, Kv::class.java)
        }

        return dao.template("conditions.findDepartment", conditions).paginate(pageNumber, pageSize)
    }

    fun findById(id: Int): ZDepartment {
        // 多表 join，而不是单纯的一张表
        //        return dao.findById(id)
        return dao.template("conditions.findDepartmentById", id).findFirst()
    }

    fun deleteById(id: Int): Boolean {
        return dao.deleteById(id)
    }

    fun deleteByBatch(ids: Array<String>): Int {
        val str: String = StrKit.join(ids, ",")
        return Db.update("delete from t_z_department where id in($str)");
    }
}
