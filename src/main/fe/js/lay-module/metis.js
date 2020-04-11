/**
 * date:2020/04/06
 * author:zhengyg
 * version:1.0
 * description: metis 辅助函数
 */

layui.define(["jquery", "miniPage", "layer", "table"], function (exports) {
    const { $, miniPage, layer, table } = layui;

    const state = {
        clientid: 0,
    };

    const metis = {
        setState: function (k, v) {
            state[k] = v;
        },

        getState: function (k) {
            return state[k];
        },

        getCheckBoxValues: function (name) {
            //获取checkbox[name='aname']的值
            const arr = [];
            $(`input:checkbox[name='${name}']:checked`).each(function (i) {
                arr.push($(this).val());
            });

            //将数组合并成字符串
            return arr.join(" ");
        },

        // 2020-04-11: 基于 mimiPage 同名函数改写，返回 promise
        getHrefContentA: function (href) {
            const v = new Date().getTime();

            return $.get(
                href.indexOf("?") > -1 ? href + "&v=" + v : href + "?v=" + v
            ).fail(function (xhr, textstatus, thrown) {
                return layer.msg(
                    "Status:" +
                        xhr.status +
                        "，" +
                        xhr.statusText +
                        "，请稍后再试！"
                );
            });
        },

        // 在弹出层打开 entry form，如果是新增，则空表格；如果是修改，则显示当前记录
        openEntryForm: function (url, title) {
            this.getHrefContentA(url).done(function (content) {
                const openWH = miniPage.getOpenWidthHeight();

                const index = layer.open({
                    title: title,
                    type: 1,
                    shade: 0.2,
                    maxmin: true,
                    shadeClose: true,
                    area: [openWH[0] + "px", openWH[1] + "px"],
                    offset: [openWH[2] + "px", openWH[3] + "px"],
                    content: content,
                });
                $(window).on("resize", function () {
                    layer.full(index);
                });
            });
        },

        // 2020/04/11: 绑定 table 的事件，包括顶部的 toolbar 和每一行的 toolbar
        bindTableEvent: function (filter, handlerMap) {
            // 从 map 中找到匹配的 event，并调用
            function fireEvent(obj) {
                const { event, data } = obj;
                const fn = handlerMap[event];

                // console.log(obj);

                if (fn === undefined && fn === null) {
                    console.log(`Error: ${event} not defined.`);
                    return;
                }

                fn(data);
            }

            // 顶部 toolbar
            table.on(`toolbar(${filter})`, function (obj) {
                fireEvent(obj);
            });

            // 每一行的 toolbar
            table.on(`tool(${filter})`, function (obj) {
                fireEvent(obj);
            });
        },
    };

    exports("metis", metis);
});
