package com.supesuba.smoothing.domain

import com.supesuba.smoothing.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//import kotlinx.coroutines.flow.MutableStateFlow

class PNTriangleSmoothingInteractor(

): SmoothingInteractor {
    private val triangles = mutableListOf<PNTriangle>()

//    @ExperimentalCoroutinesApi
//    private val state = MutableStateFlow<RenderObject>(RenderObject())


    override suspend fun addTriangle(triangle: Triangle) {
        triangles += PNTriangle(triangle = triangle)
    }

    @ExperimentalCoroutinesApi
    override suspend fun tessellate(tessellationLevel: Int) {
        val newTriangles = triangles.map { it.tessellate(tessellationLevel) }.flatten()
        val vertices = newTriangles.map { it.toList() }.flatten()
//        state.value = RenderObject(
//            verticesArray = vertices.map { it.toList() }.flatten().toFloatArray(),
//            normalsArray = vertices.map { it.getNormalList() }.flatten().toFloatArray()
//        )
    }

    override suspend fun calculateVertexNormals(vertices: List<Vertex>) {
        vertices.forEachIndexed { _, vertex ->
            vertex.toVertexNormal(triangles.map(PNTriangle::triangle).filter { vertex in it.toList() })
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun observeModelChanges(): Flow<RenderObject> = flow {  }
}