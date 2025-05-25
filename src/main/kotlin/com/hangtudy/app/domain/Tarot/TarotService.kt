package com.hangtudy.domain.Tarot

import com.hangtudy.app.domain.Tarot.Activity
import com.hangtudy.app.domain.Tarot.ActivityRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TarotService(
    private val activityRepository: ActivityRepository
) {
    fun addTarot(
        category: String,
        userIp: String,
        userContent: String,
        resultContent: String
    ) {
        val nowUtc = LocalDateTime.now() // UTC 시간
        val nowKst = nowUtc.plusHours(9) // KST 시간 (+9시간)

        val activity = Activity(
            category = category.trim(),
            ipAddress = userIp.trim(),
            userContent = userContent.trim(),
            resultContent = resultContent.trim(),
            createdAt = nowUtc,
            updatedAt = nowUtc,
            createdAtKst = nowKst,
            updatedAtKst = nowKst
        )

        activityRepository.save(activity)
    }
}