package com.metis.index

import cn.hutool.core.util.StrUtil
import cn.hutool.poi.excel.ExcelReader
import com.google.common.base.Splitter
import com.jfinal.kit.PropKit
import com.jfinal.plugin.activerecord.Db
import com.metis.common.model.ZMClientPartition
import com.metis.common.model.ZMClientPartitionScore
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.sin

typealias MiddleResultT = HashMap<Int, HashMap<String, String>>

open class ClientPartitionService {
    private val partitionDao = ZMClientPartition().dao()

    private fun getNumber(s: String) : BigDecimal {
        // 从 db 中取出 string 转换成 BigDecimal
        try {
            if (StrUtil.isBlank(s)) {
                return BigDecimal.ZERO
            }

            return BigDecimal(s)
        } catch (e:NumberFormatException) {
            println(e)

            return BigDecimal.ZERO
        }
    }

    private fun rankScore(rank : Int, base: Int, step: Int) : Double {
        // 按照排名，计算得分
        // 前 5 名，得 5 分；然后每 5 名依次递减 0.5 分
        // <= 5 -> 5; 6-10 -> 4.5; ....

        val goal = 5.0

        if (rank <= base) {
            return goal
        }
        val a = rank - base
        val b = (a / step + 1) * 0.5
        var c = goal - b
        if (c < 0) {
            c = 0.0
        }
        return c
    }

    fun loadData() {
        // 从文件中加载 问卷
        val fileName = "${PropKit.get("dataDir")}/client_partition.xlsx"
        val reader = ExcelReader(fileName, 0)

        val result = ArrayList<ZMClientPartition>()

        // 格式化时间
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        // 输出已经格式化的现在时间（24小时制）
        val now = sdf.format(Date())

        // 从 0 开始，是 excel 的第一行；
        // 这里是 1，也即：从第二行开始，因为第一行是标题行
        var idx = 1
        while (true) {
            val lineRaw = reader.readRow(idx)

            val line = lineRaw.map { it?.toString() ?: "" }

            val entry = ZMClientPartition()

            // 空行，或者第一列为空白 ==》 文件末尾
            if (line.isEmpty() || StrUtil.isBlank(line[0] as String)) {
                break
            }

            entry.clientName = line[0]
            entry.newPlayer = line[1]
            entry.salesVol = getNumber(line[2])
            entry.incRate = getNumber(line[3])
            entry.newModelCnt = getNumber(line[4])
            entry.brandCat = line[5]
            entry.holderCat = line[6]
            entry.riskLevel = getNumber(line[7])
            entry.score = getNumber(line[8])
            entry.partition = line[9]

            entry.updateAt = now

            result.add(entry)

            idx += 1
        }

        // 删除之前提交的信息
        Db.delete("delete from z_m_client_partition")

        // 保存
        result.forEach { it.save() }
    }

    fun findAll() : List<ZMClientPartition> {
        return partitionDao.findAll()
    }

    fun calc(inputItems: MutableList<ZMClientPartition>)  {
        // 记录中间计算结果
        val middle = MiddleResultT()
        inputItems.forEach {
            val n = HashMap<String, String>()
            n["clientName"] = it.clientName

            // 后续计算中会用到
            n["newPlayer"] = it.newPlayer
            middle[it.id] = n
        }

        // 按指定列排序，计算得分
        // 使用了 inputItems ，所以不能放到外面
        fun rankBy(col: String) {
            val k1 = "${col}_rank"
            val k2 = "${col}_score"

            inputItems.sortByDescending { it.get<BigDecimal>(col) }
            inputItems.forEachIndexed { index, it ->
                val entry = middle[it.id]!!
                val r = index + 1

                entry[k1] = r.toString()
                entry[k2] = rankScore(r, 5, 5).toString()
            }
        }

        // 得分：销量、增长率
        rankBy("salesVol")
        rankBy("incRate")

        // 得分：信用风险
        rankBy("riskLevel")
        // 修正得分
        val highRisk = listOf("三方采购", "超过12个月未取得融资")
        inputItems.forEach {
            val entry = middle[it.id]!!
            if (it.holderCat == highRisk[0] || it.holderCat == highRisk[1]) {
                entry["riskLevel_score"] = "0.5"
            }
        }

        // 品牌、股东、研发数量 一起做
        calcBatch(inputItems, middle)

        // 计算综合得分
        calcWeightedScore(middle)

        // 根据综合得分，做分类
        calcWeightedCate(middle)

//        // 打印信息
//        middle.forEach {
//            println("${it.key}: ${it.value}")
//        }

        saveResult(middle)
    }

