package com.hangtudy.app.interfaces.api.v1.exception

import org.springframework.http.HttpStatus

enum class ExceptionCode(
    val code: String,
    val message: String,
    val httpStatus: HttpStatus
) {
    VALIDATION_ERROR("VALIDATION_ERROR", "Validation error occurred", HttpStatus.BAD_REQUEST),
    AUTHENTICATION_ERROR("AUTHENTICATION_ERROR", "Authentication error occurred", HttpStatus.UNAUTHORIZED)
}
