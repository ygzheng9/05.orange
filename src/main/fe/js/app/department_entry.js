layui.use(
    ["form", "layer", "table", "jquery", "laydate", "tableSelect", "metis"],
    function () {
        const { form, layer, table, $, laydate, tableSelect, metis } = layui;

        const action = "/departmentCreateOrUpdate";
        const tableId = "currentTableId";

        const formId = "#id_department_entry";

        function beforeSave(field) {
            console.log(field);

            return true;
        }

        metis.bindFormSave({ action, tableId, beforeSave });

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
    }
);
