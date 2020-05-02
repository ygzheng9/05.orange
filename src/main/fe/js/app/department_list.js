layui.use(
    ["jquery", "layer", "form", "table", "element", "tableSelect", "metis"],
    function () {
        const { $, form, table, layer, element, tableSelect, metis } = layui;

        const prefix = "";
        const tableId = "currentTableId";
        const tableFilter = "currentTableFilter";

        const formId = "#id_department_cond";

        // 监听上面的搜索条件
        form.on("submit(data-search-btn)", function (data) {
            const field = data.field;

            if (metis.isEmpty(field.companyName)) {
                field.companyId = "";
            }

            if (metis.isEmpty(field.headName)) {
                field.headId = "";
            }

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

        // 用户选择的下拉 table
        tableSelect.render({
            elem: `${formId} .headName`, //定义输入框input对象 必填
            checkedKey: "id", //表格的唯一建值，非常重要，影响到选中状态 必填
            searchKey: "keyword", //搜索输入框的name值 默认keyword
            searchPlaceholder: "关键词搜索", //搜索输入框的提示文字 默认关键词搜索
            table: {
                //定义表格参数，与LAYUI的TABLE模块一致，只是无需再定义表格elem
                url: "/userFind",
                cols: [
                    [
                        { type: "radio", width: 50, fixed: "left" },
                        {
                            field: "userName",
                            width: 120,
                            title: "用户名",
                            sort: true,
                        },
                        {
                            field: "email",
                            width: 200,
                            title: "邮箱",
                            sort: true,
                        },
                    ],
                ],
            },
            done: function (elem, data) {
                //选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
                //拿到data[]后 就按照业务需求做想做的事情啦~比如加个隐藏域放ID...
                // console.log(data.data);

                const selected = data.data[0];
                $(`${formId} .headId`).val(selected.id);

                elem.val(selected.userName);
            },
        });

        // 公司选择的下拉 table
        tableSelect.render({
            elem: `${formId} .companyName`, //定义输入框input对象 必填
            checkedKey: "id", //表格的唯一建值，非常重要，影响到选中状态 必填
            searchKey: "keyword", //搜索输入框的name值 默认keyword
            searchPlaceholder: "关键词搜索", //搜索输入框的提示文字 默认关键词搜索
            table: {
                //定义表格参数，与LAYUI的TABLE模块一致，只是无需再定义表格elem
                url: "/companyFind",
                cols: [
                    [
                        { type: "radio", width: 50, fixed: "left" },
                        {
                            field: "companyCode",
                            width: 100,
                            title: "公司代码",
                            sort: true,
                        },
                        {
                            field: "companyName",
                            width: 200,
                            title: "公司名称",
                            sort: true,
                        },
                    ],
                ],
            },
            done: function (elem, data) {
                //选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
                //拿到data[]后 就按照业务需求做想做的事情啦~比如加个隐藏域放ID...
                // console.log(data.data);

                const selected = data.data[0];
                $(`${formId} .companyId`).val(selected.id);

                elem.val(selected.companyName);
            },
        });

        // 下面的 table 数据
        table.render({
            elem: `#${tableId}`,
            url: `${prefix}/departmentFind`,
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
                        field: "departmentName",
                        width: 150,
                        title: "部门名称",
                        sort: true,
                    },
                    { field: "departmentCode", width: 150, title: "部门代码" },
                    { field: "userName", width: 150, title: "负责人" },

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
            metis.openEntryForm(`${prefix}/departmentEntry?id=0`, "新增");
        }

        function actionEdit(data) {
            const url = `${prefix}/departmentEntry?id=` + data.id;
            metis.openEntryForm(url, "修改");
        }

        function actionDelete(data) {
            layer.confirm("真的删除当前记录吗？", function (index) {
                $.post(`${prefix}/departmentDelete?id=${data.id}`).then(
                    function (res) {
                        layer.close(index);

                        table.reload(tableId);
                    }
                );
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
                $.post(`${prefix}/departmentDeleteBatch`, {
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
