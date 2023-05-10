package com.github.momikensneg.life.quest.controller

import com.github.momikensneg.life.quest.client.GatewayClient
import com.github.momikensneg.life.quest.exception.ForbiddenException
import reactor.core.publisher.Mono

open class SecuredController(private val client: GatewayClient) {

    protected fun checkAdmin(authHeader: String) =
        client.isAdmin(authHeader)
            .filter { isAdmin -> isAdmin}
            .switchIfEmpty(Mono.error(ForbiddenException("User is not admin")))

    protected fun getUsername(authHeader: String) =
        client.getUserName(authHeader)

}