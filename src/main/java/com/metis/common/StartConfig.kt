package com.metis.common


import com.jfinal.config.*
import com.jfinal.ext.handler.ContextPathHandler
import com.jfinal.json.FastJsonFactory
import com.jfinal.json.MixedJsonFactory
import com.jfinal.kit.Prop
import com.jfinal.kit.PropKit
import com.jfinal.plugin.activerecord.ActiveRecordPlugin
import com.jfinal.plugin.druid.DruidPlugin
import com.jfinal.server.undertow.UndertowServer
import com.jfinal.template.Engine
import com.jfinal.weixin.sdk.api.ApiConfig
import com.jfinal.weixin.sdk.api.ApiConfigKit
import com.jfinal.wxaapp.WxaConfig
import com.jfinal.wxaapp.WxaConfigKit
import com.metis.common.model._MappingKit
import com.metis.index.AuthInterceptor
import com.metis.index.IndexController
import com.metis.index.LoginInterceptor
import com.metis.sales.ClientController
import com.metis.sales.LeadController
import com.metis.survey.SurveryController

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

        // 支持 select 中的 动态名称 填充进 model
        me.setJsonFactory(MixedJsonFactory())

        // 不支持 select 的动态字段
//        me.setJsonFactory(FastJsonFactory())


        /// 设置 devMode 之下的 action report 是否在 invocation 之后，默认值为 true
        me.setReportAfterInvocation(false)
    }

    override fun configRoute(me: Routes) {
        me.setMappingSuperClass(true);

        me.add("/", IndexController::class.java, "/index")

//        me.add("/wx", WeixinMsgController::class.java)

        me.add("/page/sales/client", ClientController::class.java, "/sales/client")
        me.add("/page/sales/lead", LeadController::class.java, "/sales/lead")

        me.add("survey", SurveryController::class.java, "/survey")

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

    override fun configInterceptor(me: Interceptors) {
        // 全局拦截器
        me.add(LoginInterceptor())
        me.add(AuthInterceptor())
    }

    override fun configHandler(me: Handlers) {
        // 在模板中使用 #(base)  获取 url 的根路径
        me.add(ContextPathHandler("base"));


    }

    override fun onStart() {
        super.onStart()

        val appId = PropKit.get("appId")
        val appSecret = PropKit.get("appSecret")

        val ac = ApiConfig()
        // 配置微信 API 相关参数
        ac.token = PropKit.get("token")
        ac.appId = appId
        ac.appSecret = appSecret

        /**
         * 是否对消息进行加密，对应于微信平台的消息加解密方式：
         * 1：true进行加密且必须配置 encodingAesKey
         * 2：false采用明文模式，同时也支持混合模式
         */
        ac.isEncryptMessage = PropKit.getBoolean("encryptMessage", false)
        ac.encodingAesKey = PropKit.get("encodingAesKey", "setting it in config file")

        /**
         * 多个公众号时，重复调用ApiConfigKit.putApiConfig(ac)依次添加即可，第一个添加的是默认。
         */
        ApiConfigKit.putApiConfig(ac)
        ApiConfigKit.setDevMode(true)

        /**
         * 1.9 新增LocalTestTokenCache用于本地和线上同时使用一套appId时避免本地将线上AccessToken冲掉
         *
         * 设计初衷：https://www.oschina.net/question/2702126_2237352
         *
         * 注意：
         * 1. 上线时应保证此处isLocalDev为false，或者注释掉该不分代码！
         *
         * 2. 为了安全起见，此处可以自己添加密钥之类的参数，例如：
         * http://localhost/weixin/api/getToken?secret=xxxx
         * 然后在WeixinApiController#getToken()方法中判断secret
         *
         * @see WeixinApiController.getToken
         */

        val wc = WxaConfig()
        wc.appId = appId
        wc.appSecret = appSecret
        WxaConfigKit.setWxaConfig(wc)
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
