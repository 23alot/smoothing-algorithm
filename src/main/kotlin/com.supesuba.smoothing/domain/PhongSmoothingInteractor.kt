package com.supesuba.smoothing.domain

import com.supesuba.smoothing.PhongTriangle
import com.supesuba.smoothing.RenderObject
import com.supesuba.smoothing.Triangle
import com.supesuba.smoothing.Vertex
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart

class PhongSmoothingInteractor(

) : SmoothingInteractor {
    private val triangles = mutableListOf<PhongTriangle>()

    @ExperimentalCoroutinesApi
    private val state = MutableStateFlow<RenderObject>(RenderObject())


    override suspend fun addTriangle(triangle: Triangle) {
        triangles += PhongTriangle(triangle)
    }

    override suspend fun calculateVertexNormals(vertices: List<Vertex>) {
        vertices.forEachIndexed { _, vertex ->
            vertex.toVertexNormal(
                triangles.map(PhongTriangle::triangle).filter { vertex in it.toList() })
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun tessellate(tessellationLevel: Int) {
        val newTriangles = triangles.map { it.tessellate(tessellationLevel, ALPHA) }.flatten()
        val vertices = newTriangles.map { it.toList() }.flatten()

        state.value = RenderObject(
            verticesArray = vertices.map { it.toList() }.flatten().toFloatArray(),
            normalsArray = vertices.map { it.getNormalList() }.flatten().toFloatArray(),
            colorsArray = getColorsArray(vertices)
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

    override suspend fun calculateSupportPoints() = Unit

    private fun getColorsArray(vertices: List<Vertex>): FloatArray {
        val color = listOf(0f, 0f, 0f)
        val result = mutableListOf<Float>()
        for (i in 0 until vertices.count()) {
            result += color
        }

        return result.toFloatArray()
    }

    companion object {
        private const val ALPHA = 0.75f
    }
}