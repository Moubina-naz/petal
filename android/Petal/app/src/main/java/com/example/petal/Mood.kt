package com.example.petal

enum class Mood(val value: Int, val label: String) {
    VERY_BAD(1, "Very Bad"),
    BAD(2, "Bad"),
    NEUTRAL(3, "Neutral"),
    GOOD(4, "Good"),
    VERY_GOOD(5, "Very Good");

    companion object {
        fun from(value: Int?): Mood? =
            values().find { it.value == value }
    }
}