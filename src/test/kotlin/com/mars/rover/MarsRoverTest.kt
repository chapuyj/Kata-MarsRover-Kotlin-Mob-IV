package com.mars.rover

import com.mars.rover.CommandEnum.BACKWARD
import com.mars.rover.CommandEnum.FORWARD
import com.mars.rover.CommandEnum.LEFT
import com.mars.rover.CommandEnum.RIGHT
import com.mars.rover.SquareType.DESERT
import com.mars.rover.SquareType.OBSTACLE
import com.mars.rover.DirectionEnum.NORTH
import com.mars.rover.DirectionEnum.SOUTH
import com.mars.rover.DirectionEnum.EAST
import com.mars.rover.DirectionEnum.WEST
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


internal class MarsRoverTest {

    companion object {
        @JvmStatic
        fun leftCommands() = listOf(
                Arguments.of(NORTH, WEST),
                Arguments.of(WEST, SOUTH),
                Arguments.of(EAST, NORTH),
                Arguments.of(SOUTH, EAST)
        )

        @JvmStatic
        fun rightCommands() = listOf(
                Arguments.of(NORTH, EAST),
                Arguments.of(WEST, NORTH),
                Arguments.of(EAST, SOUTH),
                Arguments.of(SOUTH, WEST)
        )

        @JvmStatic
        fun forwardCommands() = listOf(
                Arguments.of(Point(1, 1), NORTH, Point(1, 0)),
                Arguments.of(Point(0, 1), SOUTH, Point(0, 2)),
                Arguments.of(Point(1, 0), WEST, Point(0, 0)),
                Arguments.of(Point(1, 0), EAST, Point(2, 0)),
                Arguments.of(Point(0, 0), NORTH, Point(0, 2)),
                Arguments.of(Point(0, 1), WEST, Point(2, 1))
        )

        @JvmStatic
        fun backwardCommands() = listOf(
                Arguments.of(Point(1, 1), NORTH, Point(1, 2)),
                Arguments.of(Point(1, 1), SOUTH, Point(1, 0)),
                Arguments.of(Point(1, 1), WEST, Point(2, 1)),
                Arguments.of(Point(1, 1), EAST, Point(0, 1)),
                Arguments.of(Point(0, 2), NORTH, Point(0, 0)),
                Arguments.of(Point(2, 1), WEST, Point(0, 1))
        )
    }

    @Test
    internal fun `Should create instance with initial position and orientation`() {
        val world = World(3, 3)
        val position = Point(0, 0)
        val direction = NORTH
        val marsRover = MarsRover(position, direction, world)

        marsRover.position shouldBe position
        marsRover.direction shouldBe NORTH
    }

    @ParameterizedTest(name = "The new direction is {1} with initial direction {0}")
    @MethodSource(value = ["leftCommands"])
    internal fun `Should turn left`(initialDirection: DirectionEnum, newDirection: DirectionEnum) {
        val world = World(3, 3)
        val position = Point(0, 0)
        val marsRover = MarsRover(position, initialDirection, world)

        marsRover.execute(listOf(LEFT))

        marsRover.position shouldBe position
        marsRover.direction shouldBe newDirection
    }

    @ParameterizedTest(name = "The new direction is {1} with initial direction {0}")
    @MethodSource(value = ["rightCommands"])
    internal fun `Should turn right`(initialDirection: DirectionEnum, newDirection: DirectionEnum) {
        val world = World(3, 3)
        val position = Point(0, 0)
        val marsRover = MarsRover(position, initialDirection, world)

        marsRover.execute(listOf(RIGHT))

        marsRover.position shouldBe position
        marsRover.direction shouldBe newDirection
    }

    @ParameterizedTest(name = "The new position is {2} after moving forward from position {0} and direction {1}")
    @MethodSource(value = ["forwardCommands"])
    internal fun `Should move forward`(
            initialPosition: Point,
            direction: DirectionEnum,
            expectedPosition: Point
    ) {
        val world = World(3, 3)
        val marsRover = MarsRover(initialPosition, direction, world)

        marsRover.execute(listOf(FORWARD))

        marsRover.position.shouldBe(expectedPosition)
        marsRover.direction shouldBe direction
    }

