package com.mars.rover

enum class DirectionEnum {
    NORTH, SOUTH, EAST, WEST;

    fun opposite() = when(this) {
        NORTH -> SOUTH
        SOUTH -> NORTH
        WEST -> EAST
        EAST -> WEST
    }
}