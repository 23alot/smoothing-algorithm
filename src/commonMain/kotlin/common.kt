/**
 * Created by 23alot on 07.03.2020.
 */
package com.supesuba.smoothing

expect fun platformName(): String

fun createApplicationScreenMessage(): String = "on ${platformName()}"