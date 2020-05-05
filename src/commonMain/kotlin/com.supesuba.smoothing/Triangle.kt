package com.supesuba.smoothing

open class Triangle(
    val v1: Vertex,
    val v2: Vertex,
    val v3: Vertex
) {
    val normal = (v1 - v2).crossProduct(v1 - v3).normalize()

    fun toList(): List<Vertex> = listOf(v1, v2, v3)
}