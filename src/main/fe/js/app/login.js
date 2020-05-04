layui.use(["form", "layer", "jquery"], function () {
    const { form, layer, $ } = layui;

    // 登录过期的时候，跳出ifram框架
    if (top.location != self.location) top.location = self.location;

    // 粒子线条背景
    $(document).ready(function () {
        $(".layui-container").particleground({
            dotColor: "#7ec7fd",
            lineColor: "#7ec7fd",
        });
    });

    // 进行登录操作
    form.on("submit(login)", function (data) {
        const { email, password, captcha } = data.field;

        if (email === "") {
            layer.msg("邮箱不能为空");
            return false;
        }
        if (password === "") {
            layer.msg("密码不能为空");
            return false;
        }
        if (captcha === "") {
            layer.msg("验证码不能为空");
            return false;
        }

        function errHandler(res) {
            // 服务没有返回，也即：服务故障
            const index = layer.alert(
                "登录失败，请检查用户名和密码",
                {
                    title: "操作失败",
                },
                function () {
                    // 关闭弹出层
                    layer.close(index);
                    console.log("服务故障: ", res);
                }
            );
        }

        function okHandler(res) {
            // 服务有返回码，返回码可以是 ok，fail
            if (res.state === "ok") {
                const index = layer.msg(
                    "登录成功",
                    {
                        time: 300,
                    },
                    function () {
                        // 关闭弹出层
                        layer.close(index);

                        window.location = "/";
                    }
                );
            } else {
                const index = layer.alert(
                    "用户名和密码不匹配",
                    {
                        title: "登录失败",
                    },
                    function () {
                        // 关闭弹出层
                        layer.close(index);
                        console.log("用户名和密码不匹配：", res);
                    }
                );
            }
        }

        $.post("/doLogin", {
            param: JSON.stringify(data.field),
        }).then(okHandler, errHandler);

        return false;
    });
});
