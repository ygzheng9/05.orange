layui.use(["jquery", "layer", "miniAdmin"], function() {
        const {$, layer, miniAdmin} = layui;

        // console.log('jquery version: ', $.fn.jquery);
        // console.log('echart version: ', echarts.version);

        const options = {
            iniUrl: "assets/api/init.json", // 初始化接口
            clearUrl: "assets/api/clear.json", // 缓存清理接口
            renderPageVersion: true, // 初始化页面是否加版本号
            bgColorDefault: 0, // 主题默认配置
            multiModule: true, // 是否开启多模块
            menuChildOpen: false, // 是否默认展开菜单
            loadingTime: 0, // 初始化加载时间
            pageAnim: true, // 切换菜单动画
        };
        miniAdmin.render(options);

        $(".login-out").on("click", function() {
            layer.msg("退出登录成功", function() {
                window.location = "/page/login-1.html";
            });
        });
    }
);
