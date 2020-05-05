package com.supesuba.smoothing

import kotlin.math.sqrt

data class Vector(
    val x: Float,
    val y: Float,
    val z: Float
) {

    operator fun div(value: Float): Vector =
        Vector(
            x = this.x / value,
            y = this.y / value,
            z = this.z / value
        )

    operator fun times(value: Float): Vector =
        Vector(
            x = this.x * value,
            y = this.y * value,
            z = this.z * value
        )

    operator fun times(other: Vector): Vector = crossProduct(other)

    operator fun plus(value: Float): Vector =
        Vector(
            x = this.x + value,
            y = this.y + value,
            z = this.z + value
        )

    operator fun plus(value: Vector): Vector =
        Vector(
            x = this.x + value.x,
            y = this.y + value.y,
            z = this.z + value.z
        )

    operator fun minus(value: Float): Vector =
        Vector(
            x = this.x - value,
            y = this.y - value,
            z = this.z - value
        )

    operator fun minus(value: Vector): Vector =
        Vector(
            x = this.x - value.x,
            y = this.y - value.y,
            z = this.z - value.z
        )

    // Векторное произведение
    fun crossProduct(other: Vector): Vector =
        Vector(
            x = this.y * other.z - this.z * other.y,
            y = this.z * other.x - this.x * other.z,
            z = this.x * other.y - this.y * other.x
        )

    // Скалярное произведение
    fun dotProduct(other: Vector): Float = this.x * other.x + this.y * other.y + this.z * other.z

    fun distance(): Float = sqrt(x * x + y * y + z * z)

    fun normalize(): Vector = this / this.distance()

    fun toVertex(): Vertex =
        Vertex(
            x = this.x,
            y = this.y,
            z = this.z
        )

    fun toList(): List<Float> = listOf(x, y, z)
}

operator fun Float.times(value: Vector): Vector = value * this

fun List<Vector>.average(): Vector {
    val averageX = this.map(Vector::x).sum() / this.count()
    val averageY = this.map(Vector::y).sum() / this.count()
    val averageZ = this.map(Vector::z).sum() / this.count()
    return Vector(
        x = averageX,
        y = averageY,
        z = averageZ
    )
}