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
    val resultContent: String,

    @Field("created_at")
    val createdAt: LocalDateTime,

    @Field("updated_at")
    val updatedAt: LocalDateTime,

    @Field("created_at_kst")
    val createdAtKst: LocalDateTime,

    @Field("updated_at_kst")
    val updatedAtKst: LocalDateTime
)