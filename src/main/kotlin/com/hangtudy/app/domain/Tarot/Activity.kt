package com.hangtudy.app.domain.Tarot

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document(collection = "Activity")
data class Activity(
    @Id
    val id: String? = null,

    @Field("category")
    val category: String,

    @Field("ip_address")
    val ipAddress: String,

    @Field("user_content")
    val userContent: String,

    @Field("result_content")
    val resultContent: String, // 원본 JSON 문자열

    @Field("result_data")
    val resultData: TarotResult? = null, // 파싱된 타로 결과

    @Field("created_at")
    val createdAt: LocalDateTime,

    @Field("updated_at")
    val updatedAt: LocalDateTime,

    @Field("created_at_kst")
    val createdAtKst: LocalDateTime,

    @Field("updated_at_kst")
    val updatedAtKst: LocalDateTime
) {
    data class TarotResult(
        @Field("cards")
        val cards: List<TarotCard>,
        
        @Field("interpretation")
        val interpretation: String,
        
        @Field("timestamp")
        val timestamp: String
    )

    data class TarotCard(
        @Field("name")
        val name: String,
        
        @Field("nameKr")
        val nameKr: String,
        
        @Field("reversed")
        val reversed: Boolean,
        
        @Field("interpretation")
        val interpretation: String
    )
}