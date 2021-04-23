package com.mars.rover

import com.mars.rover.SquareType.DESERT
import com.mars.rover.SquareType.OBSTACLE
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


internal class MarsRoverTest {

    companion object {
        @JvmStatic
        fun leftCommands() = listOf(
            Arguments.of("N", "W"),
            Arguments.of("W", "S"),
            Arguments.of("E", "N"),
            Arguments.of("S", "E")
        )

        @JvmStatic
        fun rightCommands() = listOf(
            Arguments.of("N", "E"),
            Arguments.of("W", "N"),
            Arguments.of("E", "S"),
            Arguments.of("S", "W")
        )

        @JvmStatic
        fun forwardCommands() = listOf(
            Arguments.of(Point(1, 1), "N", Point(1, 0)),
            Arguments.of(Point(0, 1), "S", Point(0 , 2)),
            Arguments.of(Point(1, 0), "W", Point(0, 0)),
            Arguments.of(Point(1, 0), "E", Point(2, 0)),
            Arguments.of(Point(0, 0), "N", Point(0, 2)),
            Arguments.of(Point(0, 1), "W", Point(2 , 1))
        )

        @JvmStatic
        fun backwardCommands() = listOf(
            Arguments.of(Point(1, 1), "N", Point(1, 2)),
            Arguments.of(Point(1, 1), "S", Point(1, 0)),
            Arguments.of(Point(1, 1), "W", Point(2 , 1)),
            Arguments.of(Point(1, 1), "E", Point(0, 1)),
            Arguments.of(Point(0 , 2), "N", Point(0, 0)),
            Arguments.of(Point(2 , 1), "W", Point(0, 1))
        )
    }

    @Test
    internal fun `Should create instance with initial position and orientation`() {
        val world = World(3,3)
        val position = Point(0, 0)
        val direction = "N"
        val marsRover = MarsRover(position, direction, world)

        marsRover.position shouldBe position
        marsRover.direction shouldBe "N"
    }

    @ParameterizedTest(name = "The new direction is {1} with initial direction {0}")
    @MethodSource(value = ["leftCommands"])
    internal fun `Should turn left`(initialDirection: String, newDirection: String) {
        val world = World(3,3)
        val position = Point(0, 0)
        val marsRover = MarsRover(position, initialDirection, world)

        marsRover.execute(listOf("l"))

        marsRover.position shouldBe position
        marsRover.direction shouldBe newDirection
    }

    @ParameterizedTest(name = "The new direction is {1} with initial direction {0}")
    @MethodSource(value = ["rightCommands"])
    internal fun `Should turn right`(initialDirection: String, newDirection: String) {
        val world = World(3,3)
        val position = Point(0, 0)
        val marsRover = MarsRover(position, initialDirection, world)

        marsRover.execute(listOf("r"))

        marsRover.position shouldBe position
        marsRover.direction shouldBe newDirection
    }

    @ParameterizedTest(name = "The new position is {2} after moving forward from position {0} and direction {1}")
    @MethodSource(value = ["forwardCommands"])
    internal fun `Should move forward`(
        initialPosition: Point,
        direction: String,
        expectedPosition: Point
    ) {
        val world = World(3,3)
        val marsRover = MarsRover(initialPosition, direction, world)

        marsRover.execute(listOf("f"))

        marsRover.position.shouldBe(expectedPosition)
        marsRover.direction shouldBe direction
    }

    @ParameterizedTest(name = "The new position is {2} after moving backward from position {0} and direction {1}")
    @MethodSource(value = ["backwardCommands"])
    internal fun `Should move backward`(
        initialPosition: Point,
        direction: String,
        expectedPosition: Point
    ) {
        val world = World(3,3)
        val marsRover = MarsRover(initialPosition, direction, world)

        marsRover.execute(listOf("b"))

        marsRover.position.shouldBe(expectedPosition)
        marsRover.direction shouldBe direction
    }

    @Test
    internal fun `Should accept multiple commands`() {
        val world = World(3,3)
        val marsRover = MarsRover(Point(2, 2), "N", world)

        marsRover.execute(listOf("l", "f", "l", "b", "r", "r", "r", "f"))

        marsRover.position.shouldBe(Point(2, 1))
        marsRover.direction shouldBe "E"
    }

    @Test
    internal fun `Should do nothing with unknown commands`() {
        val world = World(3,3)
        val marsRover = MarsRover(Point(2, 2), "N", world)

        marsRover.execute(listOf("%", "z", "2", "*"))

        marsRover.position.shouldBe(Point(2, 2))
        marsRover.direction shouldBe "N"
    }

    @Test
    internal fun `Should stay confined in parisian studio`() {
        val world = World(1,1)
        val marsRover = MarsRover(Point(0, 0), "S", world)

        marsRover.execute(listOf("f", "l", "f", "f", "f", "b", "f", "f"))

        marsRover.position.shouldBe(Point(0, 0))
        marsRover.direction shouldBe "E"
    }

    // Generator - Should be moved ?

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
}

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
                if (square == OBSTACLE) {
                    Point(column, line)
                }
                else {
                    null
                }
            }.toTypedArray()

            obstaclePositions.forEach {print(it)}

            return World(height, width, obstaclePositions)
        }

        private fun worldIsTooSmall(squares: Array<Array<SquareType>>) =
            squares.count() < 1 || squares[0].count() < 1

        private fun parisianStudio(): World = World(1, 1)
    }
}
