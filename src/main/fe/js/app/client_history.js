layui.use(
    ["jquery", "layer", "form", "table", "element", "metis"],
    function () {
        const { $, form, table, layer, element, metis } = layui;

        const prefix = "/page/sales/client";

        const clientId = metis.getState("clientId");

        // 过往项目列表
        table.render({
            elem: "#projectTableId",
            url: `${prefix}/historyProject?clientId=${clientId}`,
            toolbar: "#topToolbar",
            defaultToolbar: ["filter", "exports", "print"],
            cols: [
                [
                    { type: "checkbox", width: 50, fixed: "left" },
                    {
                        field: "projectName",
                        width: 120,
                        title: "项目名称",
                        sort: true,
                    },
                    {
                        field: "location",
                        width: 150,
                        title: "项目地点",
                        sort: true,
                    },
                    {
                        field: "clientOwner",
                        width: 150,
                        title: "负责人",
                        sort: true,
                    },
                    {
                        field: "ownerTitle",
                        width: 150,
                        title: "级别",
                        sort: true,
                    },
                    {
                        field: "clientEmail",
                        width: 150,
                        title: "邮箱",
                        sort: true,
                    },
                    {
                        field: "status",
                        width: 150,
                        title: "状态",
                        sort: true,
                    },
                    {
                        field: "updateAt",
                        width: 150,
                        title: "更新日期",
                        sort: true,
                    },
                    {
                        title: "操作",
                        minWidth: 50,
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

        function projectAdd() {
            metis.openEntryForm(
                `${prefix}/oppEntry?id=0&clientId=${clientId}`,
                "新增"
            );
        }

        function projectEdit(data) {
            const url = `${prefix}/oppEntry?id=${data.id}`;
            metis.openEntryForm(url, "修改");
        }

        function projectDelete(data) {
            layer.confirm("真的删除当前记录吗？", function (index) {
                $.post(`${prefix}/oppDeleteBy?id=${data.id}`).then(function (
                    res
                ) {
                    layer.close(index);

                    table.reload("projectTableId");
                    table.reload("oppTableId");
                });
            });
        }

        metis.bindTableEvent("projectTableFilter", {
            add: projectAdd,
            edit: projectEdit,
            delete: projectDelete,
        });

        // 过往机会
        table.render({
            elem: "#oppTableId",
            url: `${prefix}/historyOpp?clientId=${clientId}`,
            toolbar: "#topToolbar",
            defaultToolbar: ["filter", "exports", "print"],
            cols: [
                [
                    { type: "checkbox", width: 50, fixed: "left" },
                    {
                        field: "projectName",
                        width: 120,
                        title: "机会名称",
                        sort: true,
                    },
                    {
                        field: "location",
                        width: 150,
                        title: "项目地点",
                        sort: true,
                    },
                    {
                        field: "clientOwner",
                        width: 150,
                        title: "负责人",
                        sort: true,
                    },
                    {
                        field: "ownerTitle",
                        width: 150,
                        title: "级别",
                        sort: true,
                    },
                    {
                        field: "followUp",
                        width: 150,
                        title: "跟进状态",
                        sort: true,
                    },
                    {
                        field: "proposalLead",
                        width: 150,
                        title: "跟进人",
                        sort: true,
                    },
                    {
                        field: "updateAt",
                        width: 150,
                        title: "更新日期",
                        sort: true,
                    },
                    {
                        title: "操作",
                        minWidth: 50,
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

        metis.bindTableEvent("oppTableFilter", {
            add: projectAdd,
            edit: projectEdit,
            delete: projectDelete,
        });
    }
);
