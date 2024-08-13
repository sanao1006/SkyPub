package app.skypub.common

enum class ScreenType(val index: Int) {
    HOME(0),
    NOTIFICATION(1);

    companion object {
        fun getType(num: Int): ScreenType {
            return when (num) {
                0 -> HOME
                1 -> NOTIFICATION
                else -> HOME
            }
        }
    }
}