    // TODO: 做一个界面，把结果展现出来, 可以按类型过滤
    // TODO: 整理下条件规则，变成可配置的，首先通过文件配置，再做个界面配置

    private fun saveResult(middle: MiddleResultT) {
        // 格式化时间
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        // 输出已经格式化的现在时间（24小时制）
        val now = sdf.format(Date())

        // 删除之前提交的信息
        Db.delete("delete from z_m_client_partition_score")

        // 计算结果保存进数据库
        middle.forEach {
            val entry = it.value
            ZMClientPartitionScore()
                .set("refId", it.key)
                .set("clientName", entry["clientName"])
                .set("newPlayer", entry["newPlayer"])
                .set("salesVolRank", entry["salesVol_rank"])
                .set("salesVolScore", entry["salesVol_score"])
                .set("incRateRank", entry["incRate_rank"])
                .set("incRateScore", entry["incRate_score"])
                .set("holderCatRank", entry["holderCat_rank"])
                .set("holderCatScore", entry["holderCat_score"])
                .set("riskLevelRank", entry["riskLevel_rank"])
                .set("riskLevelScore", entry["riskLevel_score"])
                .set("brandCatRank", entry["brandCat_rank"])
                .set("brandCatScore", entry["brandCat_score"])
                .set("newModelCntRank", entry["newModelCnt_rank"])
                .set("newModelCntScore", entry["newModelCnt_score"])
                .set("weightedScore", entry["weighted_score"])
                .set("weightedScoreRank", entry["weighted_score_rank"])
                .set("weightedScoreScore", entry["weighted_score_score"])
                .set("weightedScoreCate", entry["weighted_score_cate"])
                .set("weightedScoreCateAdj", entry["weighted_score_cate_adj"])
                .set("updateAt", now)
                .save()
        }
    }

    private fun calcBatch(inputItems:MutableList<ZMClientPartition>, middle: MiddleResultT) {
        // 品牌、股东、研发数量 一起做

        // 规则：品牌
        val brandMap = mapOf(
            "豪华车" to 5,
            "合资车" to 4,
            "国资背景品牌或高端新能源车企" to 3,
            "民营品牌或新能源车企" to 2,
            "其他车型均价低于10万的品牌" to 1
        )

        // 规则：股东
        val splitter = Splitter.on(" ")
        val holderInfo = listOf(
            "3个月内取得融资 国资背景",
            "6个月内取得融资 合资",
            "9个月内取得融资 民营/地方企业",
            "12个月内取得融资 相关资本方无汽车领域经",
            "超过12个月未取得融资 三方采购"
        ).map { splitter.splitToList(it) }

        // 得分：研发项目数量
        inputItems.forEach {
            val entry = middle[it.id]!!

            // 研发项目数量
            val a = it.newModelCnt
            var r = a.toInt()
            if (r > 5) {
                r = 5
            }
            entry["newModelCnt_score"] = r.toString()

            // 品牌档次
            if (brandMap[it.brandCat] != null) {
                entry["brandCat_score"] = brandMap[it.brandCat].toString()
            } else {
                entry["brandCat_score"] = "0"
            }

            // 股权背景
            var idx = 0
            val total = holderInfo.size
            while (idx < total) {
                val cond = holderInfo[idx]
                if (it.holderCat == cond[0] || it.holderCat == cond[1]) {
                    break
                }

                idx += 1
            }
            r = total - idx
            entry["holderCat_score"] = r.toString()
        }
    }

