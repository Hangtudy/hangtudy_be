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

/**
 * 사용자 등록 요청 데이터 클래스
 * @property email 사용자 이메일 (필수)
 * @property password 사용자 비밀번호 (필수)
 * @property name 사용자 이름 (필수)
 * @property profilePictureUrl 프로필 사진 URL (선택)
 */
data class UserRegistrationRequest(
    val email: String,
    val password: String,
    val name: String,
    val profilePictureUrl: String? = null
)

/**
 * 사용자 등록 응답 데이터 클래스
 * @property id 사용자 고유 ID
 * @property email 사용자 이메일
 * @property name 사용자 이름
 * @property profilePictureUrl 프로필 사진 URL
 * @property createdAt 계정 생성 시간
 * @property workspaces 사용자가 속한 워크스페이스 목록
 */
data class UserRegistrationResponse(
    val id: String,
    val email: String,
    val name: String,
    val profilePictureUrl: String?,
    val createdAt: String,
    val workspaces: List<Any> = emptyList()
)

/**
 * 유효성 검사 오류 응답 데이터 클래스
 * @property error 오류 코드
 * @property message 오류 메시지
 * @property details 상세 오류 정보
 */
data class ValidationErrorResponse(
    val error: String,
    val message: String,
    val details: Map<String, List<String>>
)

/**
 * 인증 관련 API 컨트롤러
 * 사용자 등록, 로그인 등의 인증 기능을 제공합니다.
 */
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
        // 이메일 중복 검사
        if (request.email == "duplicate@example.com") {
            val errorResponse = ValidationErrorResponse(
                error = ExceptionCode.VALIDATION_ERROR.code,
                message = "이미 사용 중인 이메일입니다",
                details = mapOf("email" to listOf("이 이메일은 이미 등록되어 있습니다"))
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
        }

        // 비밀번호 유효성 검사
        if (request.password.length < 8) {
            val errorResponse = ValidationErrorResponse(
                error = ExceptionCode.VALIDATION_ERROR.code,
                message = "비밀번호가 너무 짧습니다",
                details = mapOf("password" to listOf("비밀번호는 최소 8자 이상이어야 합니다"))
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
        }

        // 사용자 ID 생성 및 응답 구성
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
