package com.supesuba.smoothing

import collections.list

class PhongTriangle(
    val triangle: Triangle
) {

    fun tessellate(tessellationLevel: Int, alpha: Float): List<Triangle> {
        val vertices = mutableListOf<Vertex>()
        for (i in 0..tessellationLevel) {
            val k = tessellationLevel - i
            for (j in (tessellationLevel - k) downTo 0) {
                val z = i - j

                val vertex = pS(
                    u = k.toFloat() / tessellationLevel,
                    v = j.toFloat() / tessellationLevel,
                    w = z.toFloat() / tessellationLevel,
                    alpha = alpha
                )

                vertex.normal = n(
                    u = k.toFloat() / tessellationLevel,
                    v = j.toFloat() / tessellationLevel,
                    w = z.toFloat() / tessellationLevel
                )

                vertices += vertex
            }
        }

        return trianglesFromVertices(vertices = vertices, level = tessellationLevel)
    }

    private fun p(u: Float, v: Float, w: Float): Vertex {
        val barVector = Vector(x = u, y = v, z = w)
        val vertexVector = VectorOfVector(
            v1 = triangle.v1.toVector(),
            v2 = triangle.v2.toVector(),
            v3 = triangle.v3.toVector()
        )

        return (barVector * vertexVector).toVertex()
    }

    private fun n(u: Float, v: Float, w: Float): Normal {
        val barVector = Vector(x = u, y = v, z = w)
        val normalVector = VectorOfVector(
            v1 = triangle.v1.normal,
            v2 = triangle.v2.normal,
            v3 = triangle.v3.normal
        )
        return (barVector * normalVector).normalize()
    }

    private fun pi(q: Vertex, v: Vertex, n: Normal): Vector = q.toVector() -
            ((q - v) * n) * n

    private fun pS(u: Float, v: Float, w: Float, alpha: Float): Vertex {
        val p = p(u, v, w)

        return ((1 - alpha) * p.toVector() + alpha * Vector(u, v, w) * VectorOfVector(
            pi(p, triangle.v1, triangle.v1.normal),
            pi(p, triangle.v2, triangle.v2.normal),
            pi(p, triangle.v3, triangle.v3.normal)
        )).toVertex()
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