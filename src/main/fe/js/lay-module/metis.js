/**
 * date:2020/04/06
 * author:zhengyg
 * version:1.0
 * description: metis 辅助函数
 */

layui.define(["jquery", "miniPage", "layer", "table", "form"], function (
    exports
) {
    const { $, miniPage, layer, table, form } = layui;

    // 全局共享数据
    const state = {
        // 当前选中的 客户 id
        clientid: 0,

        // 当前登录用所具有的权限，在 index.js 中被初始化；
        loginRoles: [],
    };

    const metis = {
        setState: function (k, v) {
            state[k] = v;
        },

        getState: function (k) {
            return state[k];
        },

        // 20200505 zhengyg 增加权限判定
        setLoginRoles: function (str) {
            this.setState("loginRoles", this.toList(str));
        },

        // 把以空格分割的字符串，拆分成 list
        toList: function (str) {
            const list = [];

            if (str === undefined || str.length === 0) {
                return list;
            }

            const l = str.split(" ");
            l.forEach(function (i) {
                const v = i.trim();
                if (v.length > 0) {
                    list.push(v);
                }
            });

            return list;
        },

        // 两个 list 中是否有相同元素
        hasCommon: function (list1, list2) {
            if (
                list1 === undefined ||
                list2 === undefined ||
                list1.length === 0 ||
                list2.length === 0
            ) {
                return false;
            }

            for (var i = 0; i < list1.length; i++) {
                for (var j = 0; j < list2.length; j++) {
                    if (list1[i] === list2[j]) {
                        return true;
                    }
                }
            }

            return false;
        },

        // 在 miniMenu 的 each 中使用；
        // auths 是 每个菜单项所要求的 权限
        checkPermission: function (perms) {
            // 没有定义权限，默认都可以访问
            if (perms === undefined || perms === null || perms.length === 0) {
                return true;
            }

            // 从全局中取得数据；
            const { loginRoles } = state;

            // 定义了所需的权限，但是当前用户没有任何权限，则禁止访问
            if (loginRoles.length === 0) {
                return false;
            }

            // 约定都是 空格 分割
            const required = this.toList(perms);
            // console.log("required: ", required);
            return this.hasCommon(required, loginRoles);
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

        // 2020/05/01: 单一表单的新增和修改
        bindFormSave: function (options) {
            // tableId: 是 form 关闭后，需要刷新的 table
            // beforeSave: 保存前的校验，返回 true 则继续保存，false 则终止；
            const { action, tableId, beforeSave } = options;

            // 当前弹出层，防止ID被覆盖
            const parentIndex = layer.index;

            function errHandler(res) {
                // 服务没有返回，也即：服务故障
                const index = layer.alert(
                    "保存失败，请检查数据格式",
                    {
                        title: "操作失败",
                    },
                    function () {
                        // 关闭弹出层
                        layer.close(index);
                    }
                );
            }

            function okHandler(res) {
                // 服务有返回码，返回码可以是 ok，fail
                if (res.state === "ok") {
                    const index = layer.msg(
                        "保存成功",
                        {
                            time: 300,
                        },
                        function () {
                            table.reload(tableId);
                            // 关闭弹出层
                            layer.close(index);
                            layer.close(parentIndex);
                        }
                    );
                } else {
                    const index = layer.alert(
                        "保存失败，请稍后重试",
                        {
                            title: "操作失败",
                        },
                        function () {
                            // 关闭弹出层
                            layer.close(index);
                        }
                    );
                }
            }

            function trimAll(field) {
                // 去除所有空格
                for (var key in field) {
                    var item = field[key];
                    field[key] =
                        typeof item.trim === "function" ? item.trim() : item;
                }

                return field;
            }

            /**
             * 初始化表单，要加上，不然刷新部分组件可能会不加载
             */
            form.render();
            form.on("submit(saveBtn)", function (data) {
                // 去除所有空格
                const field = trimAll(data.field);

                if (beforeSave !== undefined) {
                    if (!beforeSave(field)) {
                        console.log("beforeSave failed.");
                        return false;
                    }
                }

                $.post(action, {
                    param: JSON.stringify(field),
                }).then(okHandler, errHandler);

                return false;
            });
        },

        // 2020/04/11: 绑定 table 的事件，包括顶部的 toolbar 和每一行的 toolbar
        bindTableEvent: function (filter, handlerMap) {
            // 从 map 中找到匹配的 event，并调用
            function fireEvent(obj) {
                const { event, data } = obj;
                const fn = handlerMap[event];

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

        isEmpty: function (obj) {
            if (typeof obj === "undefined" || obj === null || obj === "") {
                return true;
            } else {
                return false;
            }
        },

        selectFillOptions: function (domid, items, value, label) {
            // [{value: "A", label: "上海"}]
            // 删除原有选项
            $(domid).empty();

            // 增加一个空白项
            var first = `请选择`;
            if (label != undefined) {
                first = label;
            }
            $(domid).append(`<option value=''>${first}</option>`);

            // 遍历 items，生成新的 options
            $.each(items, function (k, item) {
                var selected = "";
                if (value !== undefined && value === item.value) {
                    selected = "selected";
                }

                const option = `<option value=${item.value} ${selected}>${item.label}</option>`;
                $(domid).append(option);
            });

            // 刷新，使得  layui select 正常显示
            form.render("select");
        },
    };

    exports("metis", metis);
});
