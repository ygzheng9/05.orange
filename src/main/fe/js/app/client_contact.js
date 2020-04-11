layui.use(
    ["jquery", "layer", "form", "table", "element", "metis"],
    function () {
        const { $, form, table, layer, element, metis } = layui;

        const prefix = "/page/sales/client";

        const clientId = metis.getState("clientId");

        // 过往项目列表
        table.render({
            elem: "#contactTableId",
            url: `${prefix}/contactList?clientId=${clientId}`,
            toolbar: "#topToolbar",
            defaultToolbar: ["filter", "exports", "print"],
            cols: [
                [
                    { type: "checkbox", width: 50, fixed: "left" },
                    {
                        field: "personName",
                        width: 120,
                        title: "名称",
                        sort: true,
                    },
                    {
                        field: "department",
                        width: 100,
                        title: " 部门",
                        sort: true,
                    },
                    {
                        field: "title",
                        width: 100,
                        title: "头衔",
                        sort: true,
                    },
                    {
                        field: "phone",
                        width: 150,
                        title: "电话",
                        sort: true,
                    },
                    {
                        field: "email",
                        width: 150,
                        title: "邮箱",
                        sort: true,
                    },
                    {
                        field: "focus",
                        width: 150,
                        title: "关注点",
                        sort: true,
                    },
                    {
                        field: "currentVote",
                        width: 100,
                        title: "当前态度",
                        sort: true,
                    },
                    {
                        field: "targetVote",
                        width: 100,
                        title: "目标态度",
                        sort: true,
                    },
                    {
                        field: "coveredBy",
                        width: 150,
                        title: "负责人",
                        sort: true,
                    },
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

        function actionAdd() {
            metis.openEntryForm(
                `${prefix}/contactEntry?id=0&clientId=${clientId}`,
                "新增"
            );
        }

        function actionEdit(data) {
            const url = `${prefix}/contactEntry?id=${data.id}`;
            metis.openEntryForm(url, "修改");
        }

        function actionDelete(data) {
            layer.confirm("真的删除当前记录吗？", function (index) {
                $.post(`${prefix}/contactDeleteBy?id=${data.id}`).then(
                    function (res) {
                        layer.close(index);

                        table.reload("contactTableId");
                    }
                );
            });
        }

        metis.bindTableEvent("contactTableFilter", {
            add: actionAdd,
            edit: actionEdit,
            delete: actionDelete,
        });
    }
);
