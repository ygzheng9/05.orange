package com.metis.sales

import com.jfinal.kit.JsonKit
import com.jfinal.kit.Kv
import com.jfinal.kit.StrKit
import com.jfinal.plugin.activerecord.Db
import com.jfinal.plugin.activerecord.Page
import com.metis.common.model.*

open class ClientService {
    // 客户基本信息
    private val clientDao = ZClient().dao()

    // 客户拜访记录
    private val callDao = ZClientCall().dao()

    // 客户潜力评估指标
    private val potentialDao = ZClientPotential().dao()

    // 机会 和 项目
    private val oppDao = ZClientOpp().dao()

    // 相关决策人
    private val contactDao = ZClientContact().dao()


    fun paginate(pageNumber: Int, pageSize: Int, params: String): Page<ZClient> {
        var conditions = Kv()
        if (params.isNotEmpty()) {
            conditions = JsonKit.parse(params, Kv::class.java)
        }

        return clientDao.template("conditions.findClient", conditions).paginate(pageNumber, pageSize)
    }

    fun findById(id: Int): ZClient {
        return clientDao.findById(id)
    }

    fun deleteById(id: Int): Boolean {
        return clientDao.deleteById(id)
    }

    fun deleteByBatch(ids: Array<String>): Int {
        val str: String = StrKit.join(ids, ",")
        return Db.update("delete from t_zuser where id in($str)");
    }

    fun callPaginate(pageNumber: Int, pageSize: Int, clientid: Int): Page<ZClientCall> {
        return callDao.template("conditions.findCall", clientid).paginate(pageNumber, pageSize)
    }

    fun callFindById(id: Int): ZClientCall {
        return callDao.findById(id)
    }

    fun callDeleteById(id: Int): Boolean {
        return callDao.deleteById(id)
    }

    fun callDeleteByBatch(ids: Array<String>): Int {
        val str: String = StrKit.join(ids, ",")
        return Db.update("delete from t_z_client_call where id in($str)");
    }

    fun potentialFindByClient(clientid: Int) : ZClientPotential {
        val items = potentialDao.template("conditions.findPotentialByClient", clientid).find()

        if (items.size > 0) {
            return items[0]
        }

        val a =  ZClientPotential()
        a.id = 0
        a.clientid = clientid

        return a
    }

    // 客户历史订单和商机
    fun historyProjectList(clientId: Int) : List<ZClientOpp> {
        return oppDao.template("conditions.findHistory", "project", clientId).find()
    }

    fun historyOppList(clientId: Int) : List<ZClientOpp> {
        return oppDao.template("conditions.findHistory", "opp",  clientId).find()
    }

    fun oppFindById(id: Int): ZClientOpp {
        return oppDao.findById(id)
    }

    fun oppDeleteById(id: Int): Boolean {
        return oppDao.deleteById(id)
    }

    // 客户联系人信息
    fun contactList(clientId: Int) : List<ZClientContact> {
        return contactDao.template("conditions.findContact", clientId).find()
    }

    fun contactFindById(id: Int): ZClientContact {
        return contactDao.findById(id)
    }

    fun contactDeleteById(id: Int): Boolean {
        return contactDao.deleteById(id)
    }
}
