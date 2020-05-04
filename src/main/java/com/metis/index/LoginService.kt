package com.metis.index

import com.jfinal.aop.Inject
import com.jfinal.kit.Ret
import com.jfinal.kit.StrKit
import com.jfinal.log.Log
import com.metis.common.ZCacheKit
import com.metis.common.model.Session
import com.metis.common.model.ZUser

open class LoginService {
    private val logger = Log.getLog(LoginService::class.java)

    @Inject
    lateinit var userSvc: UserService

    companion object {
        // 存放登录用户的 cacheName
        const val loginAccountCacheName = "loginAccount"

        // 仅用于 cookie 名称，其它地方如 cache 中全部用的 "sessionId" 来做 key
        const val sessionIdName = "x-METIS_ID"
    }

    fun login(email: String, password: String, keepLogin: Boolean): Ret {
        val loginAccount: ZUser? = userSvc.checkLogin(email, password)
        if (loginAccount == null) {
            logger.info("failed login: $email:$password")
            return Ret.fail("msg", "这没你的地方，你不属于这")
        }

        // 如果用户勾选保持登录，暂定过期时间为 3 天，单位为秒
        val liveSeconds = if (keepLogin) 3 * 24 * 60 * 60 else 120 * 60.toLong()
        // 传递给控制层的 cookie
        val maxAgeInSeconds = (if (keepLogin) liveSeconds else -1).toInt()
        // expireAt 用于设置 session 的过期时间点，需要转换成毫秒
        val expireAt = System.currentTimeMillis() + liveSeconds * 1000

        // 保存登录 session 到数据库
        val session = Session()
        val sessionId = StrKit.getRandomUUID()
        session.id = sessionId
        session.accountId = loginAccount.id
        session.expireAt = expireAt
        if (!session.save()) {
            return Ret.fail("msg", "保存 session 到数据库失败，请联系管理员")
        }
        userSvc.loadAllSession()
        loginAccount.removeSensitiveInfo()
        ZCacheKit.put(loginAccountCacheName, sessionId, loginAccount)
        return Ret.ok(sessionIdName, sessionId)
            .set(loginAccountCacheName, loginAccount)
            .set("maxAgeInSeconds", maxAgeInSeconds)
    }

    /**
     * 通过 sessionId 获取登录用户信息
     * sessoin 表结构：session(id, accountId, expireAt)
     *
     *
     * 1：先从缓存里面取，如果取到则返回该值，如果没取到则从数据库里面取
     * 2：在数据库里面取，如果取到了，则检测是否已过期，如果过期则清除记录，
     * 如果没过期则先放缓存一份，然后再返回
     */
    fun getUserBySessionId(sessionId: String): ZUser? {
        // 先从 cache 中取，找到了直接返回
        val u:ZUser? = ZCacheKit.get(loginAccountCacheName, sessionId)
        if (u != null) {
            return u
        }

        // cache 中没有，从 db 中找，并且保存到 cache 中
        /// new Session().dao().findById(sessionId);
        val session = userSvc.fromSessionPool(sessionId!!)
            ?: // session 不存在
            return null
        if (session.isExpired()) {
            // session 已过期
            // 被动式删除过期数据，此外还需要定时线程来主动清除过期数据
            session.delete()
            return null
        }

        ///  new User().dao().findById(session.getAccountId());
        val loginAccount: ZUser = userSvc.fromUserPool(session.accountId)
        // 找到 loginAccount 并且 是正常状态 才允许登录
        if (loginAccount != null) {
            // 移除 password 与 salt 属性值
            loginAccount.removeSensitiveInfo()
            ZCacheKit.put(loginAccountCacheName, sessionId, loginAccount)
            return loginAccount
        }
        return null
    }
}
