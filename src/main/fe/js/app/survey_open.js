layui.use("form", function () {
    const { form } = layui;

    //监听提交
    form.on("submit(formDemo)", function (data) {
        console.log(data.field);

        const param = {
            num: $("#id_num").html(),
            user: $("#id_user").html(),
            answers: JSON.stringify(data.field),
        };

        $.post("/survey/submit", param).then(function (res) {
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

    form.verify({
        mustradio: function (value, item) {
            //单选按钮必选
            const va = $(item).find("input[type='radio']:checked").val();
            if (typeof va == "undefined") {
                return $(item).attr("lay-verify-msg");
            }
        },
        mustcheck: function (value, item) {
            //复选框必选
            const xname = $(item).attr("id");
            const va = $(item).find("input[type='checkbox']:checked").val();
            if (typeof va == "undefined") {
                return $(item).attr("lay-verify-msg");
            }
        },
    });
});
