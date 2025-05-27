package com.hangtudy.app.interfaces.api.v1.auth

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `사용자 등록이 성공적으로 완료되어야 한다`() {
        val requestBody = """
            {
                "email": "user@example.com",
                "password": "securePassword123",
                "name": "User Name",
                "profilePictureUrl": "https://example.com/profile.jpg"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/v1/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.email").value("user@example.com"))
            .andExpect(jsonPath("$.name").value("User Name"))
            .andExpect(jsonPath("$.profilePictureUrl").value("https://example.com/profile.jpg"))
            .andExpect(jsonPath("$.createdAt").exists())
            .andExpect(jsonPath("$.workspaces").isArray)
    }

    @Test
    fun `이미 사용 중인 이메일로 가입 시도 시 유효성 검사 오류가 반환되어야 한다`() {
        val requestBody = """
            {
                "email": "duplicate@example.com",
                "password": "securePassword123",
                "name": "User Name",
                "profilePictureUrl": "https://example.com/profile.jpg"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/v1/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Email already in use"))
            .andExpect(jsonPath("$.details.email[0]").value("This email is already registered"))
    }
}
