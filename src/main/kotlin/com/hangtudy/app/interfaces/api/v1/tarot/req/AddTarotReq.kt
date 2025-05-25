package com.hangtudy.app.interfaces.api.v1.tarot.req

import jakarta.validation.constraints.NotBlank
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "타로 추가 요청")
data class AddTarotReq(
    @field:NotBlank(message = "카테고리는 필수입니다.")
    @Schema(description = "타로 카테고리", example = "연애운")
    val category: String,

    @field:NotBlank(message = "IP 주소는 필수입니다.")
    @Schema(description = "유저의 아이피", example = "127.0.0.1")
    val userIp: String,

    @field:NotBlank(message = "사용자 내용은 필수입니다.")
    @Schema(description = "사용자가 작성한 내용", example = "올해 연애운이 궁금합니다.")
    val userContent: String,
    
    @field:NotBlank(message = "결과 내용은 필수입니다.")
    @Schema(description = "타로 결과 내용", example = "좋은 만남이 기다리고 있습니다.")
    val resultContent: String
)