package com.metis.survey

import com.jfinal.aop.Clear
import com.jfinal.aop.Inject
import com.jfinal.core.Controller
import com.jfinal.kit.Ret
import com.metis.index.AuthInterceptor
import com.metis.index.LoginInterceptor

// 从文件读取问卷
// 提交问卷，保存问卷答案
// 再次打开时，显示上次提交的信息

@Clear(LoginInterceptor::class, AuthInterceptor::class)
class SurveryController : Controller() {
    @Inject
    lateinit var surveySvc: SurveyService;

    // 统计问卷
    fun open() {
        // /survey/open?num=S01&id=123
        // 从 querystring 中获取 问卷编码，答卷人
        val num = get("num", "")
        val id = get("id", "")

        // 加载问卷
        val s = surveySvc.loadSurvey(num)

        // 读取之前提交的答案
        val ans2 = surveySvc.loadAnswers(num, id)
        if (ans2 != null) {
            surveySvc.fillAnswers(s, ans2)
        }

        set("num", num)
        set("id", id)
        set("title", s.title)
        set("sections", s.sections)

        render("survey_open.html")
    }

    fun submit() {
        val num = get("num", "")
        val user = get("user", "")
        val answers = get("answers", "")

        surveySvc.saveAnswers(num, user, answers)

        renderJson(Ret.ok())
    }

    // 指标填报
    fun matrixOpen() {
        // survey/matrixOpen?num=A02&id=124
        val num = get("num", "")
        val bizDate = get("d", "2020-05-09")
        val bizUnit = get("u", "-1")

        val lines = surveySvc.matrixOpen(num)
        val ans = surveySvc.matrixLoadAnswers(num, bizDate, bizUnit)
        surveySvc.matrixFillAnswers(lines, ans)

        val title = if (lines.isNotEmpty()) lines[0].code else ""
        val remains = lines.filter { it.type !== "LEVEL_1"  }

        set("title", title)
        set("lines", remains)
        set("d", bizDate)
        set("u", bizUnit)

        keepPara()

        val options = listOf(
            SelectOption("1", "北京"),
            SelectOption("2", "上海"),
            SelectOption("3", "广州"),
            SelectOption("4", "深圳"),
            SelectOption("5", "天津"),
            SelectOption("6", "重庆")
        )
        set("options", options)


        render("matrix_open.html")
    }

    fun matrixSubmit() {
        val bizDate = get("bizDate", "")
        val num = get("num", "")
        val bizUnit = get("bizUnit", "")
        val user = get("user", "")
        val answers = get("answers", "")

        surveySvc.matrixSave(num, bizDate, bizUnit, user, answers)

        renderJson(Ret.ok())
    }
}
