layui.use(["form", "laydate"], function () {
    const { form, laydate } = layui;

    laydate.render({
        elem: "#bizDate",
        value: $("#id_bizDate").html(),
    });

    // 顶部查询
    form.on("submit(btnSearch)", function (data) {
        // console.log(data.field);

        const { bizDate, bizUnit } = data.field;
        const num = $("#id_num").html();
        const user = $("#id_user").html();

        window.location = `/survey/matrixOpen?num=${num}&id=${user}&d=${bizDate}&u=${bizUnit}`;

        return false;
    });

    //监听提交
    form.on("submit(formDemo)", function (data) {
        const search = form.val("searchForm");

        const param = {
            num: $("#id_num").html(),
            user: $("#id_user").html(),
            answers: JSON.stringify(data.field),
            ...search,
        };

        console.log(param);

        $.post("/survey/matrixSubmit", param).then(function (res) {
            console.log(res);

            const index = layer.msg(
                "提交成功",
                {
                    time: 300,
                },
                function () {
                    // 关闭弹出层
                    layer.close(index);
                }
            );
        });

        return false;
    });
});
