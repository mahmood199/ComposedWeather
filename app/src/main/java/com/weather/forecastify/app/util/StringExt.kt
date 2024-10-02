package com.weather.forecastify.app.util

// [Romain Guy](https://www.romainguy.dev/posts/2024/speeding-up-isblank/)

fun CharSequence.fastIsBlank(): Boolean {
    for (element in this) {
        if (!element.isWhitespace()) {
            return false
        }
    }
    return true
}

// Faster than above CharSequence.fastIsBlank()
fun CharSequence.fastIsBlank2(): Boolean {
    for (element in this) {
        if (!Character.isWhitespace(element) &&
            element != '\u00a0' && element != '\u2007' && element != '\u202f'
        ) {
            return false
        }
    }
    return true
}

fun String.fastIsBlank(): Boolean {
    for (element in this) {
        if (!element.isWhitespace()) {
            return false
        }
    }
    return true
}

// Faster than above String.fastIsBlank()
fun String.fastIsBlank2(): Boolean {
    for (element in this) {
        if (!Character.isWhitespace(element) &&
            element != '\u00a0' && element != '\u2007' && element != '\u202f'
        ) {
            return false
        }
    }
    return true
}
