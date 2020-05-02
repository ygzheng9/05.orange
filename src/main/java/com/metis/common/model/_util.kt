package com.metis.common.model

import cn.hutool.core.util.StrUtil
import kotlin.random.Random


// 从 list 中随机返回一个值
fun <T> getRandom(list: List<T>): T {
    val i = Random.nextInt(0, list.size)
    return list[i]
}

// 编码前面补零
fun padPre(i: Int, l: Int) : String {
    return StrUtil.padPre("$i", l, "0")
}
