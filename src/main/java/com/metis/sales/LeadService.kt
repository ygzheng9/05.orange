package com.metis.sales

import com.jfinal.kit.JsonKit
import com.jfinal.kit.Kv
import com.jfinal.kit.StrKit
import com.jfinal.plugin.activerecord.Db
import com.jfinal.plugin.activerecord.Page
import com.metis.common.model.ZClient
import com.metis.common.model.ZLeadInfo

open class LeadService {
    private val infoDao = ZLeadInfo().dao()

    fun paginate(pageNumber: Int, pageSize: Int, params: String): Page<ZLeadInfo> {
        var conditions = Kv()
        if (params.isNotEmpty()) {
            conditions = JsonKit.parse(params, Kv::class.java)
        }

        return infoDao.template("conditions.findLead", conditions).paginate(pageNumber, pageSize)
    }

    fun findById(id: Int): ZLeadInfo {
        return infoDao.findById(id)
    }

    fun deleteById(id: Int): Boolean {
        return infoDao.deleteById(id)
    }

    fun deleteByBatch(ids: Array<String>): Int {
        val str: String = StrKit.join(ids, ",")
        return Db.update("delete from t_z_lead_info where id in($str)");
    }
}
