package com.metis.survey

import cn.hutool.core.util.StrUtil
import cn.hutool.poi.excel.ExcelReader
import com.jfinal.kit.JsonKit
import com.jfinal.kit.Kv
import com.jfinal.kit.PropKit
import com.jfinal.plugin.activerecord.Db
import com.metis.common.model.ZTSurvey
import com.metis.common.model.ZTSurveyItem
import com.metis.common.model.ZTSurveyMatrix
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


open class SurveyService {
    private val surveyItemDao = ZTSurveyItem().dao()
    private val matrixDao = ZTSurveyMatrix().dao()

    fun loadSurvey(num: String): Survey {
        // 从文件中加载 问卷
        val fileName = "${PropKit.get("dataDir")}/survey/${num}.yaml"
        val steam = File(fileName).inputStream()

        val yaml = Yaml()
        val ret = yaml.loadAs(steam, Survey::class.java)

        ret.splitQuestions()

        return ret
    }

    fun loadAnswers(surveyNum: String, user: String) : List<ZTSurveyItem>? {
        return surveyItemDao.find("select * from z_t_survey_item where surveyNum = ? and submitter = ?", surveyNum, user)
    }

    fun fillAnswers(survey: Survey, answers: List<ZTSurveyItem>) {
        // 把数据库中的答案，写到问卷中
        for(section in survey.sections) {
            for(question in section.questions) {
                for(ans in answers) {
                    if (question.seq == ans.questionSeq) {
                        question.answer = ans.answer
                        break
                    }
                }
            }
        }
    }

    fun saveAnswers(surveyNum: String, user: String, answers: String) {
        // 格式化时间
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        // 输出已经格式化的现在时间（24小时制）
        val now = sdf.format(Date())

        // 删除之前提交的信息
        Db.delete("delete from z_t_survey_item where surveyNum = ? and submitter = ?", surveyNum, user);
        Db.delete("delete from z_t_survey where surveyNum = ? and submitter = ?", surveyNum, user);

        // 保存本次的结果
        ZTSurvey().set("surveyNum", surveyNum)
            .set("submitter", user)
            .set("byDate", now)
            .set("answers", answers)
            .save()

        val kv = JsonKit.parse(answers, Kv::class.java)
        kv.forEach {
            ZTSurveyItem().set("surveyNum", surveyNum)
                .set("submitter", user)
                .set("byDate", now)
                .set("questionSeq", it.key)
                .set("answer", it.value)
                .save()
        }
    }

    fun matrixOpen(num: String) : List<LineItem> {
        // 从文件中加载 问卷
        val fileName = "${PropKit.get("dataDir")}/survey/matrix/${num}.xlsx"
        val reader = ExcelReader(fileName, 0)

        val result = ArrayList<LineItem>()

        // 从 0 开始，是 excel 的第一行；
        // 这里是 1，也即：从第二行开始，因为第一行是标题行
        var idx = 1
        while (true) {
            val line = reader.readRow(idx)
            val entry = LineItem()

            // 空行，或者第一列为空白 ==》 文件末尾
            if (line.size == 0 || StrUtil.isBlank(line[0] as String)) {
                break
            }

            entry.code = line[0] as String

            if (idx == 1) {
                // 第一行是总标题
                entry.type = "LEVEL_1"
            } else if (line.size == 1 || StrUtil.isBlank(line[1] as String)) {
                // 只有第一列，或者第二列为空白
                entry.type = "LEVEL_2"
            } else {
                entry.type = "LEVEL_3"

                entry.name = line[1] as String
                entry.unit = line[3] as String
                entry.definition = line[4] as String
            }

            result.add(entry)

            idx += 1
        }

        return result
    }

    fun matrixSave(num: String, bizDate: String, bizUnit: String, user:String, answers : String) {
        // 格式化时间
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        // 输出已经格式化的现在时间（24小时制）
        val now = sdf.format(Date())

        // 约定好的后缀
        val suffix = "_comment"

        val kv = JsonKit.parse(answers, Kv::class.java)
        val values = Kv()
        val comments = Kv()
        kv.forEach {
            val k = it.key as String
            val v = it.value as String

            if (StrUtil.endWith(k, suffix, true)) {
                comments.set(StrUtil.removeSuffix(k, suffix), v)
            } else {
                values.set(k, BigDecimal(v))
            }
        }

        // 删除之前提交的信息
        Db.delete("delete from z_t_survey_matrix where num = ? and bizDate = ? and  bizUnit = ?", num, bizDate, bizUnit);

        // 保存新信息
        values.forEach {
            val k = it.key as String
            val v = it.value as BigDecimal
            val c = comments[it.key]
            ZTSurveyMatrix().set("num", num).set("indexCode", k)
                .set("bizDate", bizDate).set("bizUnit", bizUnit)
                .set("user", user)
                .set("bizValue", v)
                .set("comment", c)
                .set("updateDate", now)
                .save()
        }
    }

    fun matrixLoadAnswers(num:String, bizDate: String, bizUnit: String): List<ZTSurveyMatrix> {
        return matrixDao.find("select * from z_t_survey_matrix where num = ? and bizDate = ? and  bizUnit = ?", num, bizDate, bizUnit)
    }

    fun matrixFillAnswers(lines: List<LineItem>?, ans : List<ZTSurveyMatrix>?) {
        // 填充上次保存的结果
        if (lines == null || ans == null) {
            return
        }

        lines.forEach {
            if (it.type == "LEVEL_3") {
                val k = it.code
                val a = ans.find { i -> i.indexCode == k }
                if (a != null) {
                    it.value = a.bizValue
                    it.comment = a.comment
                }
            }
        }
    }
}
