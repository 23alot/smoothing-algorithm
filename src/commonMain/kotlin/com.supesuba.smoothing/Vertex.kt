package com.supesuba.smoothing

typealias Normal = Vector

data class Vertex(
    val x: Float,
    val y: Float,
    val z: Float
) {

    lateinit var normal: Normal

    operator fun minus(other: Vertex): Vector =
        Vector(
            x = this.x - other.x,
            y = this.y - other.y,
            z = this.z - other.z
        )

    operator fun times(value: Float): Vertex =
        Vertex(
            x = this.x * value,
            y = this.y * value,
            z = this.z * value
        )

    operator fun plus(value: Float): Vertex =
        Vertex(
            x = this.x + value,
            y = this.y + value,
            z = this.z + value
        )

    operator fun plus(value: Vertex): Vertex =
        Vertex(
            x = this.x + value.x,
            y = this.y + value.y,
            z = this.z + value.z
        )

    operator fun div(value: Float): Vertex =
        Vertex(
            x = this.x / value,
            y = this.y / value,
            z = this.z / value
        )

    fun toVector(): Vector =
        Vector(
            x = this.x,
            y = this.y,
            z = this.z
        )

    fun toVertexNormal(triangles: List<Triangle>) {
        normal = triangles.map(Triangle::normal).average().normalize()
    }

    fun toList(): List<Float> = listOf(x, y, z)

    fun getNormalList(): List<Float> = normal.toList()
}

operator fun Float.times(value: Vertex): Vertex =
    Vertex(
        x = this * value.x,
        y = this * value.y,
        z = this * value.z
    )

fun FloatArray.toVertexList(): List<Vertex> {
    val vertices = mutableListOf<Vertex>()
    for (i in 0 until this.count() - 2 step 3) {
        vertices += Vertex(
            x = this[i],
            y = this[i + 1],
            z = this[i + 2]
        )
    }

    return vertices
}