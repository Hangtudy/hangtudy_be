package com.hangtudy.app.interfaces.api.exception

import org.springframework.boot.logging.LogLevel

open class ApiException(
    exceptionCode: ExceptionCode,
    private val logLevel: LogLevel,
    private val data: Any? = null
) : RuntimeException(exceptionCode.message)
