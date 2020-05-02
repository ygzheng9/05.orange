layui.use(
    ["jquery", "layer", "form", "table", "element", "metis"],
    function () {
        const { $, form, table, layer, element, metis } = layui;

        const prefix = "";
        const tableId = "currentTableId";
        const tableFilter = "currentTableFilter";

        // 监听上面的搜索条件
        form.on("submit(data-search-btn)", function (data) {
            // 查询条件输入的内容
            const result = JSON.stringify(data.field);

            // 执行搜索重载, where.params 作为 querystring 出给后端
            table.reload(tableId, {
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
            elem: `#${tableId}`,
            url: `${prefix}/companyFind`,
            toolbar: "#topToolbar",
            defaultToolbar: ["filter", "exports", "print"],
            cols: [
                [
                    { type: "checkbox", width: 50, fixed: "left" },
                    {
                        field: "companyName",
                        width: 120,
                        title: "公司名称",
                        sort: true,
                    },
                    {
                        field: "companyCode",
                        width: 150,
                        title: "公司代码",
                        sort: true,
                    },
                    {
                        field: "address",
                        width: 250,
                        title: "地址",
                        sort: true,
                    },
                    { field: "bizUnit", width: 150, title: "板块" },
                    { field: "remark", width: 200, title: "备注" },
                    {
                        title: "操作",
                        minWidth: 200,
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

        function actionAdd() {
            metis.openEntryForm(`${prefix}/companyEntry?id=0`, "新增");
        }

        function actionEdit(data) {
            const url = `${prefix}/companyEntry?id=` + data.id;
            metis.openEntryForm(url, "修改");
        }

        function actionDelete(data) {
            layer.confirm("真的删除当前记录吗？", function (index) {
                $.post(`${prefix}/companyDelete?id=${data.id}`).then(function (
                    res
                ) {
                    layer.close(index);

                    table.reload(tableId);
                });
            });
        }

        function actionDeleteBatch() {
            // 检查批量选中的记录
            const checkStatus = table.checkStatus(tableId);
            const data = checkStatus.data;

            const ids = R.map((entry) => entry.id, data);

            if (ids.length === 0) {
                layer.alert("未选中任何记录", {
                    title: "警告",
                });
                return;
            }

            layer.confirm("真的删除选中记录吗？", function (index) {
                $.post(`${prefix}/companyDeleteBatch`, {
                    param: JSON.stringify(ids),
                }).then(function (res) {
                    if (res.state === "ok") {
                        table.reload(tableId);
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

        metis.bindTableEvent(tableFilter, {
            add: actionAdd,
            edit: actionEdit,
            delete: actionDelete,
            deleteBatch: actionDeleteBatch,
        });

        // 监听 table 每一行开头的复选框选择
        table.on(`checkbox(${tableFilter})`, function (obj) {
            // console.log(obj)
        });
    }
);
