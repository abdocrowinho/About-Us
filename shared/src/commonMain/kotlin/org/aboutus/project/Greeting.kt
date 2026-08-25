package org.aboutus.project

import org.aboutus.project.getPlatform
import org.aboutus.project.sayHello

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return sayHello(platform.name)
    }
}