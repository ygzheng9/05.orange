layui.use(
    ["form", "layer", "table", "jquery", "laydate", "metis"],
    function () {
        const { form, layer, table, $, laydate, metis } = layui;

        const action = "/page/sales/lead/createOrUpdate";
        const tableId = "currentTableId";

        function beforeSave(field) {
            console.log(field);

            return true;
        }

        metis.bindFormSave({ action, tableId, beforeSave });

        laydate.render({
            elem: "#id_targetDate", //指定元素
        });
    }
);
