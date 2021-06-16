package com.mars.rover

import com.mars.rover.CommandEnum.BACKWARD
import com.mars.rover.CommandEnum.FORWARD
import com.mars.rover.CommandEnum.LEFT
import com.mars.rover.CommandEnum.RIGHT
import com.mars.rover.DirectionEnum.NORTH
import com.mars.rover.DirectionEnum.SOUTH
import com.mars.rover.DirectionEnum.EAST
import com.mars.rover.DirectionEnum.WEST

class MarsRover(var position: Point, var direction: DirectionEnum, val world: World) {

    var detectedObstacle: Point? = null
        private set

    fun execute(commands: List<CommandEnum>) {
        commands.forEach { execute(it) }
    }

    private fun execute(command: CommandEnum) {
        if (detectedObstacle != null) {
            return
        }

        when (command) {
            FORWARD -> position = moveForward()
            BACKWARD -> position = moveBackward()
            LEFT -> direction = turnLeft()
            RIGHT -> direction = turnRight()
        }
    }

    private fun moveForward() = move(direction)
    private fun moveBackward() = move(direction.opposite())

    private fun turnLeft() = when (direction) {
        NORTH -> WEST
        WEST -> SOUTH
        SOUTH -> EAST
        EAST -> NORTH
    }

    private fun turnRight() = when (direction) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }

    private fun move(direction: DirectionEnum): Point {

        val newPosition = when (direction) {
            NORTH -> position.goNorth(world)
            SOUTH -> position.goSouth(world)
            WEST -> position.goWest(world)
            EAST -> position.goEast(world)
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
}