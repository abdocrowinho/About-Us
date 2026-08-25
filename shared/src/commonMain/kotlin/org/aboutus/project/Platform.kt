package org.aboutus.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform