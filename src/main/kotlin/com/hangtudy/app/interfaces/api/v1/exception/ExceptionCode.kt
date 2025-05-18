package com.hangtudy.app.interfaces.api.v1.exception

import org.springframework.http.HttpStatus

enum class ExceptionCode(
    val code: String,
    val message: String,
    val httpStatus: HttpStatus
) {
}