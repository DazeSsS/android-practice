package com.example.movies.presentation.model

class BadgeCache {
    private var badgeActive = false

    fun isBadgeActive(): Boolean = badgeActive

    fun setBadgeActive(value: Boolean) {
        badgeActive = value
    }
}