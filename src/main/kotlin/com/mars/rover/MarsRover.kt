package com.mars.rover

class MarsRover(var position: Point, var direction: String, val world: World) {

    var detectedObstacle: Point? = null

    fun execute(commands: List<String>) {
        commands.forEach { execute(it) }
    }

    private fun execute(command: String) {
        if (detectedObstacle != null) {
            return
        }

        when (command) {
            "f" -> position = moveForward()
            "b" -> position = moveBackward()
            "l" -> direction = turnLeft()
            "r" -> direction = turnRight()
        }
    }

    private fun moveForward() = move(direction)
    private fun moveBackward() = move(direction.opposite())

    private fun turnLeft() = when (direction) {
        "N" -> "W"
        "W" -> "S"
        "S" -> "E"
        "E" -> "N"
        else -> direction
    }

    private fun turnRight() = when (direction) {
        "N" -> "E"
        "E" -> "S"
        "S" -> "W"
        "W" -> "N"
        else -> direction
    }

    private fun move(direction: String): Point {

        val newPosition = when (direction) {
            "N" -> position.goNorth(world)
            "S" -> position.goSouth(world)
            "W" -> position.goWest(world)
            "E" -> position.goEast(world)
            else -> position
        }

        return if (world.obstaclePositions.contains(newPosition)) {
            detectedObstacle = newPosition
            position
        } else newPosition
    }


    private fun Point.goNorth(world: World): Point {
        val futurePosition = this.copy(line = this.line - 1)
        if (futurePosition.line < 0) {
            return this.copy(line = world.height - 1)
        }
        return futurePosition
    }

    private fun Point.goSouth(world: World): Point {
        val futurePosition = this.copy(line = this.line + 1)
        if (futurePosition.line >= world.height) {
            return this.copy(line = 0)
        }
        return futurePosition
    }

    private fun Point.goWest(world: World): Point {
        val futurePosition = this.copy(column = this.column - 1)
        if (futurePosition.column < 0) {
            return this.copy(column = world.width - 1)
        }
        return futurePosition
    }

    private fun Point.goEast(world: World): Point {
        val futurePosition = this.copy(column = this.column + 1)
        if (futurePosition.column >= world.width) {
            return this.copy(column = 0)
        }
        return futurePosition
    }

    private fun String.opposite() = when(this) {
        "N" -> "S"
        "S" -> "N"
        "W" -> "E"
        "E" -> "W"
        else -> this
    }
}