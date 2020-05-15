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

    operator fun times(transposedVector: TransposedVector): Float =
        x * transposedVector.p1 +
                y * transposedVector.p2 +
                z * transposedVector.p3

    operator fun times(vv: VectorOfVector): Vector = Vector(
        x = this.x * vv.v1.x + this.y * vv.v2.x + this.z * vv.v3.x,
        y = this.x * vv.v1.y + this.y * vv.v2.y + this.z * vv.v3.y,
        z = this.x * vv.v1.z + this.y * vv.v2.z + this.z * vv.v3.z
    )

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

data class VectorOfVector(
    val v1: Vector,
    val v2: Vector,
    val v3: Vector
) {
    operator fun times(v: Vector): Vector = Vector(
        x = 0f,
        y = 0f,
        z = 0f
    )
}

data class TransposedVector(
    val p1: Float,
    val p2: Float,
    val p3: Float
) {
    operator fun times(v: Vector): VectorOfVector = VectorOfVector(
        v1 = Vector(x = this.p1 * v.x, y = this.p1 * v.y, z = this.p1 * v.z),
        v2 = Vector(x = this.p2 * v.x, y = this.p2 * v.y, z = this.p2 * v.z),
        v3 = Vector(x = this.p3 * v.x, y = this.p3 * v.y, z = this.p3 * v.z)
    )
}