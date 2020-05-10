layui.use(
    ["jquery", "layer", "form", "table", "miniPage", "element", "metis"],
    function () {
        const { $, form, table, miniPage, layer, element, metis } = layui;

        const prefix = "/page/sales/client";

        // 监听上面的搜索条件
        form.on("submit(data-search-btn)", function (data) {
            const result = JSON.stringify(data.field);

            // 执行搜索重载, where.params 作为 querystring 出给后端
            table.reload("currentTableId", {
                page: {
                    curr: 1,
                },
                where: {
                    params: result,
                },
            });

            return false;
        });

        // 下面的 table 数据
        table.render({
            elem: "#currentTableId",
            url: `${prefix}/find`,
            toolbar: "#topToolbar",
            defaultToolbar: ["filter", "exports", "print"],
            cols: [
                [
                    { type: "checkbox", width: 50, fixed: "left" },
                    {
                        field: "clientName",
                        width: 120,
                        title: "客户名称",
                        sort: true,
                    },
                    {
                        field: "clientCode",
                        width: 150,
                        title: "客户代码",
                        sort: true,
                    },
                    {
                        field: "address",
                        width: 250,
                        title: "地址",
                        sort: true,
                    },
                    { field: "channelType", width: 150, title: "渠道类型" },
                    { field: "clientStatus", width: 120, title: "客户状态" },
                    {
                        title: "操作",
                        minWidth: 150,
                        toolbar: "#lineToolbar",
                        fixed: "right",
                        align: "center",
                    },
                ],
            ],
            page: {
                limit: 30,
                limits: [30, 100, 200, 500],
            },
        });

        // 打开 entry form，如果是新增，则空表格；如果是修改，则显示当前记录
        function openEntryForm(url, title) {
            miniPage.getHrefContentA(url).done(function (content) {
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
        }

        //  监听 table 上面的 toolbar 事件
        table.on("toolbar(currentTableFilter)", function (obj) {
            if (obj.event === "add") {
                // 监听添加操作
                openEntryForm(`${prefix}/entry?id=0`, "新增客户");
            } else if (obj.event === "delete") {
                // 监听删除操作
                const checkStatus = table.checkStatus("currentTableId"),
                    data = checkStatus.data;

                const ids = R.map((entry) => entry.id, data);

                if (ids.length === 0) {
                    layer.alert("未选中任何记录", {
                        title: "警告",
                    });
                    return;
                }

                layer.confirm("真的删除选中记录吗？", function (index) {
                    $.post(`${prefix}/deleteBatch`, {
                        param: JSON.stringify(ids),
                    }).then(function (res) {
                        if (res.state === "ok") {
                            table.reload("currentTableId");
                            // 关闭弹出层
                            layer.close(index);
                        } else {
                            const index = layer.alert(
                                "批量删除失败，请稍后重试",
                                {
                                    title: "操作失败",
                                },
                                function () {
                                    // 关闭弹出层
                                    layer.close(index);
                                }
                            );
                        }
                    });
                });
            }
        });

        // 监听 table 每一行开头的复选框选择
        table.on("checkbox(currentTableFilter)", function (obj) {
            // console.log(obj)
        });

        // 监听 table 每一行的按钮动作
        table.on("tool(currentTableFilter)", function (obj) {
            // console.log("current line: ", obj);

            const data = obj.data;
            if (obj.event === "edit") {
                const url = `${prefix}/entry?id=` + data.id;
                openEntryForm(url, "编辑客户");

                return false;
            } else if (obj.event === "delete") {
                layer.confirm("真的删除当前记录吗？", function (index) {
                    $.post(`${prefix}/deleteBy?id=` + data.id).then(function (
                        res
                    ) {
                        layer.close(index);

                        table.reload("currentTableId");
                    });
                });
            } else if (obj.event === "details") {
                // 设置全局变量
                metis.setState("clientId", data.id);
                console.log(data);

                // 仅改变 url，但是菜单没有高亮切换
                // miniPage.hashChange(`${prefix}/basicInfo?clientId=${data.id}`);

                // 模拟点击菜单
                const m = $("a#basicInfoMenuId");
                m.attr(
                    "layuimini-href",
                    `page/sales/client/basicInfo?clientId=${data.id}`
                );

                m.trigger("click");

                // 过往历史、联系人，都依赖于一个客户，所以这里把客户id 作为 querystring
                $("a#clientHistoryMenuId").attr(
                    "layuimini-href",
                    `page/sales/client/clientHistory?clientId=${data.id}`
                );

                $("a#clientContactMenuId").attr(
                    "layuimini-href",
                    `page/sales/client/clientContact?clientId=${data.id}`
                );
            }
        });
    }
);
