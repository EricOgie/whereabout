package com.tees.s3186984.whereabout.model

data class Notification (
    val type: NoticeType = NoticeType.NOTICE,
    val title: String = "",
    val Data: Any = ShareLocationNotice()
)

data class ShareLocationNotice (
    val notifierId: String = "",
    val shareLocationBucketId: String = ""
)

enum class NoticeType {
    NOTICE,
    EMERGENCY
}
