package org.one.oneappstorebackend

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform