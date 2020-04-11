package com.metis.common


import com.metis.common.model._MappingKit
import com.metis.index.IndexController
import com.metis.sales.ClientController
import com.jfinal.config.*
import com.jfinal.ext.handler.ContextPathHandler
import com.jfinal.json.MixedJsonFactory
import com.jfinal.kit.Prop
import com.jfinal.kit.PropKit
import com.jfinal.plugin.activerecord.ActiveRecordPlugin
import com.jfinal.plugin.druid.DruidPlugin
import com.jfinal.server.undertow.UndertowServer
import com.jfinal.template.Engine

class StartConfig : JFinalConfig() {
    override fun configConstant(me: Constants) {
        loadConfig()

        me.devMode = p!!.getBoolean("devMode", false)
        /**
         * 支持 Controller、Interceptor、Validator 之中使用 @Inject 注入业务层，并且自动实现 AOP
         * 注入动作支持任意深度并自动处理循环注入
         */
        me.injectDependency = true
        // 配置对超类中的属性进行注入
        me.injectSuperClass = true

        me.setJsonFactory(MixedJsonFactory())

        /// 设置 devMode 之下的 action report 是否在 invocation 之后，默认值为 true
        me.setReportAfterInvocation(false)
    }

    override fun configRoute(me: Routes) {
        me.add("/", IndexController::class.java, "/index")

        me.add("/page/sales/client", ClientController::class.java, "/sales/client")
    }

    override fun configEngine(me: Engine) {
        me.addSharedFunction("/common/_layout.html")
        me.addSharedFunction("/common/_paginate.html")
    }

    override fun configPlugin(me: Plugins) {
        // 配置 druid 数据库连接池插件
        val druidPlugin = createDruidPlugin()

        me.add(druidPlugin)
        // 配置ActiveRecord插件
        val arp = ActiveRecordPlugin(druidPlugin)
        // 所有映射在 MappingKit 中自动化搞定
        _MappingKit.mapping(arp)

        arp.setShowSql(p!!.getBoolean("devMode", false));

        arp.getEngine().setToClassPathSourceFactory();
        arp.addSqlTemplate("/sql/_all.sql");

        me.add(arp)

    }

    override fun configInterceptor(me: Interceptors) {}

    override fun configHandler(me: Handlers) {
        // 在模板中使用 #(base)  获取 url 的根路径
        me.add(ContextPathHandler("base"));
    }

    companion object {
        var p: Prop? = null

        /**
         * PropKit.useFirstFound(...) 使用参数中从左到右最先被找到的配置文件
         * 从左到右依次去找配置，找到则立即加载并立即返回，后续配置将被忽略
         */
        fun loadConfig() {
            if (p == null) {
                p = PropKit.useFirstFound("demo-config-pro.txt", "demo-config-dev.txt")
            }
        }

        @JvmStatic
        fun createDruidPlugin(): DruidPlugin {
            loadConfig()
            return DruidPlugin(
                p!!["jdbcUrl"],
                p!!["user"],
                p!!["password"].trim { it <= ' ' }
            )
        }

        @JvmStatic
        fun main(args: Array<String>) {
            UndertowServer.start(StartConfig::class.java)
        }
    }
}
