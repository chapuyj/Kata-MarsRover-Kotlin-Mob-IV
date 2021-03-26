package com.mars.rover

class MarsRover(var position: Pair<Int, Int>, var direction: String, val world: World) {

    fun execute(commands: List<String>) {
        commands.forEach { execute(it) }
    }

    private fun execute(command: String) {
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

    private fun move(direction: String): Pair<Int, Int> = when (direction) {
        "N" -> position.goNorth(world)
        "S" -> position.goSouth(world)
        "W" -> position.goWest(world)
        "E" -> position.goEast(world)
        else -> position
    }

    private fun Pair<Int, Int>.goNorth(world: World): Pair<Int, Int> {
        val futurePosition = this.copy(second = this.second - 1)
        if (futurePosition.second < 0) {
            return this.copy(second = world.height - 1)
        }
        return futurePosition
    }

    private fun Pair<Int, Int>.goSouth(world: World): Pair<Int, Int> {
        val futurePosition = this.copy(second = this.second + 1)
        if (futurePosition.second >= world.height) {
            return this.copy(second = 0)
        }
        return futurePosition
    }

    private fun Pair<Int, Int>.goWest(world: World): Pair<Int, Int> {
        val futurePosition = this.copy(first = this.first - 1)
        if (futurePosition.first < 0) {
            return this.copy(first = world.width - 1)
        }
        return futurePosition
    }

    private fun Pair<Int, Int>.goEast(world: World): Pair<Int, Int> {
        val futurePosition = this.copy(first = this.first + 1)
        if (futurePosition.first >= world.width) {
            return this.copy(first = 0)
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