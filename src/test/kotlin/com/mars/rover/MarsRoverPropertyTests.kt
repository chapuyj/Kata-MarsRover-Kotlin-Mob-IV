package com.mars.rover

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class MarsRoverPropertyTests: StringSpec({

    "Start always in world" {
        checkAll(Arb.int(-50..50), Arb.int(-50..50)) { startColumn, startLine ->
            val worldSize = 20
            val world = World(worldSize, worldSize)
            val position = Point(startColumn, startLine)
            val direction = DirectionEnum.NORTH
            val marsRover = MarsRover(position, direction, world)

            marsRover.position.column shouldBeInRange 0..worldSize
            marsRover.position.line shouldBeInRange 0..worldSize
                        
            marsRover.direction shouldBe DirectionEnum.NORTH
        }
    }

    "Stay always in world after commands execution" {
        checkAll(Arb.int(-50..50), Arb.int(-50..50)) { startColumn, startLine ->
            val worldSize = 20
            val world = World(worldSize, worldSize)
            val position = Point(startColumn, startLine)
            val direction = DirectionEnum.NORTH // generated ?
            val marsRover = MarsRover(position, direction, world)

            val commands = emptyList<CommandEnum>() // to generate
            marsRover.execute(commands)
            
            marsRover.position.column shouldBeInRange 0..worldSize
            marsRover.position.line shouldBeInRange 0..worldSize
        }
    }
    
    // + autogenerated field ?
})