package com.supesuba.smoothing

class Figure {

    private var triangles = mutableListOf<Triangle>()

    fun addTriangle(t: Triangle) = triangles.add(t)

    fun calculateVertexNormals(vertices: List<Vertex>) {
        vertices.forEachIndexed { index, vertex ->
            vertex.toVertexNormal(triangles.filter { vertex in it.toList() })
        }
    }

    fun tessellatePN() {
        triangles = triangles.map { it.toPNTriangle().tesselate(1) }.flatten().toMutableList()
        val a = 0
    }

    fun toVertexList(): List<Float> = triangles.map { it.toList().map { it.toList() }.flatten() }.flatten()

    fun toNormalList(): List<Float> = triangles.map { it.toList().map { it.getNormalList() }.flatten() }.flatten()
}