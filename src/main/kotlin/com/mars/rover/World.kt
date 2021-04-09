package com.mars.rover

class World(val height: Int, val width: Int, val obstaclePositions: Array<Pair<Int, Int>> = arrayOf()) {

    fun topography(): Array<Array<SquareType>> {

        val topo = Array(height) { Array(width) { SquareType.DESERT } }

        obstaclePositions.forEach { position ->
            topo[position.second][position.first] = SquareType.OBSTACLE
        }

        return topo
    }
}
