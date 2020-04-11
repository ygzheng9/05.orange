layui.use(
    ["jquery", "layer", "form", "table", "element", "metis"],
    function () {
        const { $, form, table, layer, element, metis } = layui;

        const prefix = "/page/sales/client";

        const clientId = metis.getState("clientId");
        // console.log("clientId: ", clientId);

        // 下面的 table 数据
        table.render({
            elem: "#currentTableId",
            url: `${prefix}/callFind?clientId=${clientId}`,
            toolbar: "#topToolbar",
            defaultToolbar: ["filter", "exports", "print"],
            cols: [
                [
                    {
                        field: "reportName",
                        width: 150,
                        title: "报告名称",
                        sort: true,
                    },
                    {
                        field: "callDate",
                        width: 150,
                        title: "拜访日期",
                        sort: true,
                    },
                    { field: "callAddress", width: 150, title: "地址" },
                    { field: "callHost", width: 120, title: "客户" },
                    { field: "hostTitle", width: 120, title: "职位" },
                    { field: "hostPhone", width: 120, title: "电话" },
                    { field: "salesName", width: 120, title: "业务员" },
                    { field: "salesTitle", width: 120, title: "职位" },
                    { field: "callType", width: 120, title: "拜访类型" },
                    { field: "remark", width: 120, title: "备注" },

                    {
                        title: "操作",
                        minWidth: 120,
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

        //  监听 table 上面的 toolbar 事件
        table.on("toolbar(currentTableFilter)", function (obj) {
            if (obj.event === "add") {
                // 监听添加操作
                metis.openEntryForm(
                    `${prefix}/callEntry?id=0&clientId=${clientId}`,
                    "添加记录"
                );
            }
        });

        // 监听 table 每一行的按钮动作
        table.on("tool(currentTableFilter)", function (obj) {
            // console.log("current line: ", obj);

            const data = obj.data;
            if (obj.event === "edit") {
                const url = `${prefix}/callEntry?id=${data.id}`;
                metis.openEntryForm(url, "编辑拜访记录");

                return false;
            } else if (obj.event === "delete") {
                layer.confirm("真的删除当前记录吗？", function (index) {
                    $.post(`${prefix}/callDeleteBy?id=${data.id}`).then(
                        function (res) {
                            layer.close(index);

                            table.reload("currentTableId");
                        }
                    );
                });
            }
        });

        $("#potentialEntry").on("click", function (evt) {
            evt.preventDefault();

            metis.openEntryForm(
                `${prefix}/potentialEntry?clientId=${clientId}`,
                "客户潜力评估"
            );
        });

        // 加载 potential 页面
        function initLoad() {
            $("#potentialShowId").load(
                `${prefix}/potentialShow?clientId=${clientId}`
            );
        }

        initLoad();
    }
);
