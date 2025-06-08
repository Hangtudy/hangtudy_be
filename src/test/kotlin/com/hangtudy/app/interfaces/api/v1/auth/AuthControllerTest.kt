package com.hangtudy.app.interfaces.api.v1.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.hangtudy.app.interfaces.api.exception.ExceptionCode
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(AuthController::class)
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Nested
    @DisplayName("사용자 등록 API 테스트")
    inner class RegisterUserTest {

        @Test
        @DisplayName("유효한 사용자 정보로 등록 성공")
        fun `should register user successfully with valid data`() {
            // given
            val request = UserRegistrationRequest(
                email = "test@example.com",
                password = "securePassword123",
                name = "테스트 사용자",
                profilePictureUrl = "https://example.com/profile.jpg"
            )

            // when & then
            mockMvc.perform(
                post("/v1/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isCreated)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("테스트 사용자"))
                .andExpect(jsonPath("$.profilePictureUrl").value("https://example.com/profile.jpg"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.workspaces").isArray)
        }

        @Test
        @DisplayName("프로필 사진 URL 없이 등록 성공")
        fun `should register user successfully without profile picture`() {
            // given
            val request = UserRegistrationRequest(
                email = "test2@example.com",
                password = "securePassword123",
                name = "테스트 사용자2"
            )

            // when & then
            mockMvc.perform(
                post("/v1/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.email").value("test2@example.com"))
                .andExpect(jsonPath("$.name").value("테스트 사용자2"))
                .andExpect(jsonPath("$.profilePictureUrl").isEmpty)
        }

        @Test
        @DisplayName("중복된 이메일로 등록 실패")
        fun `should fail to register with duplicate email`() {
            // given
            val request = UserRegistrationRequest(
                email = "duplicate@example.com",
                password = "securePassword123",
                name = "중복 사용자"
            )

            // when & then
            mockMvc.perform(
                post("/v1/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(ExceptionCode.VALIDATION_ERROR.code))
                .andExpect(jsonPath("$.message").value("이미 사용 중인 이메일입니다"))
                .andExpect(jsonPath("$.details.email").exists())
        }

        @Test
        @DisplayName("짧은 비밀번호로 등록 실패")
        fun `should fail to register with short password`() {
            // given
            val request = UserRegistrationRequest(
                email = "test@example.com",
                password = "short",
                name = "테스트 사용자"
            )

            // when & then
            mockMvc.perform(
                post("/v1/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(ExceptionCode.VALIDATION_ERROR.code))
                .andExpect(jsonPath("$.message").value("비밀번호가 너무 짧습니다"))
                .andExpect(jsonPath("$.details.password").exists())
        }
    }

    @Nested
    @DisplayName("사용자 로그인 API 테스트")
    inner class LoginUserTest {

        @Test
        @DisplayName("유효한 로그인 정보로 로그인 성공")
        fun `should login successfully with valid credentials`() {
            // given
            val request = UserLoginRequest(
                email = "user@example.com",
                password = "securePassword123"
            )

            // when & then
            mockMvc.perform(
                post("/v1/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.user.id").exists())
                .andExpect(jsonPath("$.user.email").value("user@example.com"))
                .andExpect(jsonPath("$.user.name").value("User Name"))
                .andExpect(jsonPath("$.user.profilePictureUrl").value("https://example.com/profile.jpg"))
                .andExpect(jsonPath("$.expiresIn").value(3600))
        }

        @Test
        @DisplayName("잘못된 이메일로 로그인 실패")
        fun `should fail to login with invalid email`() {
            // given
            val request = UserLoginRequest(
                email = "invalid@example.com",
                password = "securePassword123"
            )

            // when & then
            mockMvc.perform(
                post("/v1/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isUnauthorized)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(ExceptionCode.AUTHENTICATION_ERROR.code))
                .andExpect(jsonPath("$.message").value("Invalid email or password"))
        }

        @Test
        @DisplayName("잘못된 비밀번호로 로그인 실패")
        fun `should fail to login with wrong password`() {
            // given
            val request = UserLoginRequest(
                email = "user@example.com",
                password = "wrongpassword"
            )

            // when & then
            mockMvc.perform(
                post("/v1/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isUnauthorized)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(ExceptionCode.AUTHENTICATION_ERROR.code))
                .andExpect(jsonPath("$.message").value("Invalid email or password"))
        }
    }

    @Nested
    @DisplayName("토큰 갱신 API 테스트")
    inner class RefreshTokenTest {

        @Test
        @DisplayName("유효한 리프레시 토큰으로 갱신 성공")
        fun `should refresh token successfully with valid refresh token`() {
            // given
            val validRefreshToken =
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyX2lkIiwidHlwZSI6InJlZnJlc2giLCJpYXQiOjE3MzU2NzMyMDAsImV4cCI6MTczNjI3ODAwMH0.valid_refresh_token"

            // when & then
            mockMvc.perform(
                post("/v1/api/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer $validRefreshToken")
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.expiresIn").value(3600))
        }

        @Test
        @DisplayName("잘못된 리프레시 토큰으로 갱신 실패")
        fun `should fail to refresh token with invalid refresh token`() {
            // given
            val invalidRefreshToken = "invalid_refresh_token"

            // when & then
            mockMvc.perform(
                post("/v1/api/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer $invalidRefreshToken")
            )
                .andExpect(status().isUnauthorized)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(ExceptionCode.AUTHENTICATION_ERROR.code))
                .andExpect(jsonPath("$.message").value("Invalid refresh token"))
        }

        @Test
        @DisplayName("Authorization 헤더 없이 토큰 갱신 실패")
        fun `should fail to refresh token without authorization header`() {
            // when & then
            mockMvc.perform(
                post("/v1/api/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isUnauthorized)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(ExceptionCode.AUTHENTICATION_ERROR.code))
                .andExpect(jsonPath("$.message").value("Invalid refresh token"))
        }

        @Test
        @DisplayName("잘못된 형식의 Authorization 헤더로 토큰 갱신 실패")
        fun `should fail to refresh token with malformed authorization header`() {
            // when & then
            mockMvc.perform(
                post("/v1/api/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "InvalidFormat token")
            )
                .andExpect(status().isUnauthorized)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(ExceptionCode.AUTHENTICATION_ERROR.code))
                .andExpect(jsonPath("$.message").value("Invalid refresh token"))
        }
    }
}
