package com.hangtudy.app.interfaces.api.v1.auth

import com.hangtudy.app.interfaces.api.exception.ExceptionCode
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.ZonedDateTime
import java.util.UUID

/**
 * 사용자 로그인 요청 데이터 클래스
 * @property email 사용자 이메일 (필수)
 * @property password 사용자 비밀번호 (필수)
 */
data class UserLoginRequest(
    val email: String,
    val password: String
)

/**
 * 토큰 갱신 요청 데이터 클래스
 * @property refreshToken 갱신 토큰 (필수)
 */
data class TokenRefreshRequest(
    val refreshToken: String
)

/**
 * 사용자 정보 데이터 클래스
 * @property id 사용자 고유 ID
 * @property email 사용자 이메일
 * @property name 사용자 이름
 * @property profilePictureUrl 프로필 사진 URL
 */
data class UserInfo(
    val id: String,
    val email: String,
    val name: String,
    val profilePictureUrl: String?
)

/**
 * 로그인 응답 데이터 클래스
 * @property accessToken 액세스 토큰
 * @property refreshToken 갱신 토큰
 * @property user 사용자 정보
 * @property expiresIn 토큰 만료 시간 (초)
 */
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserInfo,
    val expiresIn: Int = 3600
)

/**
 * 토큰 갱신 응답 데이터 클래스
 * @property accessToken 새로운 액세스 토큰
 * @property refreshToken 새로운 갱신 토큰
 * @property expiresIn 토큰 만료 시간 (초)
 */
data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int = 3600
)

/**
 * 인증 오류 응답 데이터 클래스
 * @property error 오류 코드
 * @property message 오류 메시지
 */
data class AuthErrorResponse(
    val error: String,
    val message: String
)

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
 * 사용자 등록, 로그인, 토큰 갱신 등의 인증 기능을 제공합니다.
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

    @Operation(summary = "사용자 로그인", description = "사용자 로그인을 처리합니다.")
    @ApiResponse(
        responseCode = "200",
        description = "로그인이 성공적으로 완료되었습니다",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = LoginResponse::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "인증 오류",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = AuthErrorResponse::class)
        )]
    )
    @PostMapping("/login")
    fun loginUser(@RequestBody request: UserLoginRequest): ResponseEntity<Any> {
        // 잘못된 이메일 또는 비밀번호 검사
        if (request.email == "invalid@example.com" || request.password == "wrongpassword") {
            val errorResponse = AuthErrorResponse(
                error = ExceptionCode.AUTHENTICATION_ERROR.code,
                message = "Invalid email or password"
            )
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
        }

        // Mock 사용자 정보 생성
        val userId = "usr_" + UUID.randomUUID().toString().replace("-", "")
        val accessToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIkdXNlcklkIiwiaWF0IjoxNzM1NjczMjAwLCJleHAiOjE3MzU2NzY4MDB9.${
                UUID.randomUUID().toString().replace("-", "")
            }"
        val refreshToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIkdXNlcklkIiwidHlwZSI6InJlZnJlc2giLCJpYXQiOjE3MzU2NzMyMDAsImV4cCI6MTczNjI3ODAwMH0.${
                UUID.randomUUID().toString().replace("-", "")
            }"

        val userInfo = UserInfo(
            id = userId,
            email = request.email,
            name = "User Name",
            profilePictureUrl = "https://example.com/profile.jpg"
        )

        val response = LoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            user = userInfo
        )

        return ResponseEntity.ok(response)
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.")
    @ApiResponse(
        responseCode = "200",
        description = "토큰이 성공적으로 갱신되었습니다",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = TokenRefreshResponse::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "인증 오류",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = AuthErrorResponse::class)
        )]
    )
    @PostMapping("/refresh")
    fun refreshToken(@RequestHeader("Authorization") authHeader: String?): ResponseEntity<Any> {
        // Authorization 헤더 검증
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            val errorResponse = AuthErrorResponse(
                error = ExceptionCode.AUTHENTICATION_ERROR.code,
                message = "Invalid refresh token"
            )
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
        }

        val refreshToken = authHeader.substring(7) // "Bearer " 제거

        // 잘못된 리프레시 토큰 검사
        if (refreshToken == "invalid_refresh_token") {
            val errorResponse = AuthErrorResponse(
                error = ExceptionCode.AUTHENTICATION_ERROR.code,
                message = "Invalid refresh token"
            )
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
        }

        // 새로운 토큰 생성
        val newAccessToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyX2lkIiwiaWF0IjoxNzM1NjczMjAwLCJleHAiOjE3MzU2NzY4MDB9.${
                UUID.randomUUID().toString().replace("-", "")
            }"
        val newRefreshToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyX2lkIiwidHlwZSI6InJlZnJlc2giLCJpYXQiOjE3MzU2NzMyMDAsImV4cCI6MTczNjI3ODAwMH0.${
                UUID.randomUUID().toString().replace("-", "")
            }"

        val response = TokenRefreshResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )

        return ResponseEntity.ok(response)
    }
}
