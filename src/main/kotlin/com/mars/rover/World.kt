package com.mars.rover

class World(val height: Int, val width: Int, val obstaclePositions: Set<Point> = setOf()) {

    fun topography(): Array<Array<SquareType>> {

        val topography = Array(height) { Array(width) { SquareType.DESERT } }

        obstaclePositions.forEach { position ->
            topography[position.line][position.column] = SquareType.OBSTACLE
        }

        return topography
    }
}

