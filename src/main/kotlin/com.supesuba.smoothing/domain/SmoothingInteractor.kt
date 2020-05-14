package com.supesuba.smoothing.domain

import com.supesuba.smoothing.RenderObject
import com.supesuba.smoothing.Triangle
import com.supesuba.smoothing.Vertex
import kotlinx.coroutines.flow.Flow

interface SmoothingInteractor {

    suspend fun addTriangle(triangle: Triangle)

    suspend fun tessellate(tessellationLevel: Int)

    suspend fun observeModelChanges(): Flow<RenderObject>

    suspend fun calculateVertexNormals(vertices: List<Vertex>)

    suspend fun calculateSupportPoints()
}