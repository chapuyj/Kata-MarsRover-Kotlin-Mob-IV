package com.mars.rover

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
            Arguments.of(1 to 1, "N", 1 to 0),
            Arguments.of(0 to 1, "S", 0 to 2),
            Arguments.of(1 to 0, "W", 0 to 0),
            Arguments.of(1 to 0, "E", 2 to 0),
        )

        @JvmStatic
        fun backwardCommands() = listOf(
            Arguments.of(1 to 1, "N", 1 to 2),
            Arguments.of(1 to 1, "S", 1 to 0),
            Arguments.of(1 to 1, "W", 2 to 1),
            Arguments.of(1 to 1, "E", 0 to 1)
        )
    }

    @Test
    internal fun `Should create instance with initial position and orientation`() {
        val position = 0 to 0
        val direction = "N"
        val marsRover = MarsRover(position, direction)

        marsRover.position shouldBe position
        marsRover.direction shouldBe "N"
    }

    @ParameterizedTest(name = "The new direction is {1} with initial direction {0}")
    @MethodSource(value = ["leftCommands"])
    internal fun `Should turn left`(initialDirection: String, newDirection: String) {
        val position = 0 to 0
        val marsRover = MarsRover(position, initialDirection)

        marsRover.execute(listOf("l"))

        marsRover.position shouldBe position
        marsRover.direction shouldBe newDirection
    }

    @ParameterizedTest(name = "The new direction is {1} with initial direction {0}")
    @MethodSource(value = ["rightCommands"])
    internal fun `Should turn right`(initialDirection: String, newDirection: String) {
        val position = 0 to 0
        val marsRover = MarsRover(position, initialDirection)

        marsRover.execute(listOf("r"))

        marsRover.position shouldBe position
        marsRover.direction shouldBe newDirection
    }

    @ParameterizedTest(name = "The new position is {2} after moving forward from position {0} and direction {1}")
    @MethodSource(value = ["forwardCommands"])
    internal fun `Should move forward`(
        initialPosition: Pair<Int, Int>,
        direction: String,
        expectedPosition: Pair<Int, Int>
    ) {
        val marsRover = MarsRover(initialPosition, direction)

        marsRover.execute(listOf("f"))

        marsRover.position.shouldBe(expectedPosition)
        marsRover.direction shouldBe direction
    }

    @ParameterizedTest(name = "The new position is {2} after moving backward from position {0} and direction {1}")
    @MethodSource(value = ["backwardCommands"])
    internal fun `Should move backward`(
        initialPosition: Pair<Int, Int>,
        direction: String,
        expectedPosition: Pair<Int, Int>
    ) {
        val marsRover = MarsRover(initialPosition, direction)

        marsRover.execute(listOf("b"))

        marsRover.position.shouldBe(expectedPosition)
        marsRover.direction shouldBe direction
    }

    @Test
    internal fun `Should accept multiple commands`() {
        val marsRover = MarsRover(2 to 2, "N")

        marsRover.execute(listOf("l", "f", "l", "b", "r", "r", "r", "f"))

        marsRover.position.shouldBe(2 to 1)
        marsRover.direction shouldBe "E"
    }

    @Test
    internal fun `Should do nothing with unknown commands`() {
        val marsRover = MarsRover(2 to 2, "N")

        marsRover.execute(listOf("%", "z", "2", "*"))

        marsRover.position.shouldBe(2 to 2)
        marsRover.direction shouldBe "N"
    }
}
