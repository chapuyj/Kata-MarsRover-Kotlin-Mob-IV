package com.mars.rover

class World(val height: Int, val width: Int, val obstaclePositions: Array<Point> = arrayOf()) {

    fun topography(): Array<Array<SquareType>> {

        val topo = Array(height) { Array(width) { SquareType.DESERT } }

        obstaclePositions.forEach { position ->
            topo[position.line][position.column] = SquareType.OBSTACLE
        }

        return topo
    }
}