    @ParameterizedTest(name = "The new position is {2} after moving backward from position {0} and direction {1}")
    @MethodSource(value = ["backwardCommands"])
    internal fun `Should move backward`(
            initialPosition: Point,
            direction: DirectionEnum,
            expectedPosition: Point
    ) {
        val world = World(3, 3)
        val marsRover = MarsRover(initialPosition, direction, world)

        marsRover.execute(listOf(BACKWARD))

        marsRover.position.shouldBe(expectedPosition)
        marsRover.direction shouldBe direction
    }

    @Test
    internal fun `Should accept multiple commands`() {
        val world = World(3, 3)
        val marsRover = MarsRover(Point(2, 2), NORTH, world)

        marsRover.execute(listOf(LEFT, FORWARD, LEFT, BACKWARD, RIGHT, RIGHT, RIGHT, FORWARD))

        marsRover.position.shouldBe(Point(2, 1))
        marsRover.direction shouldBe EAST
    }

    @Test
    internal fun `Should stay confined in parisian studio`() {
        val world = World(1, 1)
        val marsRover = MarsRover(Point(0, 0), SOUTH, world)

        marsRover.execute(listOf(FORWARD, LEFT, FORWARD, FORWARD, FORWARD, BACKWARD, FORWARD, FORWARD))

        marsRover.position.shouldBe(Point(0, 0))
        marsRover.direction shouldBe EAST
    }

    @Test
    internal fun `Should generate a world`() {
        val world = WorldGenerator.create(arrayOf(
                arrayOf(DESERT, DESERT, DESERT, DESERT),
                arrayOf(DESERT, DESERT, OBSTACLE, DESERT),
                arrayOf(OBSTACLE, DESERT, DESERT, DESERT),
        ))

        world.height shouldBe 3
        world.width shouldBe 4

        world.topography() shouldBe arrayOf(
                arrayOf(DESERT, DESERT, DESERT, DESERT),
                arrayOf(DESERT, DESERT, OBSTACLE, DESERT),
                arrayOf(OBSTACLE, DESERT, DESERT, DESERT),
        )
    }

    @Test
    internal fun `Should generate a parisian studio with invalid inputs in generator`() {
        val world = WorldGenerator.create(arrayOf(arrayOf()))

        world.height shouldBe 1
        world.width shouldBe 1
    }

    @Test
    internal fun `Should stop commands if rover encounters an obstacle and reports obstacle`() {
        val world = WorldGenerator.create(arrayOf(
                arrayOf(DESERT, OBSTACLE, DESERT, DESERT),
                arrayOf(DESERT, DESERT, DESERT, DESERT),
                arrayOf(DESERT, DESERT, DESERT, DESERT),
        ))

        val marsRover = MarsRover(Point(1, 2), NORTH, world)
        marsRover.execute(listOf(FORWARD, FORWARD, RIGHT, FORWARD)) 

        marsRover.detectedObstacle shouldBe Point(1, 0)
        marsRover.direction shouldBe NORTH
        marsRover.position shouldBe Point(1, 1)
    }

    @Test
    internal fun `Should return information about command execution`() {
        val world = WorldGenerator.create(arrayOf(
                arrayOf(DESERT, OBSTACLE, DESERT, DESERT),
                arrayOf(DESERT, DESERT, DESERT, DESERT),
                arrayOf(DESERT, DESERT, DESERT, DESERT),
        ))

        val marsRover = MarsRover(Point(0, 2), EAST, world)
        marsRover.execute(listOf(FORWARD, FORWARD, FORWARD))
        
        marsRover.direction shouldBe EAST
        marsRover.position shouldBe Point(3, 2)
    }
}

sealed class CommandOutput {
    data class EncounterObstacle(val commandList: CommandEnum, val obstacle : Point): CommandOutput()
    object Success: CommandOutput()
}
        