package app.skypub.common

enum class ReasonEnum(val value: String) {
    LIKE("like"),
    REPOST("repost"),
    FOLLOW("follow"),
    MENTION("mention"),
    REPLY("reply"),
    QUOTE("quote"),
    STARTERPACK_JOINED("starterpack-joined"),
    UNKNOWN("unknown");

    companion object {
        fun getType(value: String): ReasonEnum {
            return when (value) {
                LIKE.value -> LIKE
                REPOST.value -> REPOST
                FOLLOW.value -> FOLLOW
                MENTION.value -> MENTION
                REPLY.value -> REPLY
                QUOTE.value -> QUOTE
                STARTERPACK_JOINED.value -> STARTERPACK_JOINED
                else -> UNKNOWN
            }
        }
    }
}
