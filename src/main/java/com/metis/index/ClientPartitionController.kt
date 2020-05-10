package com.metis.index

import com.jfinal.aop.Inject
import com.jfinal.core.Controller
import com.jfinal.kit.Ret

class ClientPartitionController : Controller() {
    @Inject
    lateinit var partitionSvc: ClientPartitionService

    fun load() {
        partitionSvc.loadData()

        renderJson(Ret.ok())
    }

    fun calc() {
        val items = partitionSvc.findAll().toMutableList()
        partitionSvc.calc(items)

        renderJson(Ret.ok())
    }
}
