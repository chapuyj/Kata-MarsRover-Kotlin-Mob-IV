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

    @Test
    internal fun `Should move forward`() {
        val position = 1 to 1
        val direction = "N"
        val marsRover = MarsRover(position, direction)

        marsRover.execute(listOf("f"))

        marsRover.position.shouldBe(1 to 0) 
        marsRover.direction shouldBe direction
    }

}
