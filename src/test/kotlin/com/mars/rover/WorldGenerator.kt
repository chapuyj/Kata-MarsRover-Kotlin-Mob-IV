package com.mars.rover

class WorldGenerator {

    companion object Factory {
        fun create(squares: Array<Array<SquareType>>): World {
            if (worldIsTooSmall(squares)) {
                return parisianStudio()
            }

            val height = squares.count()
            val width = squares[0].count()

            val obstaclePositions = squares.flatten().mapIndexedNotNull { index, square ->
                val line = index / width
                val column = index % width
                if (square == SquareType.OBSTACLE) {
                    Point(column, line)
                }
                else {
                    null
                }
            }.toSet()

            obstaclePositions.forEach {print(it)}

            return World(height, width, obstaclePositions)
        }

        private fun worldIsTooSmall(squares: Array<Array<SquareType>>) =
            squares.count() < 1 || squares[0].count() < 1

        private fun parisianStudio(): World = World(1, 1)
    }
}