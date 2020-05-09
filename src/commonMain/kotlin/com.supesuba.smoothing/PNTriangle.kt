package com.supesuba.smoothing

import com.supesuba.smoothing.collections.list
import kotlin.math.pow

class PNTriangle(
    private val triangle: Triangle
) {
    init {
        calculateSupportPoints()
    }

    private lateinit var supportPoints: SupportPoints

    private fun calculateSupportPoints() {
        if (::supportPoints.isInitialized) return
        val p = listOf(triangle.v1, triangle.v2, triangle.v3)
        val n = listOf(triangle.v1.normal, triangle.v2.normal, triangle.v3.normal)
        val v = mutableListOf<List<Float>>()
        val w = mutableListOf<List<Float>>()
        for (i in 0..2) {
            val rv = mutableListOf<Float>()
            val rw = mutableListOf<Float>()
            for (j in 0..2) {
                rv += 2f * ((p[j] - p[i]).dotProduct(n[i] + n[j])) / (p[j] - p[i]).dotProduct(p[j] - p[i])
                rw += (p[j] - p[i]).dotProduct(n[i])
            }
            v += rv
            w += rw
        }

        supportPoints = SupportPoints(
            n200 = n[0],
            n020 = n[1],
            n002 = n[2],
            n110 = (n[0] + n[1] - v[0][1] * (p[1] - p[0])).normalize(),
            n011 = (n[1] + n[2] - v[1][2] * (p[2] - p[1])).normalize(),
            n101 = (n[2] + n[0] - v[2][0] * (p[0] - p[2])).normalize(),
            b111 = Vertex(0f, 0f, 0f),
            b210 = (2f * p[0] + p[1] - w[0][1] * n[0].toVertex()).toVertex() / 3f,
            b120 = (2f * p[1] + p[0] - w[1][0] * n[1].toVertex()).toVertex() / 3f,
            b021 = (2f * p[1] + p[2] - w[1][2] * n[1].toVertex()).toVertex() / 3f,
            b012 = (2f * p[2] + p[1] - w[2][1] * n[2].toVertex()).toVertex() / 3f,
            b102 = (2f * p[2] + p[0] - w[2][0] * n[2].toVertex()).toVertex() / 3f,
            b201 = (2f * p[0] + p[2] - w[0][2] * n[0].toVertex()).toVertex() / 3f,
            b003 = p[2],
            b030 = p[1],
            b300 = p[0]
        )
        with(supportPoints) {
            val E = (b210 + b120 + b021 + b012 + b102 + b201) / 6f
            val V = (p[0] + p[1] + p[2]) / 3f

            supportPoints = supportPoints.copy(
                b111 = (1.5f * E - 0.5f * V).toVertex()
            )
        }
    }

    fun tessellate(level: Int): List<Triangle> {
        val vertices = mutableListOf<Vertex>()
        for (i in 0..level) {
            val k = level - i
            for (j in (level - k) downTo 0) {
                val z = i - j

                val vertex = p(
                    u = k.toFloat() / level,
                    v = j.toFloat() / level,
                    w = z.toFloat() / level
                )

                vertex.normal = n(
                    u = k.toFloat() / level,
                    v = j.toFloat() / level,
                    w = z.toFloat() / level
                )

                vertices += vertex
            }
        }

        return trianglesFromVertices(vertices = vertices, level = level)
    }

    private fun p(u: Float, v: Float, w: Float): Vertex {
        with(supportPoints) {
            return b300 * u.pow(3) + b030 * v.pow(3) + b003 * w.pow(3) +
                    3f * b210 * u.pow(2) * v + 3f * b120 * u * v.pow(2) + 3f * b201 * u.pow(2) * w + 3f * b021 * v.pow(2) * w +
                    3f * b102 * u * w.pow(2) + 3f * b012 * v * w.pow(2) + 6f * b111 * u * v * w
        }
    }

    private fun n(u: Float, v: Float, w: Float): Vector {
        with(supportPoints) {
            return n200 * u.pow(2) + n020 * v.pow(2) + n002 * w.pow(2) + 2f * n110 * u * v +
                    2f * n011 * v * w + 2f * n101 * u * w
        }
    }

    private fun trianglesFromVertices(vertices: List<Vertex>, level: Int): List<Triangle> {
        val result = mutableListOf<Triangle>()
        val rightEdgePoints = list<Int>(
            capacity = level,
            supplier = { index -> (index + 2) * (index + 1) / 2 })
        val n = rightEdgePoints.last()
        for (i in 0 until n) {
            val verticesInRow = rightEdgePoints.indexOfFirst { i < it } + 1
            // blue triangle
            result += Triangle(
                v1 = vertices[i],
                v2 = vertices[i + verticesInRow + 1],
                v3 = vertices[i + verticesInRow]
            )
            if ((i + 1 in rightEdgePoints).not()) {
                // red triangle
                result += Triangle(
                    v1 = vertices[i],
                    v2 = vertices[i + 1],
                    v3 = vertices[i + verticesInRow + 1]
                )
            }
        }

        return result
    }

}

fun Triangle.toPNTriangle(): PNTriangle = PNTriangle(this)

data class SupportPoints(
    val b300: Vertex,
    val b030: Vertex,
    val b003: Vertex,
    val b210: Vertex,
    val b120: Vertex,
    val b021: Vertex,
    val b012: Vertex,
    val b201: Vertex,
    val b102: Vertex,
    val b111: Vertex,
    val n200: Vector,
    val n020: Vector,
    val n002: Vector,
    val n110: Vector,
    val n011: Vector,
    val n101: Vector
)