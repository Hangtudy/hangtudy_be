package com.hangtudy.app.interfaces.api.v1.auth

import com.hangtudy.app.interfaces.api.v1.exception.ExceptionCode
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime
import java.util.UUID

data class UserRegistrationRequest(
    val email: String,
    val password: String,
    val name: String,
    val profilePictureUrl: String? = null
)

data class UserRegistrationResponse(
    val id: String,
    val email: String,
    val name: String,
    val profilePictureUrl: String?,
    val createdAt: String,
    val workspaces: List<Any> = emptyList()
)

data class ValidationErrorResponse(
    val error: String,
    val message: String,
    val details: Map<String, List<String>>
)

@RestController
@RequestMapping(path = ["v1/api/auth"])
@Tag(name = "Auth", description = "사용자 관리 API")
class AuthController {

    @Operation(summary = "사용자 등록", description = "사용자를 등록합니다.")
    @ApiResponse(
        responseCode = "201",
        description = "사용자가 성공적으로 등록되었습니다",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = UserRegistrationResponse::class)
        )]
    )
    @ApiResponse(
        responseCode = "400",
        description = "유효성 검사 오류",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ValidationErrorResponse::class)
        )]
    )
    @PostMapping("/register")
    fun registerUser(@RequestBody request: UserRegistrationRequest): ResponseEntity<Any> {

        if (request.email == "duplicate@example.com") {
            val errorResponse = ValidationErrorResponse(
                error = ExceptionCode.VALIDATION_ERROR.code,
                message = "Email already in use",
                details = mapOf("email" to listOf("This email is already registered"))
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
        }

        val userId = "usr_" + UUID.randomUUID().toString().replace("-", "")
        val response = UserRegistrationResponse(
            id = userId,
            email = request.email,
            name = request.name,
            profilePictureUrl = request.profilePictureUrl,
            createdAt = ZonedDateTime.now().toString()
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}
