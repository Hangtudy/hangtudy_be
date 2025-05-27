package com.hangtudy.app.interfaces.api.v1.auth

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * AuthController 테스트 클래스
 * 사용자 등록 API의 다양한 시나리오를 테스트합니다.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    /**
     * 사용자 등록 성공 테스트
     * 모든 필수 필드와 선택적 필드를 포함한 요청이 성공적으로 처리되는지 확인합니다.
     */
    @Test
    fun `사용자 등록이 성공적으로 완료되어야 한다`() {
        // 테스트 요청 데이터 준비
        val requestBody = """
            {
                "email": "user@example.com",
                "password": "securePassword123",
                "name": "홍길동",
                "profilePictureUrl": "https://example.com/profile.jpg"
            }
        """.trimIndent()

        // API 요청 실행 및 응답 검증
        mockMvc.perform(
            post("/v1/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.email").value("user@example.com"))
            .andExpect(jsonPath("$.name").value("홍길동"))
            .andExpect(jsonPath("$.profilePictureUrl").value("https://example.com/profile.jpg"))
            .andExpect(jsonPath("$.createdAt").exists())
            .andExpect(jsonPath("$.workspaces").isArray)
    }

    /**
     * 이메일 중복 오류 테스트
     * 이미 등록된 이메일로 가입 시도 시 적절한 오류 응답이 반환되는지 확인합니다.
     */
    @Test
    fun `이미 사용 중인 이메일로 가입 시도 시 유효성 검사 오류가 반환되어야 한다`() {
        // 테스트 요청 데이터 준비
        val requestBody = """
            {
                "email": "duplicate@example.com",
                "password": "securePassword123",
                "name": "김철수",
                "profilePictureUrl": "https://example.com/profile.jpg"
            }
        """.trimIndent()

        // API 요청 실행 및 응답 검증
        mockMvc.perform(
            post("/v1/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("이미 사용 중인 이메일입니다"))
            .andExpect(jsonPath("$.details.email[0]").value("이 이메일은 이미 등록되어 있습니다"))
    }

    /**
     * 비밀번호 유효성 검사 테스트
     * 짧은 비밀번호로 가입 시도 시 적절한 오류 응답이 반환되는지 확인합니다.
     */
    @Test
    fun `짧은 비밀번호로 가입 시도 시 유효성 검사 오류가 반환되어야 한다`() {
        // 테스트 요청 데이터 준비
        val requestBody = """
            {
                "email": "user3@example.com",
                "password": "short",
                "name": "박지민"
            }
        """.trimIndent()

        // API 요청 실행 및 응답 검증
        mockMvc.perform(
            post("/v1/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("비밀번호가 너무 짧습니다"))
            .andExpect(jsonPath("$.details.password[0]").value("비밀번호는 최소 8자 이상이어야 합니다"))
    }
}
