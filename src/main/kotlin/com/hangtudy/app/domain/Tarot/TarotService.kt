package com.hangtudy.domain.Tarot

import com.fasterxml.jackson.databind.ObjectMapper
import com.hangtudy.app.domain.Tarot.Activity
import com.hangtudy.app.domain.Tarot.ActivityRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TarotService(
    private val activityRepository: ActivityRepository,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(TarotService::class.java)

    fun addTarot(
        category: String,
        userIp: String,
        userContent: String,
        resultContent: String
    ) {
        val nowUtc = LocalDateTime.now()
        val nowKst = nowUtc.plusHours(9)

        // JSON 파싱 시도
        val parsedResult = try {
            objectMapper.readValue(resultContent, Activity.TarotResult::class.java)
        } catch (e: Exception) {
            logger.error(e.printStackTrace().toString())
            null
        }

        val activity = Activity(
            category = category.trim(),
            ipAddress = userIp.trim(),
            userContent = userContent.trim(),
            resultContent = resultContent.trim(),
            resultData = parsedResult,
            createdAt = nowUtc,
            updatedAt = nowUtc,
            createdAtKst = nowKst,
            updatedAtKst = nowKst
        )

        activityRepository.save(activity)
    }
}