package com.supesuba.smoothing.domain

import com.supesuba.smoothing.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class PNTriangleSmoothingInteractor(

): SmoothingInteractor {
    private val triangles = mutableListOf<PNTriangle>()

    @ExperimentalCoroutinesApi
    private val state = MutableStateFlow<RenderObject>(RenderObject())


    override suspend fun addTriangle(triangle: Triangle) {
        triangles += PNTriangle(triangle = triangle)
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
        val newTriangles = triangles.map { it.tessellate(tessellationLevel) }.flatten()
        val vertices = newTriangles.map { it.toList() }.flatten()

        val colors = vertices.map { it.toList() }.flatten().toMutableList()
        colors += colors + colors

        state.value = RenderObject(
            verticesArray = vertices.map { it.toList() }.flatten().toFloatArray(),
            normalsArray = vertices.map { it.getNormalList() }.flatten().toFloatArray(),
            colorsArray = colors.toFloatArray()
        )
    }

    @ExperimentalCoroutinesApi
    override suspend fun observeModelChanges(): Flow<RenderObject> = state
        .onStart {
            val vertices = triangles.map { it.triangle.toList() }.flatten()
            val colors = vertices.map { it.toList() }.flatten().toMutableList()
            colors += colors + colors
            emit(
                RenderObject(
                    verticesArray = vertices.map { it.toList() }.flatten().toFloatArray(),
                    normalsArray = vertices.map { it.getNormalList() }.flatten().toFloatArray(),
                    colorsArray = colors.toFloatArray()
                )
            )
        }
}