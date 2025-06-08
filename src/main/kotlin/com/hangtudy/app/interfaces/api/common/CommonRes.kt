package com.hangtudy.app.interfaces.api.common

import com.hangtudy.app.interfaces.api.exception.ExceptionCode
import com.hangtudy.app.interfaces.api.exception.ExceptionMessage
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus

@Schema(description = "공통 API 응답 객체")
data class CommonRes<T>(
    @Schema(description = "반환 결과")
    val resultType: ResultType,
    @Schema(description = "반환 데이터")
    val data: T,
    @Schema(description = "반환 메시지", defaultValue = "SUCCESS or error 메시지")
    val exception: ExceptionMessage
) {
    override fun toString(): String {
        return """
            {
                "CommonRes": {
                    "resultType": "$resultType",
                    "data": $data,
                    "exception": "$exception"
                }
            }
        """.trimIndent()
    }

    companion object {
        fun <T> success(data: T): CommonRes<T> {
            return CommonRes(ResultType.SUCCESS, data, ExceptionMessage())
        }

        fun error(error: Exception, status: HttpStatus): CommonRes<Map<String, Any>> {
            return CommonRes(ResultType.FAIL, emptyMap(), ExceptionMessage(error, status))
        }

        fun error(error: ExceptionCode, errorData: Any?): CommonRes<Map<String, Any>> {
            return CommonRes(ResultType.FAIL, emptyMap(), ExceptionMessage(error, errorData))
        }
    }
}
