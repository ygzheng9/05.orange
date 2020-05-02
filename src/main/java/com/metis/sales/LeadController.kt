package com.metis.sales

import com.jfinal.aop.Inject
import com.jfinal.core.Controller
import com.jfinal.kit.JsonKit
import com.jfinal.kit.Ret
import com.metis.common.buildLayuiTable
import com.metis.common.model.ZClient
import com.metis.common.model.ZLeadInfo

class LeadController : Controller() {
    @Inject
    lateinit var infoSvc: LeadService

    fun index() {
        render("lead_list.html")
    }

    fun list() {
        render("lead_list.html")
    }

    fun entry() {
        var entry = ZLeadInfo()
        entry.id = 0

        val id = getInt("id", 0)
        if (id > 0) {
            entry = infoSvc.findById(id)
        }

        set("entry", entry)
        render("lead_entry.html")
    }

    fun find() {
        // layUI 分页默认是这两个参数名
        val pageNum = getInt("page", 1)
        val pageSize = getInt("limit", 20)

        // layui table 上面的查询条件传入
        val params = get("params", "")

        val items = infoSvc.paginate(pageNum, pageSize, params)

        val results = buildLayuiTable(items)

        renderJson(results)
    }

    fun deleteBy() {
        val id = getInt("id", 0)
        infoSvc.deleteById(id)
        renderJson(Ret.ok())
    }

    fun deleteBatch() {
        val param = get("param", "")
        val ids = JsonKit.parse(param, Array<String>::class.java)
        infoSvc.deleteByBatch(ids)
        renderJson(Ret.ok())
    }

    fun createOrUpdate() {
        val param = get("param", "")
        val entry = JsonKit.parse(param, ZLeadInfo::class.java)

        // 根据 id 判断是 更新 还是 新增
        val op = if (entry.id > 0) entry::update else entry::save
        if ( op.invoke() ) {
            renderJson(Ret.ok())
        } else {
            renderJson(Ret.fail())
        }
    }
}
