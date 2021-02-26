package com.mars.rover

class MarsRover(var position: Pair<Int, Int>, var direction: String) {

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
        "N" -> position.goNorth()
        "S" -> position.goSouth()
        "W" -> position.goWest()
        "E" -> position.goEast()
        else -> position
    }

    private fun Pair<Int, Int>.goNorth() = this.copy(second = this.second -1)
    private fun Pair<Int, Int>.goSouth() = this.copy(second = this.second +1)
    private fun Pair<Int, Int>.goWest() = this.copy(first = this.first - 1)
    private fun Pair<Int, Int>.goEast() = this.copy(first = this.first + 1)

    private fun String.opposite() = when(this) {
        "N" -> "S"
        "S" -> "N"
        "W" -> "E"
        "E" -> "W"
        else -> this
    }
}