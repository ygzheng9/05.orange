layui.use(
    ["form", "layer", "table", "jquery", "laydate", "metis"],
    function () {
        const { form, layer, table, $, laydate, metis } = layui;

        const action = "/companyCreateOrUpdate";
        const tableId = "currentTableId";

        function beforeSave(field) {
            console.log(field);

            return true;
        }

        metis.bindFormSave({ action, tableId, beforeSave });
    }
);