    private fun calcWeightedScore(middle: MiddleResultT) {
        // 计算：加权得分
        val weights = mapOf(
            "salesVol_score" to 5,
            "incRate_score" to 4,
            "newModelCnt_score" to 3,
            "brandCat_score" to 2,
            "holderCat_score" to 2,
            "riskLevel_score" to 1
        )

        // 总体得分：把总得分映射到 0~1, 手段是 sin
        val goal = 5
        val ratio = (Math.PI / 2) / goal

        middle.forEach { t ->
            val entry = t.value
            var s = 0.0

            weights.forEach { w ->
                // 原始得分
                var a = getNumber(entry[w.key]!!).toDouble()
                // 得分通过 sin 映射到 0~1
                a = sin(a * ratio)

                // 权重
                val p = w.value

                // 欧式空间的各个维度长度的平方和
                val c = p * p * a * a
                s += c
            }

            // 开根号，就等于模长，和不开根号性质相同
            // s = sqrt(s)
            entry["weighted_score"] = s.toString()
        }
    }

    private fun calcWeightedCate(middle: MiddleResultT) {
        // 按四分位数：总共分 4 档，第 1 档是最好的
        val percentiles = listOf(0.25, 0.5, 0.75)

        // 综合得分排序，一定要按 number 排序，不能按 string 排序
        val s = middle.toList().toMutableList()
        s.sortByDescending { it.second["weighted_score"]!!.toDouble() }

        // 所有的数量
        val total = s.size + 1
        s.forEachIndexed { index, it ->
            val r = index + 1
            val entry = middle[it.first]!!
            entry["weighted_score_rank"] = r.toString()
            entry["weighted_score_score"] = rankScore(r, 5, 5).toString()

            // 按照 四分位数，对排名进行分类
            var cate = 4
            var i = 0
            while(i < percentiles.size) {
                if ( r < percentiles[i] * total) {
                    cate = i+1
                    break
                }
                i += 1
            }
            entry["weighted_score_cate"] = cate.toString()
            entry["weighted_score_cate_adj"] = cate.toString()
        }

        // 按业务类型，对最终的分类做调账
        adjustBatch(middle)
    }

    private fun adjustBatch(middle: MiddleResultT) {
        // 对分类做调整：如果在某一类中，最好的分类还是 3，则该分类中整体上调 1
        // 豪华车
        adjustCate(middle) { it -> it.second["brandCat_score"] == "5"  }
        // 合资车
        adjustCate(middle) { it -> it.second["brandCat_score"] == "4"  }
        // 国产车
        val domestic = listOf("0", "1", "2", "3")
        adjustCate(middle) { it ->
            domestic.contains(it.second["brandCat_score"]) || it.second["newPlayer"] == "否"
        }
        // 新能源
        adjustCate(middle) { it.second["newPlayer"] == "是" }
    }

    private fun adjustCate(inputScores: MiddleResultT, fn: (Pair<Int, HashMap<String, String>>) -> Boolean ) {
        // 按照不同类型的业务，如果最好的排名还是 3，则整体上调 1
        val a = inputScores.toList()
            .filter {
                fn(it)
            }
            .sortedBy { it.second["weighted_score_cate"]!!.toInt() }

        if (a.isEmpty()) {
            return
        }

        // 第一个就是排名最靠前的，因为上一步已经排过顺序了
        val first = a[0]
        if (first.second["weighted_score_cate"]!! >= "3") {
            a.forEach { it ->
                val k = it.first
                val entry = inputScores[k]!!
                val old = entry["weighted_score_cate"]!!.toInt()
                entry["weighted_score_cate_adj"] = (old - 1).toString()
            }
        }
    }
}
