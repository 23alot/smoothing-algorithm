package com.supesuba.smoothing.domain

import com.supesuba.smoothing.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class PNTriangleSmoothingInteractor(

): SmoothingInteractor {
    private var triangles = listOf<PNTriangle>()

    @ExperimentalCoroutinesApi
    private val state = MutableStateFlow<RenderObject>(RenderObject())

    override suspend fun setTriangles(triangles: List<Triangle>) {
        this.triangles = triangles.map(::PNTriangle)
    }

    override suspend fun calculateVertexNormals(vertices: List<Vertex>) {
        vertices.forEachIndexed { _, vertex ->
            vertex.toVertexNormal(triangles.map(PNTriangle::triangle).filter { vertex in it.toList() })
        }
    }

    override suspend fun calculateSupportPoints() {
        triangles.forEach { it.calculateSupportPoints() }
    }

    @ExperimentalCoroutinesApi
    override suspend fun tessellate(tessellationLevel: Int) {
        val start = System.currentTimeMillis()
        val newTriangles = triangles.map { it.tessellate(tessellationLevel) }.flatten()
        val vertices = newTriangles.map { it.toList() }.flatten()
        val end = System.currentTimeMillis()

        state.value = RenderObject(
            verticesArray = vertices.map { it.toList() }.flatten().toFloatArray(),
            normalsArray = vertices.map { it.getNormalList() }.flatten().toFloatArray(),
            colorsArray = getColorsArray(vertices),
            computationTime = end - start
        )
    }

    @ExperimentalCoroutinesApi
    override suspend fun observeModelChanges(): Flow<RenderObject> = state
        .onStart {
            val vertices = triangles.map { it.triangle.toList() }.flatten()
            emit(
                RenderObject(
                    verticesArray = vertices.map { it.toList() }.flatten().toFloatArray(),
                    normalsArray = vertices.map { it.getNormalList() }.flatten().toFloatArray(),
                    colorsArray = getColorsArray(vertices)
                )
            )
        }

    private fun getColorsArray(vertices: List<Vertex>): FloatArray {
        val color = listOf(0f, 0f, 0f)
        val result = mutableListOf<Float>()
        for (i in 0 until vertices.count()){
            result += color
        }

        return result.toFloatArray()
    }
}