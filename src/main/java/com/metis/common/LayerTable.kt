package com.metis.common;


import com.jfinal.plugin.activerecord.Page;

/**
 * @author zhengyg
 * @param <T> 分页查询结果 Page<User>
 * 保持和 layUI table 所需的格式，格式参见：https://www.layui.com/doc/modules/table.html#async
 * {
 *   "code": 0,
 *   "msg": "",
 *   "count": 1000,
 *   "data": [{}, {}]
 * }
 */


data class LayerTable<T>(val code: Int, val msg: String, val count: Int, val data: List<T>)


fun <T> buildLayuiTable(pages:Page<T>) : LayerTable<T>  {
    return LayerTable<T>(
        0,
        "OK",
        pages.totalRow,
        pages.list
    )
}

fun <T> buildLayuiTable(items: List<T>) : LayerTable<T>  {
    return LayerTable<T>(
        0,
        "OK",
        items.size,
        items
    )
}
