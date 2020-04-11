package com.metis.sales

import com.metis.common.LayerTable
import com.jfinal.aop.Inject
import com.jfinal.core.Controller
import com.jfinal.kit.JsonKit
import com.jfinal.kit.Ret
import com.metis.common.buildLayuiTable
import com.metis.common.model.*


/*
2020-04-06 zhengyg
 */

class ClientController : Controller() {
    @Inject
    lateinit var clientSvc: ClientService

    fun list() {
        render("client_list.html")
    }

    fun entry() {
        var entry = ZClient()
        entry.id = 0

        val id = getInt("id", 0)
        if (id > 0) {
            entry = clientSvc.findById(id)
        }

        set("entry", entry)
        render("client_entry.html")
    }


    fun newClient() {
        val entry = ZClient()
        entry.id = 0;

        set("entry", entry);
        render("newClient.html")
    }

    fun find() {
        // layUI 分页默认是这两个参数名
        val pageNum = getInt("page", 1)
        val pageSize = getInt("limit", 20)

        // layui table 上面的查询条件传入
        val params = get("params", "")

        val items = clientSvc.paginate(pageNum, pageSize, params)

        val results = buildLayuiTable(items)

        renderJson(results)
    }

    fun deleteBy() {
        val id = getInt("id", 0)
        clientSvc.deleteById(id)
        renderJson(Ret.ok())
    }

    fun deleteBatch() {
        val param = get("param", "")
        val ids = JsonKit.parse(param, Array<String>::class.java)
        clientSvc.deleteByBatch(ids)
        renderJson(Ret.ok())
    }

    fun createOrUpdate() {
        val param = get("param", "")
        val entry = JsonKit.parse(param, ZClient::class.java)

        // 根据 id 判断是 更新 还是 新增
        val op = if (entry.id > 0) entry::update else entry::save
        if ( op.invoke() ) {
            renderJson(Ret.ok())
        } else {
            renderJson(Ret.fail())
        }
    }

    fun basicInfo() {
        val clientId = getInt("clientId", 0)

        if (clientId == 0) {
            this.list()
        } else {
            val client = clientSvc.findById(clientId)
            set("client", client);
            render("basic_info.html");
        }
    }

    // 客户拜访记录
    fun callEntry() {
        var entry = ZClientCall()
        entry.id = 0

        val id = getInt("id", 0)
        if (id > 0) {
            entry = clientSvc.callFindById(id)
        } else {
            entry.clientId = getInt("clientId", 0);
        }

        set("entry", entry)
        render("call_entry.html")
    }

    fun callFind() {
        val clientId = getInt("clientId", 0)

        // layUI 分页默认是这两个参数名
        val pageNum = getInt("page", 1)
        val pageSize = getInt("limit", 20)

        val items = clientSvc.callPaginate(pageNum, pageSize, clientId)

        val results = buildLayuiTable(items)

        renderJson(results)
    }

    fun callDeleteBy() {
        val id = getInt("id", 0)
        clientSvc.callDeleteById(id)
        renderJson(Ret.ok())
    }

    fun callDeleteBatch() {
        val param = get("param", "")
        val ids = JsonKit.parse(param, Array<String>::class.java)
        clientSvc.callDeleteByBatch(ids)
        renderJson(Ret.ok())
    }

    fun callCreateOrUpdate() {
        val param = get("param", "")
        val entry = JsonKit.parse(param, ZClientCall::class.java)

        // 根据 id 判断是 更新 还是 新增
        val op = if (entry.id > 0) entry::update else entry::save
        if ( op.invoke() ) {
            renderJson(Ret.ok())
        } else {
            renderJson(Ret.fail())
        }
    }

    // 客户潜能指标
    fun potentialEntry() {
        var entry = ZClientPotential()

        val clientId = getInt("clientId", 0)
        if (clientId > 0) {
            entry = clientSvc.potentialFindByClient(clientId)
        }

        set("entry", entry)
        render("potential_entry.html")
    }

    fun potentialShow() {
        var entry = ZClientPotential()

        val clientId = getInt("clientId", 0)
        if (clientId > 0) {
            entry = clientSvc.potentialFindByClient(clientId)
        }

        set("entry", entry)
        render("potential_show.html")
    }


    fun potentialCreateOrUpdate() {
        val param = get("param", "")
        val entry = JsonKit.parse(param, ZClientPotential::class.java)

        // 根据 id 判断是 更新 还是 新增
        val op = if (entry.id > 0) entry::update else entry::save
        if ( op.invoke() ) {
            renderJson(Ret.ok())
        } else {
            renderJson(Ret.fail())
        }
    }

    // 项目过往历史
    fun clientHistory() {
        val clientId = getInt("clientId", 0)
        if (clientId == 0) {
            this.list()
            return
        }

        val client = clientSvc.findById(clientId)
        set("client", client);
        render("client_history.html");
    }

    fun historyProject() {
        val clientId = getInt("clientId", 0)
        val projects = clientSvc.historyProjectList(clientId)

        val results = buildLayuiTable(projects)
        renderJson(results)
    }

    fun historyOpp() {
        val clientId = getInt("clientId", 0)
        val items = clientSvc.historyOppList(clientId)

        val results = buildLayuiTable(items)
        renderJson(results)
    }

    fun oppEntry() {
        var entry = ZClientOpp()
        entry.id = 0

        val id = getInt("id", 0)
        if (id > 0) {
            entry = clientSvc.oppFindById(id)
        } else {
            entry.clientId = getInt("clientId", 0);
        }

        set("entry", entry)
        render("opp_entry.html")
    }

    fun oppCreateOrUpdate() {
        val param = get("param", "")
        val entry = JsonKit.parse(param, ZClientOpp::class.java)

        // 根据 id 判断是 更新 还是 新增
        val op = if (entry.id > 0) entry::update else entry::save
        if ( op.invoke() ) {
            renderJson(Ret.ok())
        } else {
            renderJson(Ret.fail())
        }
    }

    fun oppDeleteBy() {
        val id = getInt("id", 0)
        clientSvc.oppDeleteById(id)
        renderJson(Ret.ok())
    }

    // 客户联系人
    fun clientContact() {
        val clientId = getInt("clientId", 0)
        if (clientId == 0) {
            this.list()
            return
        }

        val client = clientSvc.findById(clientId)
        set("client", client);
        render("client_contact.html");
    }

    fun contactList() {
        val clientId = getInt("clientId", 0)
        val projects = clientSvc.contactList(clientId)

        val results = buildLayuiTable(projects)
        renderJson(results)
    }

    fun contactEntry() {
        var entry = ZClientContact()
        entry.id = 0

        val id = getInt("id", 0)
        if (id > 0) {
            entry = clientSvc.contactFindById(id)
        } else {
            entry.clientId = getInt("clientId", 0);
        }

        set("entry", entry)
        render("contact_entry.html")
    }

    fun contactCreateOrUpdate() {
        val param = get("param", "")
        val entry = JsonKit.parse(param, ZClientContact::class.java)

        // 根据 id 判断是 更新 还是 新增
        val op = if (entry.id > 0) entry::update else entry::save
        if ( op.invoke() ) {
            renderJson(Ret.ok())
        } else {
            renderJson(Ret.fail())
        }
    }

    fun contactDeleteBy() {
        val id = getInt("id", 0)
        clientSvc.contactDeleteById(id)
        renderJson(Ret.ok())
    }

    // 组织结构图，不实用，用户修改起来不方便
    fun clientOrg() {
        render("client_org.html")
    }
}
