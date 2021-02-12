package com.mars.rover

class MarsRover(var position: Pair<Int, Int>, var direction: String) {

    fun execute(commands: List<String>) {
        val command = commands.firstOrNull()
        
        when (command) {
            "f" -> position = 1 to 0
            "l" -> direction = turnLeft()
            "r" -> direction = turnRight()
        }
    }

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


}